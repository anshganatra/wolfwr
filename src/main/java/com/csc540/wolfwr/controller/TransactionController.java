package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.TransactionDTO;
import com.csc540.wolfwr.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Transaction API", description = "CRUD operations for transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create a new transaction", description = "Creates a new transaction with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok(createdTransaction);
    }

    @Operation(summary = "Get a transaction by ID", description = "Retrieves a transaction by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Integer transactionId) {
        TransactionDTO transactionDTO = transactionService.getTransactionDTOById(transactionId);
        return ResponseEntity.ok(transactionDTO);
    }

    @Operation(summary = "Get all transactions", description = "Retrieves all transactions")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by a member between two dates", 
               description = "Retrieves transactions by a member between two dates")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping("/member")
    public ResponseEntity<List<Map<String, Object>>> getTransactionsByMemberAndDates(@RequestParam Integer memberId,
                                                                                @RequestParam LocalDate startDate,
                                                                                @RequestParam LocalDate endDate) {
        List<Map<String, Object>> transactions = transactionService.getTransactionsByMemberAndDates(memberId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Generates a report of the sales growth", 
               description = "Retrieves transactions by a member between two dates")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/sales-growth")
    public ResponseEntity<List<Map<String, Object>>> generateSalesGrowthReport(@RequestParam LocalDate currentPeriodStartDate,
                                                                               @RequestParam LocalDate currentPeriodEndDate,
                                                                               @RequestParam LocalDate previousPeriodStartDate,
                                                                               @RequestParam LocalDate previousPeriodEndDate,
                                                                               @RequestParam(required = false) Integer storeId) {
        List<Map<String, Object>> transactions = transactionService.generateSalesGrowthReport(currentPeriodStartDate, currentPeriodEndDate, 
                                                 previousPeriodStartDate, previousPeriodEndDate, storeId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Update an existing transaction", description = "Updates an existing transaction with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Integer transactionId,
                                                            @RequestBody TransactionDTO transactionDTO) {
        transactionDTO.setTransactionId(transactionId);
        TransactionDTO updatedTransaction = transactionService.updateTransaction(transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction by its unique ID")
    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
}
