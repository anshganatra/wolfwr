package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.TransactionItemDTO;
import com.csc540.wolfwr.service.TransactionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Transaction Item API", description = "CRUD operations for transaction items")
@RestController
@RequestMapping("/transaction-item")
public class TransactionItemController {
    private final TransactionItemService transactionItemService;

    public TransactionItemController(TransactionItemService transactionItemService) {
        this.transactionItemService = transactionItemService;
    }

    @Operation(summary = "Create a new transaction item", description = "Creates a new transaction item with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created transaction item"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TransactionItemDTO> createTransactionItem(@Valid @RequestBody TransactionItemDTO transactionItemDTO) {
        TransactionItemDTO createdTransactionItem = transactionItemService.createTransactionItem(transactionItemDTO);
        return ResponseEntity.ok(createdTransactionItem);
    }

    @Operation(summary = "Get all transaction items", description = "Retrieves all transaction items")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<TransactionItemDTO>> getAllTransactionItems() {
        List<TransactionItemDTO> list = transactionItemService.getAllTransactionItems();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Update an existing transaction item", description = "Updates a transaction item's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated transaction item"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/item-report")
    public ResponseEntity<List<Map<String, Object>>> getAllTransactionItemsBetweenTwoDates(@RequestParam LocalDate startDate,
                                                                                           @RequestParam LocalDate endDate,
                                                                                           @RequestParam Integer memberId) {
        List<Map<String, Object>> transactionItems = transactionItemService.getAllTransactionItemsBetweenTwoDates(startDate, endDate, memberId);
        return ResponseEntity.ok(transactionItems);
    }

    @Operation(summary = "Update an existing transaction item", description = "Updates a transaction item's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated transaction item"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping
    public ResponseEntity<TransactionItemDTO> updateTransactionItem(@RequestParam Integer transactionId,
                                                                    @RequestParam Integer productBatchId,
                                                @Valid @RequestBody TransactionItemDTO transactionItemDTO) {
        TransactionItemDTO updatedTransactionItem = transactionItemService.updateTransactionItem(transactionItemDTO, transactionId, productBatchId);
        return ResponseEntity.ok(updatedTransactionItem);
    }

    @Operation(summary = "Delete a transaction item", description = "Deletes a transaction item by its transaction and product batch ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted transaction item")
    @DeleteMapping
    public ResponseEntity<Void> deleteTransactionItem(@RequestParam Integer transactionId,
                                            @RequestParam Integer productBatchId) {
        transactionItemService.deleteTransactionItem(transactionId, productBatchId);
        return ResponseEntity.noContent().build();
    }
}
