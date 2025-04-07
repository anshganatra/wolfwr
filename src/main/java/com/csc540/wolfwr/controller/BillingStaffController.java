package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.BillingStaffDTO;
import com.csc540.wolfwr.service.BillingStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Billing Staff API", description = "CRUD operations for billing staff")
@RestController
@RequestMapping("/billing-staff")
public class BillingStaffController {

    private final BillingStaffService billingStaffService;

    public BillingStaffController(BillingStaffService billingStaffService) {
        this.billingStaffService = billingStaffService;
    }

    @Operation(summary = "Add a new billing staff entry")
    @PostMapping
    public ResponseEntity<BillingStaffDTO> create(@Valid @RequestBody BillingStaffDTO dto) {
        return ResponseEntity.ok(billingStaffService.create(dto));
    }

    @Operation(summary = "Get billing staff by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BillingStaffDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(billingStaffService.getById(id));
    }

    @Operation(summary = "Get all billing staff")
    @GetMapping
    public ResponseEntity<List<BillingStaffDTO>> getAll() {
        return ResponseEntity.ok(billingStaffService.getAll());
    }

    @Operation(summary = "Delete a billing staff entry")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        billingStaffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
