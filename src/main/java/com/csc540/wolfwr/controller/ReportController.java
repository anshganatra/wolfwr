package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.service.InventoryService;
import com.csc540.wolfwr.service.MemberService;
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

@Tag(name = "Reports API", description = "Generate reports for sales and other metrics")
@RestController("reportController") // Custom bean name
@RequestMapping("/reports")
public class ReportController {

    private final TransactionService transactionService;
    private final InventoryService inventoryService;
    private final MemberService memberService; 


    public ReportController(TransactionService transactionService, InventoryService inventoryService, MemberService memberService) {
        this.transactionService = transactionService;
        this.inventoryService = inventoryService;
        this.memberService = memberService;


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
    @GetMapping("/total-sales")
    public ResponseEntity<List<Map<String, Object>>> getTotalSalesReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        // Calling the service method to generate the total sales report
        List<Map<String, Object>> report = transactionService.getSalesReport(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/sales-growth")
    public ResponseEntity<List<Map<String, Object>>> generateSalesGrowthReport(
            @RequestParam LocalDate currentPeriodStartDate,
            @RequestParam LocalDate currentPeriodEndDate,
            @RequestParam LocalDate previousPeriodStartDate,
            @RequestParam LocalDate previousPeriodEndDate,
            @RequestParam(required = false) Integer storeId) {

        // Generate the sales growth report by calling the service method
        List<Map<String, Object>> transactions = transactionService.generateSalesGrowthReport(
                currentPeriodStartDate, currentPeriodEndDate, previousPeriodStartDate, previousPeriodEndDate, storeId);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(
            summary = "Get current product stocks for a store or for all stores",
            description = "Retrieves current quantity of each product within a store or within all stores"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product stock summary retrieved successfully")
    })
    @GetMapping("/product-stock")
    public ResponseEntity<List<Map<String, Object>>> getProductStockSummary(
            @RequestParam(value = "storeId", required = false) Integer storeId,
    		@RequestParam(value = "productId", required = false) Integer productId) {

        // Call the inventory service method to get the product stock summary
        List<Map<String, Object>> productStock = inventoryService.getProductStock(storeId,productId);
        return ResponseEntity.ok(productStock);
    }
    
    @Operation(
            summary = "Get customer growth report",
            description = "Returns how many new active members joined (active_status=1) in a given date range, grouped by day/month/quarter/year. Optional store filter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer growth report retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/customer-growth")
    public ResponseEntity<List<Map<String, Object>>> getCustomerGrowthReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        // Call the member service method to get the customer growth report
        List<Map<String, Object>> report = memberService.getCustomerGrowth(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "Generate a total profit report",
            description = "Returns total profit for the given date range, grouped by day/month/quarter/year. Also accepts storeId filter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total profit report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/total-profit")
    public ResponseEntity<List<Map<String, Object>>> getTotalProfitReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        // Calling the service method to generate the total profit report
        List<Map<String, Object>> report = transactionService.getProfitReport(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "Generate a total revenue report",
            description = "Returns total revenue (total sales) for the given date range, grouped by day/month/quarter/year. Also accepts storeId filter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total revenue report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/total-revenue")
    public ResponseEntity<List<Map<String, Object>>> getTotalRevenueReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId) {

        // Calling the service method to generate the total revenue report
        List<Map<String, Object>> report = transactionService.getRevenueReport(reportType, startDate, endDate, storeId);
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "Generate a customer activity report",
            description = "Returns the total purchase amount for a given customer (member) during a specified date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer activity report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/customer-activity")
    public ResponseEntity<List<Map<String, Object>>> getCustomerActivityReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "memberId", required = false) Integer memberId) {

        // Calling the service method to generate the customer activity report
        List<Map<String, Object>> report = transactionService.getCustomerActivityReport(startDate, endDate, memberId);
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "Get Membership Level and Rewards",
            description = "Returns the membership level and total rewards for each member, optionally filtered by year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership level and rewards retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/membership-rewards")
    public ResponseEntity<List<Map<String, Object>>> getMembershipAndRewards(
            @RequestParam(value = "year", required = false) Integer year) {

        // Calling the service method to get the membership level and reward total
        List<Map<String, Object>> report = transactionService.getMembershipAndRewards(year);
        return ResponseEntity.ok(report);
    }
}
