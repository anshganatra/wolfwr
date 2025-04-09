package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.BillingStaffDTO;
import com.csc540.wolfwr.service.BillingStaffService;
import com.csc540.wolfwr.service.ShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Billing Staff API", description = "CRUD operations for billing staff")
@RestController
@RequestMapping("/admin/billing-staff")
public class BillingStaffController {

    private final BillingStaffService billingStaffService;
    private final ShipmentService shipmentService;

    public BillingStaffController(BillingStaffService billingStaffService, ShipmentService shipmentService) {
        this.billingStaffService = billingStaffService;
        this.shipmentService = shipmentService;
    }

    @Operation(summary = "Add a new billing staff entry")
    @PostMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Billing Staff created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<BillingStaffDTO> create(@Valid @RequestBody BillingStaffDTO dto) {
        return ResponseEntity.ok(billingStaffService.create(dto));
    }

    @Operation(summary = "Get billing staff by ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "Billing Staff with given ID retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<BillingStaffDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(billingStaffService.getById(id));
    }

    @Operation(summary = "Get all billing staff")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Billing Staff retrieved successfully")
    public ResponseEntity<List<BillingStaffDTO>> getAll() {
        return ResponseEntity.ok(billingStaffService.getAll());
    }

    @Operation(summary = "Delete a billing staff entry")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "Billing Staff with given ID deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        billingStaffService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Generate Supplier Bill
    @Operation(
        summary = "Get itemized bills for suppliers",
        description = "Retrieves an itemized bill based on optional supplierId, storeId, and shipmentDate parameters. If shipmentDate is not provided, the current date is used."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itemized bill retrieved successfully")
    })
    @GetMapping("/itemized-bill")
    public ResponseEntity<List<Map<String, Object>>> getItemizedBill(
            @RequestParam(value = "supplierId", required = false) Integer supplierId,
            @RequestParam(value = "storeId", required = false) Integer storeId,
            @RequestParam(value = "shipmentDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shipmentDate) {

        List<Map<String, Object>> itemizedBill = shipmentService.getItemizedBill(supplierId, storeId, shipmentDate);
        return ResponseEntity.ok(itemizedBill);
    }

    // Billing Reports

    // Reward Handling
}
