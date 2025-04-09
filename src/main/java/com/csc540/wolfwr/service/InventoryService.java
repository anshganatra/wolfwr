package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.InventoryDAO;
import com.csc540.wolfwr.dto.InventoryDTO;
import com.csc540.wolfwr.model.Inventory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryDAO inventoryDAO;

    public InventoryService(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    // Create a new inventory record
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        // Additional business checks can be added here if needed.
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryDTO, inventory);
        inventoryDAO.save(inventory);
        return inventoryDTO;
    }

    // Retrieve an inventory record by shipment_ID
    public InventoryDTO getInventoryByShipmentId(Integer shipmentId) {
        Inventory inventory = inventoryDAO.getInventoryByShipmentId(shipmentId);
        InventoryDTO dto = new InventoryDTO();
        BeanUtils.copyProperties(inventory, dto);
        return dto;
    }

    // Retrieve all inventory records
    public List<InventoryDTO> getAllInventory() {
        List<Inventory> inventoryList = inventoryDAO.getAllInventory();
        return inventoryList.stream().map(inventory -> {
            InventoryDTO dto = new InventoryDTO();
            BeanUtils.copyProperties(inventory, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing inventory record
    public InventoryDTO updateInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryDTO, inventory);
        inventoryDAO.update(inventory);
        return inventoryDTO;
    }

    // Delete an inventory record by shipment_ID
    public void deleteInventory(Integer shipmentId) {
        inventoryDAO.delete(shipmentId);
    }

    public List<Map<String, Object>> getLowStockInventory(Integer storeId) {
        return inventoryDAO.getLowStockInventory(storeId);
    }

    // Get product stock for all stores or for a particular store
    public List<Map<String, Object>> getProductStock(Integer storeId) {
        return inventoryDAO.getProductStock(storeId);
    }

    }