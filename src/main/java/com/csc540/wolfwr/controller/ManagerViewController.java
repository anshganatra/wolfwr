package com.csc540.wolfwr.controller.manager;

import com.csc540.wolfwr.service.TransactionService;
import com.csc540.wolfwr.dto.DiscountDTO;
import com.csc540.wolfwr.service.DiscountService;
import com.csc540.wolfwr.service.InventoryService;
import com.csc540.wolfwr.service.ReportService;
import com.csc540.wolfwr.service.StaffService;
import com.csc540.wolfwr.service.StoreService;

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

@Tag(name = "Manager View API", description = "Generate and manage reports and data for managers")
@RestController
@RequestMapping("/manager")
public class ManagerViewController {

    private final TransactionService transactionService;
    private final InventoryService inventoryService;
    private final ReportService reportService;
    private final StoreService storeService;
    private final StaffService staffService;
    private final DiscountService discountService;
    

    public ManagerViewController(TransactionService transactionService, 
                                 InventoryService inventoryService, 
                                 ReportService reportService, 
                                 StaffService staffService,
                                 StoreService storeService,
                                 DiscountService discountService) {
        this.transactionService = transactionService;
        this.inventoryService = inventoryService;
        this.reportService = reportService;
        this.staffService = staffService;
        this.storeService = storeService;
        this.discountService = discountService;

    }

    @Operation(
            summary = "View Total Sales Report",
            description = "Returns total sales and sales growth for a given time period."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/sales-report")
    public ResponseEntity<List<Map<String, Object>>> getTotalSalesReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "storeId", required = false) Integer storeId,
            @RequestParam("previousPeriodStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousPeriodStartDate,
            @RequestParam("previousPeriodEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousPeriodEndDate) {

        // Call the service method with all required parameters
        List<Map<String, Object>> report = transactionService.generateSalesGrowthReport(
            startDate, endDate, previousPeriodStartDate, previousPeriodEndDate, storeId);
        
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "View Customer Activity Report",
            description = "Returns total purchase amount and number of transactions for a given customer during a specified date range."
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
            summary = "View Product Inventory Report",
            description = "Returns the current stock and status of products in a given store."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/product-inventory")
    public ResponseEntity<List<Map<String, Object>>> getProductInventory(
            @RequestParam("storeId") Integer storeId,
            @RequestParam("productId") Integer productId) { // Added productId as parameter

        // Calling the service method to generate the product inventory report
        List<Map<String, Object>> report = inventoryService.getProductInventory(storeId, productId); // Pass both storeId and productId
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "View Membership and Rewards Report",
            description = "Returns membership level and total rewards for each customer, optionally filtered by year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership level and rewards report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/membership-rewards")
    public ResponseEntity<List<Map<String, Object>>> getMembershipAndRewards(
            @RequestParam(value = "year", required = false) Integer year) {

        // Calling the service method to get the membership level and rewards
        List<Map<String, Object>> report = reportService.getMembershipAndRewards(year);
        return ResponseEntity.ok(report);
    }
    
    @Operation(
            summary = "Assign Staff to Manager's Store",
            description = "Allows a manager to assign a staff member to their store by providing their staff ID and manager ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff assigned to the manager's store successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @PostMapping("/onboard-staff-to-store")
    public ResponseEntity<String> assignStaffToStore(
            @RequestParam("managerId") Integer managerId,
            @RequestParam("staffId") Integer staffId) {

        try {
            // Call the service method to assign the staff to the manager's store
            staffService.assignStaffToManagerStore(managerId, staffId);
            return ResponseEntity.ok("Staff successfully assigned to the manager's store.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    // TODO ADD EXCEPTION HANDLING
    @Operation(summary = "Create a new discount")
    @PostMapping("/create-discount")
    public ResponseEntity<DiscountDTO> create(@RequestParam Integer managerId, @RequestBody DiscountDTO dto) {
        // Step 1: Get the storeId for the given managerId
        Integer storeId = storeService.getStoreIdByManagerId(managerId);

        if (storeId == null) {
            return ResponseEntity.status(403).body(null); // Forbidden if the manager doesn't exist or has no store
        }

        // Step 2: Check if the shipmentId in the request belongs to the same store as the manager
        if (!storeService.isShipmentBelongsToStore(dto.getShipmentId(), storeId)) {
            return ResponseEntity.status(403).body(null); // Forbidden if the shipment does not belong to the store
        }

        // Step 3: Proceed with creating the discount if the shipment belongs to the manager's store
        DiscountDTO createdDiscount = discountService.create(dto);
        return ResponseEntity.ok(createdDiscount);
    }

}
