package com.csc540.wolfwr.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csc540.wolfwr.dto.RewardDTO;
import com.csc540.wolfwr.service.RewardService;
import com.csc540.wolfwr.service.ShipmentService;
import com.csc540.wolfwr.service.TransactionItemService;
import com.csc540.wolfwr.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Billing Staff API", description = "CRUD operations for billing staff")
@RestController
@RequestMapping("/billing-staff")
public class BillingStaffViewController {

    private final ShipmentService shipmentService;
    private final RewardService rewardService;
    private final TransactionService transactionService;
    private final TransactionItemService transactionItemService;

    public BillingStaffViewController(ShipmentService shipmentService, 
                                  RewardService rewardService,
                                  TransactionService transactionService, 
                                  TransactionItemService transactionItemService) {
        this.shipmentService = shipmentService;
        this.rewardService = rewardService;
        this.transactionService = transactionService;
        this.transactionItemService = transactionItemService;
    }
    
    // Generate Supplier Bill
    @Operation(
        summary = "Get itemized bills for suppliers",
        description = "Retrieves an itemized bill based on optional supplierId, storeId, and shipmentDate parameters. If shipmentDate is not provided, the current date is used."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itemized bill retrieved successfully")
    })
    @GetMapping("/shipments/itemized-bill")
    public ResponseEntity<List<Map<String, Object>>> getItemizedBill(
            @RequestParam(value = "supplierId", required = false) Integer supplierId,
            @RequestParam(value = "storeId", required = false) Integer storeId,
            @RequestParam(value = "shipmentDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shipmentDate) {

        List<Map<String, Object>> itemizedBill = shipmentService.getItemizedBill(supplierId, storeId, shipmentDate);
        return ResponseEntity.ok(itemizedBill);
    }

    // Get all transactions between two dates for a given member
    @Operation(summary = "Get transactions by a member between two dates", 
               description = "Retrieves transactions by a member between two dates")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping("/transactions/member")
    public ResponseEntity<List<Map<String, Object>>> getTransactionsByMemberAndDates(@RequestParam Integer memberId,
                                                                                @RequestParam LocalDate startDate,
                                                                                @RequestParam LocalDate endDate) {
        List<Map<String, Object>> transactions = transactionService.getTransactionsByMemberAndDates(memberId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    // Get sales for a given day
    @Operation(
            summary = "Get sales for a particular day, optionally for a particular store",
            description = "Get total transaction price for a particular day for all stores, if a store ID is provided fetch it only for the given store"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily sales summary retrieved successfully")
    })
    @GetMapping("/transactions/daily-sales")

    public ResponseEntity<List<Map<String, Object>>> getDailySales(@RequestParam(value = "storeId", required = false) Integer storeId, @RequestParam(value = "date", required = true) LocalDate
                                                                   date) {
        List<Map<String, Object>> dailySales = transactionService.getDailySales(date, storeId);
        return ResponseEntity.ok(dailySales);
    }

    // Generate a sales report
    @Operation(
            summary = "Generate a sales report",
            description = "Returns sales report grouped by day/month/quarter/year (based on reportType) along with store_ID and total sales. " +
                    "Must provide exactly one reportType among daily, monthly, quarterly, or annually. Also accepts startDate, optional endDate, and optional storeId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/transactions/sales-report")
    public ResponseEntity<List<Map<String, Object>>> getSalesReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        List<Map<String, Object>> report = transactionService.getSalesReport(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }

    // Get all transaction items between two dates for a given member
    @Operation(summary = "Get all transaction items between two dates", 
               description = "Retrieves all transactions for a given customer made between two dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned transaction items"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/transaction-items/item-report")
    public ResponseEntity<List<Map<String, Object>>> getAllTransactionItemsBetweenTwoDates(@RequestParam LocalDate startDate,
                                                                                           @RequestParam LocalDate endDate,
                                                                                           @RequestParam Integer memberId) {
        List<Map<String, Object>> transactionItems = transactionItemService.getAllTransactionItemsBetweenTwoDates(startDate, endDate, memberId);
        return ResponseEntity.ok(transactionItems);
    }

    // Create new reward
    @PostMapping("/rewards")
    public ResponseEntity<RewardDTO> createReward(@Valid @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(rewardService.createReward(rewardDTO));
    }

    // Get rewards for a given member in a given year
    @GetMapping("/rewards/{memberId}/{year}")
    public ResponseEntity<RewardDTO> getReward(@PathVariable Integer memberId, @PathVariable Integer year) {
        return ResponseEntity.ok(rewardService.getReward(memberId, year));
    }

    // Get all rewards
    @GetMapping("/rewards")
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    // Get all rewards for all members for a given year
    @Operation(summary = "Get all rewards for a given year", 
               description = "Gets all rewards for a given year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rewards retrieved successfully"),
        @ApiResponse(responseCode =  "400", description = "Invalid input")
    })
    @GetMapping("/rewards/{year}")
    public ResponseEntity<List<RewardDTO>> getRewardsByYear(@PathVariable Integer year) {
        List<RewardDTO> rewards = rewardService.getRewardsByYear(year);
        return ResponseEntity.ok(rewards);
    }

    // Update a reward for a member in a given year
    @PutMapping("/rewards/{memberId}/{year}")
    public ResponseEntity<RewardDTO> updateReward(@PathVariable Integer memberId,
                                                  @PathVariable Integer year,
                                                  @Valid @RequestBody RewardDTO rewardDTO) {
        rewardDTO.setMemberId(memberId);
        rewardDTO.setYear(year);
        return ResponseEntity.ok(rewardService.updateReward(rewardDTO));
    }

    // Delete a reward for a member in a given year
    @DeleteMapping("/rewards/{memberId}/{year}")
    public ResponseEntity<Void> deleteReward(@PathVariable Integer memberId, @PathVariable Integer year) {
        rewardService.deleteReward(memberId, year);
        return ResponseEntity.noContent().build();
    }
}
