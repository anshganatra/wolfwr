package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.InventoryDAO;
import com.csc540.wolfwr.dto.InventoryDTO;
import com.csc540.wolfwr.dto.ReturnTransactionDTO;
import com.csc540.wolfwr.dto.TransactionDTO;
import com.csc540.wolfwr.dto.TransactionItemDTO;
import com.csc540.wolfwr.model.Inventory;
import com.csc540.wolfwr.model.TransactionItem;
import jakarta.validation.constraints.Null;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryDAO inventoryDAO;
    private final TransactionService transactionService;
    private final TransactionItemService transactionItemService;
    private final ReturnTransactionService returnTransactionService;

    public InventoryService(InventoryDAO inventoryDAO, TransactionService transactionService, TransactionItemService transactionItemService, ReturnTransactionService returnTransactionService) {
        this.inventoryDAO = inventoryDAO;
        this.transactionService = transactionService;
        this.transactionItemService = transactionItemService;
        this.returnTransactionService = returnTransactionService;
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

    // Process return
    // TODO make this whole thing an SQL transaction add try catch block
    public void returnItem(Integer ogTid, Integer shipmentId, Integer productId, Integer quantity, Integer cashierId){
        List<TransactionItemDTO> transactionItemDTOS = transactionItemService.getAllTransactionItemsFromATransaction(ogTid);
        boolean productExists = transactionItemDTOS.stream()
                .anyMatch(item -> item.getProductBatchId().equals(productId));
        if (!productExists) {
            throw new IllegalArgumentException("Product does not belong to the original transaction.");
        }
        TransactionItemDTO relevantTransactionItem = transactionItemDTOS.stream().filter(item -> item.getProductBatchId().equals(productId)).findFirst().orElse(null);
        TransactionDTO originalTransaction = transactionService.getTransactionDTOById(ogTid);

        // create new transaction with type return
        TransactionDTO newReturnTransaction = new TransactionDTO();
        newReturnTransaction.setType("RETURN");
        newReturnTransaction.setStoreId(originalTransaction.getStoreId());
        newReturnTransaction.setMemberId(originalTransaction.getMemberId());
        newReturnTransaction.setCashierId(originalTransaction.getCashierId());
        newReturnTransaction.setDate(LocalDate.now().atStartOfDay());
        newReturnTransaction.setTotalPrice(Objects.equals(relevantTransactionItem.getDiscountedPrice(), new BigDecimal(0)) ? relevantTransactionItem.getPrice() :  relevantTransactionItem.getDiscountedPrice());
        transactionService.createTransaction(newReturnTransaction);

        // map return transaction to OG transaction
        ReturnTransactionDTO returnTransaction = new ReturnTransactionDTO();
        returnTransaction.setTransactionId(ogTid);
        // TODO fix creation and update methods so that the below field won't return NPE
        returnTransaction.setTransactionId(newReturnTransaction.getTransactionId());
        returnTransactionService.create(returnTransaction);

        InventoryDTO updatedInventory = getInventoryByShipmentId(shipmentId);
        updatedInventory.setProductQty(updatedInventory.getProductQty()+quantity);
        updateInventory(updatedInventory);
    }

    }