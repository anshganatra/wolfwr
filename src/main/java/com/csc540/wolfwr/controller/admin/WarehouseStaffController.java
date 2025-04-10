package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.WarehouseStaffDTO;
import com.csc540.wolfwr.service.WarehouseStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin View for Warehouse Staff API", description = "CRUD operations for warehouse staff")
@RestController
@RequestMapping("/admin/warehouse-staff")
public class WarehouseStaffController {
    
    private final WarehouseStaffService warehouseStaffService;

    public WarehouseStaffController(WarehouseStaffService warehouseStaffService) {
        this.warehouseStaffService = warehouseStaffService;
    }

    @Operation(summary = "Create a new warehouse staff", 
               description = "Creates a new warehouse staff record referencing a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse Staff created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<WarehouseStaffDTO> createWarehouseStaff(@Valid @RequestBody WarehouseStaffDTO warehouseStaffDTO) {
        WarehouseStaffDTO createdWarehouseStaff = warehouseStaffService.createWarehouseStaff(warehouseStaffDTO);
        return ResponseEntity.ok(createdWarehouseStaff);
    }

    @Operation(summary = "Get a warehouse staff by ID", 
               description = "Retrieves a warehouse staff by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse staff retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse staff not found")
    })
    @GetMapping("/{warehouseStaffId}")
    public ResponseEntity<WarehouseStaffDTO> getWarehouseStaffById(@PathVariable Integer warehouseStaffId) {
        WarehouseStaffDTO warehouseStaffDTO = warehouseStaffService.getWarehouseStaffById(warehouseStaffId);
        return ResponseEntity.ok(warehouseStaffDTO);
    }

    @Operation(summary = "Get all warehouse staff", description = "Retrieves all warehouse staff records")
    @ApiResponse(responseCode = "200", description = "Warehouse staff retrieved successfully")
    @GetMapping
    public ResponseEntity<List<WarehouseStaffDTO>> getAllWarehouseStaff() {
        List<WarehouseStaffDTO> warehouseStaffs = warehouseStaffService.getAllWarehouseStaff();
        return ResponseEntity.ok(warehouseStaffs);
    }

    @Operation(summary = "Delete a warehouse staff", 
               description = "Deletes a warehouse staff record by its unique ID")
    @ApiResponse(responseCode = "204", description = "Warehouse staff deleted successfully")
    @DeleteMapping("/{warehouseStaffId}")
    public ResponseEntity<Void> deleteWarehouseStaff(@PathVariable Integer warehouseStaffId) {
        warehouseStaffService.deleteWarehouseStaff(warehouseStaffId);
        return ResponseEntity.noContent().build();
    }
}
