package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionDAO;
import com.csc540.wolfwr.dao.TransactionItemDAO;
import com.csc540.wolfwr.dto.*;
import com.csc540.wolfwr.model.Transaction;
import com.csc540.wolfwr.model.TransactionItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class BillingService {

    private final TransactionDAO transactionDAO;
    private final TransactionItemDAO transactionItemDAO;
    private final DiscountService discountService;
    private final ShipmentService shipmentService;
    private final InventoryService inventoryService;

    public BillingService(TransactionDAO transactionDAO, TransactionItemDAO transactionItemDAO, DiscountService discountService, ShipmentService shipmentService, InventoryService inventoryService) {
        this.transactionDAO = transactionDAO;
        this.transactionItemDAO = transactionItemDAO;
        this.discountService = discountService;
        this.shipmentService = shipmentService;
        this.inventoryService = inventoryService;
    }

    private Boolean validateLineItem(BillItemDTO billItem) {
        // check if the shipment actually exists
        if (Objects.isNull(shipmentService.getShipmentById(billItem.getProductBatchId()))) {
            return Boolean.FALSE;
        }
        if (billItem.getQuantity() < 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // Helper method to calculate the effective discount value
    private BigDecimal calculateEffectiveDiscount(DiscountDTO discountDTO, BigDecimal marketPrice) {
        if (discountDTO.getType().equals("PERCENTAGE")) {
            // If the discount is a percentage, calculate the discount value based on market price
            BigDecimal discountPercent = discountDTO.getValue().divide(new BigDecimal(100));
            return marketPrice.multiply(discountPercent);
        } else if (discountDTO.getType().equals("DOLLAR")) {
            // If the discount is a dollar amount, return the value directly
            return discountDTO.getValue();
        }
        return new BigDecimal(0);  // Default to 0.0 if the discount type is not recognized
    }

    private BigDecimal findMaxDiscountPrice(List<DiscountDTO> discountDTOs, InventoryDTO inventoryDTO) {
        BigDecimal marketPrice = inventoryDTO != null ? inventoryDTO.getMarketPrice() : new BigDecimal(0);

        // Find the maximum effective discount and calculate the final price
        BigDecimal maxDiscountValue = discountDTOs.stream()
                .map(d -> calculateEffectiveDiscount(d, marketPrice))
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0)); // If no discounts, max discount is 0.0

        // Calculate the final price after applying the max discount
        return marketPrice.subtract(maxDiscountValue);
    }

    public Integer createBill(BillRequestDTO billRequest) {
        // Step 1: Create Transaction
        Transaction transaction = new Transaction();
        transaction.setStoreId(billRequest.getStoreId());
        transaction.setCashierId(billRequest.getCashierId());
        transaction.setMemberId(billRequest.getMemberId());
        transaction.setDate(billRequest.getDate() != null ? billRequest.getDate() : LocalDateTime.now());
        transaction.setType("PURCHASE");
        transaction.setCompletedStatus(false);
        Integer transactionId = transactionDAO.save(transaction); // assumes auto-incremented ID is populated
        transaction.setTransactionId(transactionId);

        BigDecimal totalPrice = new BigDecimal(0);
        // Step 2: Create TransactionItems
        for (BillItemDTO item : billRequest.getItems()) {
            // validate if item exists in inventory
            if (!validateLineItem(item)) {
                throw new IllegalArgumentException("Bill item has invalid ID or qty");
            }
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransactionId(transaction.getTransactionId()); // assuming set by DB or DAO
            transactionItem.setProductBatchId(item.getProductBatchId());
            transactionItem.setQuantity(item.getQuantity());
            // get price from discount / shipment and set it
            InventoryDTO currentInventory = inventoryService.getInventoryByShipmentId(item.getProductBatchId());
            List<DiscountDTO> relevantDiscounts = discountService.getByProductIdOrShipmentId(currentInventory.getProductId(), item.getProductBatchId());
            if (relevantDiscounts.isEmpty()) {
                transactionItem.setPrice(currentInventory.getMarketPrice());
            }  else{
                transactionItem.setPrice(findMaxDiscountPrice(relevantDiscounts, currentInventory));
            }
            // update iventory with new quantity
            Integer newProductQty = currentInventory.getProductQty() - item.getQuantity();
            currentInventory.setProductQty(newProductQty);
            inventoryService.updateInventory(currentInventory);
            // save transaction item
            transactionItemDAO.save(transactionItem);
        }
        transaction.setCompletedStatus(true);
        return transaction.getTransactionId();
    }
}
