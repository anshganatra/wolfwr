package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.InventoryDAO;
import com.csc540.wolfwr.dao.ShipmentDAO;
import com.csc540.wolfwr.dao.StoreDAO;
import com.csc540.wolfwr.dto.StoreDTO;
import com.csc540.wolfwr.dto.TransferDTO;
import com.csc540.wolfwr.model.Store;
import com.csc540.wolfwr.model.Shipment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;


@Service
public class StoreService {

    private final StoreDAO storeDAO;
    private final ShipmentDAO shipmentDAO;
    private final InventoryDAO inventoryDAO;
    private final JdbcTemplate jdbcTemplate;

    

    public StoreService(StoreDAO storeDAO, ShipmentDAO shipmentDAO, InventoryDAO inventoryDAO, JdbcTemplate jdbcTemplate) {
        this.storeDAO = storeDAO;
        this.shipmentDAO = shipmentDAO;
        this.inventoryDAO = inventoryDAO;
        this.jdbcTemplate = jdbcTemplate;

    }

    // Create a new store
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = new Store();
        BeanUtils.copyProperties(storeDTO, store);
        storeDAO.save(store);
        return storeDTO;
    }

    // Retrieve a store by its ID (model)
    public Store getStoreById(Integer storeId) {
        return storeDAO.getStoreById(storeId);
    }

    // Retrieve a store by its ID as a DTO
    public StoreDTO getStoreDTOById(Integer storeId) {
        Store store = storeDAO.getStoreById(storeId);
        StoreDTO dto = new StoreDTO();
        BeanUtils.copyProperties(store, dto);
        return dto;
    }

    // Retrieve all stores as DTOs
    public List<StoreDTO> getAllStores() {
        List<Store> stores = storeDAO.getAllStores();
        return stores.stream().map(store -> {
            StoreDTO dto = new StoreDTO();
            BeanUtils.copyProperties(store, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing store
    public StoreDTO updateStore(StoreDTO storeDTO) {
        Store store = new Store();
        BeanUtils.copyProperties(storeDTO, store);
        storeDAO.update(store);
        return storeDTO;
    }

    // Delete a store by ID
    public void deleteStore(Integer storeId) {
        storeDAO.delete(storeId);
    }

    // Handle product transfer between stores
    @Transactional
    public void transferProducts(TransferDTO transferDTO) {
        // Step 1: Reduce stock from the sending store's inventory
        int rowsAffected = inventoryDAO.reduceInventoryStock(transferDTO.getSourceStoreId(),
                transferDTO.getShipmentId(),
                transferDTO.getProductQty());
        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Insufficient stock in the source store.");
        }

        // Step 2: Create a new shipment for the receiving store
        Shipment sourceShipment = shipmentDAO.getShipmentById(transferDTO.getShipmentId());
        Shipment newShipment = new Shipment();
        newShipment.setSupplierId(sourceShipment.getSupplierId());
        newShipment.setProductId(transferDTO.getProductId());
        newShipment.setStoreId(transferDTO.getDestinationStoreId());
        newShipment.setBuyPrice(sourceShipment.getBuyPrice());
        newShipment.setProductionDate(sourceShipment.getProductionDate());
        newShipment.setShipmentDate(LocalDate.now());
        newShipment.setExpDate(sourceShipment.getExpDate());
        newShipment.setQuantity(transferDTO.getProductQty());

        // Insert the new shipment and get the generated shipment ID
        int generatedShipmentId = shipmentDAO.createShipmentForReceivingStore(newShipment);

        // Step 3: Add this shipment to the receiving store's inventory
        inventoryDAO.addInventoryToReceivingStore(transferDTO.getDestinationStoreId(),
                generatedShipmentId,  // Use the generated shipment ID
                transferDTO.getProductId(),
                sourceShipment.getBuyPrice(),
                transferDTO.getProductQty());
    }
    
    // Method to calculate inventory turnover for all stores
    public List<Map<String, Object>> calculateInventoryTurnover() {
        // Remove the WHERE clause since we want all stores
        String sql = "SELECT i.store_ID, i.product_ID, " +
                     "SUM(ti.quantity) / SUM(i.product_qty) AS inventory_turnover_ratio " +
                     "FROM Inventory i " +
                     "JOIN Shipments s ON i.product_ID = s.product_ID " +
                     "JOIN TransactionItems ti ON s.shipment_ID = ti.product_batch_ID " +
                     "GROUP BY i.store_ID, i.product_ID";

        // Execute the query and return the result as a List of Maps
        return jdbcTemplate.queryForList(sql);
    }

}
