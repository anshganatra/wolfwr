package com.csc540.wolfwr.controller.admin;
import com.csc540.wolfwr.dto.BillingStaffDTO;
import com.csc540.wolfwr.service.BillingStaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin View for Billing Staff API", description = "CRUD operations for billing staff")
@RestController
@RequestMapping("/admin/billing-staff")
public class BillingStaffController {

    private final BillingStaffService billingStaffService;

    public BillingStaffController(BillingStaffService billingStaffService) {
        this.billingStaffService = billingStaffService;
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
}
