package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.ShipmentDAO;
import com.csc540.wolfwr.dto.ShipmentDTO;
import com.csc540.wolfwr.model.Shipment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentDAO shipmentDAO;

    public ShipmentService(ShipmentDAO shipmentDAO) {
        this.shipmentDAO = shipmentDAO;
    }

    // Create a new shipment with business rule checks
    public ShipmentDTO createShipment(ShipmentDTO shipmentDTO) {
        if (Objects.isNull(shipmentDTO.getShipmentDate())) {
            shipmentDTO.setShipmentDate(LocalDate.now());
        }
        // Business rule: production_date <= exp_date (if exp_date provided)
        if (shipmentDTO.getExpDate() != null && shipmentDTO.getProductionDate().isAfter(shipmentDTO.getExpDate())) {
            throw new IllegalArgumentException("Production date cannot be after expiration date.");
        }
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(shipmentDTO, shipment);
        shipment.setShipmentProcessed(false);
        Shipment createdShipment = shipmentDAO.save(shipment);
        // Optionally, set the generated shipmentId back into the DTO if needed.
        shipmentDTO.setShipmentId(createdShipment.getShipmentId());
        return shipmentDTO;
    }

    // Retrieve shipment by ID
    public ShipmentDTO getShipmentById(Integer shipmentId) {
        Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
        if (Objects.nonNull(shipment)) {
            ShipmentDTO dto = new ShipmentDTO();
            BeanUtils.copyProperties(shipment, dto);
            return dto;
        }
        return null;
    }

    // Retrieve all shipments
    public List<ShipmentDTO> getAllShipments() {
        List<Shipment> shipments = shipmentDAO.getAllShipments();
        return shipments.stream().map(shipment -> {
            ShipmentDTO dto = new ShipmentDTO();
            BeanUtils.copyProperties(shipment, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> identifyExpiredProducts() {
        return shipmentDAO.identifyExpiredProducts();
    }

    // Update an existing shipment with business rule checks
    public ShipmentDTO updateShipment(ShipmentDTO shipmentDTO) {
        if (shipmentDTO.getExpDate() != null && shipmentDTO.getProductionDate().isAfter(shipmentDTO.getExpDate())) {
            throw new IllegalArgumentException("Production date cannot be after expiration date.");
        }
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(shipmentDTO, shipment);
        shipmentDAO.update(shipment);
        return shipmentDTO;
    }

    // Delete shipment by ID
    public void deleteShipment(Integer shipmentId) {
        shipmentDAO.delete(shipmentId);
    }

    public List<Map<String, Object>> getItemizedBill(Integer supplierId, Integer storeId, LocalDate shipmentDate) {
        return shipmentDAO.getItemizedBill(supplierId, storeId, shipmentDate);
    }

    // Get expired shipments
    public List<Map<String, Object>> getExpiredShipments(Integer storeId) {
        return shipmentDAO.getExpiredShipments(storeId);
    }

    /**
     * Returns the shipment record as a map for the given shipment id.
     *
     * @param shipmentId the shipment id to look for.
     * @return a Map representing the shipment, or null if not found.
     */
    public Map<String, Object> getShipmentAsMapById(Integer shipmentId) {
        return shipmentDAO.getShipmentAsMapById(shipmentId);
    }
}