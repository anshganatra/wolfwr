package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.CashierDTO;
import com.csc540.wolfwr.service.CashierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin View for Cashier API", description = "CRUD operations for cashiers")
@RestController
@RequestMapping("/admin/cashiers")
public class CashierController {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @Operation(summary = "Create a new cashier", description = "Creates a new cashier record referencing a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cashier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CashierDTO> createCashier(@Valid @RequestBody CashierDTO cashierDTO) {
        CashierDTO createdCashier = cashierService.createCashier(cashierDTO);
        return ResponseEntity.ok(createdCashier);
    }

    @Operation(summary = "Get a cashier by ID", description = "Retrieves a cashier by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cashier retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cashier not found")
    })
    @GetMapping("/{cashierId}")
    public ResponseEntity<CashierDTO> getCashierById(@PathVariable Integer cashierId) {
        CashierDTO cashierDTO = cashierService.getCashierById(cashierId);
        return ResponseEntity.ok(cashierDTO);
    }

    @Operation(summary = "Get all cashiers", description = "Retrieves all cashier records")
    @ApiResponse(responseCode = "200", description = "Cashiers retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CashierDTO>> getAllCashiers() {
        List<CashierDTO> cashiers = cashierService.getAllCashiers();
        return ResponseEntity.ok(cashiers);
    }

    @Operation(summary = "Delete a cashier", description = "Deletes a cashier record by its unique ID")
    @ApiResponse(responseCode = "204", description = "Cashier deleted successfully")
    @DeleteMapping("/{cashierId}")
    public ResponseEntity<Void> deleteCashier(@PathVariable Integer cashierId) {
        cashierService.deleteCashier(cashierId);
        return ResponseEntity.noContent().build();
    }
}