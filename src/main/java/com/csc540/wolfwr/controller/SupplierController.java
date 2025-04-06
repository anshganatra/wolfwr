package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.SupplierDTO;
import com.csc540.wolfwr.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Supplier API", description = "CRUD operations for suppliers")
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Create a new supplier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO created = supplierService.createSupplier(supplierDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get supplier by ID")
    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }

    @Operation(summary = "Get all suppliers")
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @Operation(summary = "Update a supplier")
    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Integer supplierId,
                                                      @Valid @RequestBody SupplierDTO supplierDTO) {
        supplierDTO.setSupplierId(supplierId);
        return ResponseEntity.ok(supplierService.updateSupplier(supplierDTO));
    }

    @Operation(summary = "Delete a supplier")
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }
}