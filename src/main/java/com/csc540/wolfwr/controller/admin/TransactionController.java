package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.TransactionDTO;
import com.csc540.wolfwr.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Admin View for Transaction API", description = "CRUD operations for transactions")
@RestController
@RequestMapping("/admin/transactions")
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


    @Operation(
            summary = "Get sales for a particular day, optionally for a particular store",
            description = "Get total discounted transaction price for a particular day for all stores, if a store ID is provided fetch it only for the given store"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily sales summary retrieved successfully")
    })
    @GetMapping("/daily-sales")

    public ResponseEntity<List<Map<String, Object>>> getDailySales(@RequestParam(value = "storeId", required = false) Integer storeId, @RequestParam(value = "date", required = true) LocalDate
                                                                   date) {
        List<Map<String, Object>> dailySales = transactionService.getDailySales(date, storeId);
        return ResponseEntity.ok(dailySales);
    }

    @Operation(
            summary = "Generate a sales report",
            description = "Returns sales report grouped by day/month/quarter/year (based on reportType) along with store_ID and total sales. " +
                    "Must provide exactly one reportType among daily, monthly, quarterly, or annually. Also accepts startDate, optional endDate, and optional storeId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/sales-report")
    public ResponseEntity<List<Map<String, Object>>> getSalesReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        List<Map<String, Object>> report = transactionService.getSalesReport(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }
}
