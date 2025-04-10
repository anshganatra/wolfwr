package com.csc540.wolfwr.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionReportService {

    private final TransactionService transactionService;
    private final TransactionItemService transactionItemService;
    private final ShipmentService shipmentService;
    private final ProductService productService;

    public TransactionReportService(TransactionService transactionService,
                                    TransactionItemService transactionItemService,
                                    ShipmentService shipmentService,
                                    ProductService productService) {
        this.transactionService = transactionService;
        this.transactionItemService = transactionItemService;
        this.shipmentService = shipmentService;
        this.productService = productService;
    }

    /**
     * Retrieves transactions for a given member between startDate and endDate,
     * then for each transaction, retrieves the transaction items and enriches each item
     * with the product name (by first looking up the shipment by product_batch_id,
     * then fetching the product id from the shipment, and finally the product name).
     *
     * Returns a list of maps, where each map represents a transaction with an added "items" key.
     */
    public List<Map<String, Object>> getEnrichedTransactionsByMemberAndDate(
            Integer memberId,
            LocalDate startDate,
            LocalDate endDate) {

        // 1. Retrieve transactions for the member
        List<Map<String, Object>> transactions = transactionService.getTransactionsByMemberAndDate(
                memberId, startDate, endDate);

        // 2. Process each transaction to fetch and enrich its transaction items
        for (Map<String, Object> transaction : transactions) {
            // Ensure the transaction map contains the expected key
            Integer transactionId = (Integer) transaction.get("transaction_ID");
            if (transactionId == null) {
                continue; // skip if transactionId is missing
            }

            // Retrieve transaction items for this transaction
            List<Map<String, Object>> items = transactionItemService.getTransactionItemsByTransactionId(transactionId);

            // Iterate over each transaction item
            for (Map<String, Object> item : items) {
                // Get the product_batch_id which is the shipment id
                Integer shipmentId = (Integer) item.get("product_batch_ID");
                if (shipmentId == null) {
                    continue; // skip item if no shipment id is found
                }

                // Retrieve shipment info as a map (must have a column "product_ID")
                Map<String, Object> shipment = shipmentService.getShipmentAsMapById(shipmentId);
                if (shipment != null && shipment.containsKey("product_ID")) {
                    Integer productId = (Integer) shipment.get("product_ID");
                    // Retrieve the product name for this product id
                    String productName = productService.getProductById(productId).getProductName();
                    // Add the product name to the item map
                    item.put("productName", productName);
                }
            }

            // Nest the list of enriched items into the transaction map under key "items"
            transaction.put("items", items);
        }

        return transactions;
    }
}