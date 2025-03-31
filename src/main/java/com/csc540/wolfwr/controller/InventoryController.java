package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.InventoryDTO;
import com.csc540.wolfwr.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventory API", description = "CRUD operations for inventory records")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Create a new inventory record", description = "Creates a new inventory record with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
        return ResponseEntity.ok(createdInventory);
    }

    @Operation(summary = "Get an inventory record by shipment ID", description = "Retrieves an inventory record by its shipment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory record not found")
    })
    @GetMapping("/{shipmentId}")
    public ResponseEntity<InventoryDTO> getInventoryByShipmentId(@PathVariable Integer shipmentId) {
        InventoryDTO inventoryDTO = inventoryService.getInventoryByShipmentId(shipmentId);
        return ResponseEntity.ok(inventoryDTO);
    }

    @Operation(summary = "Get all inventory records", description = "Retrieves all inventory records")
    @ApiResponse(responseCode = "200", description = "Inventory records retrieved successfully")
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        List<InventoryDTO> inventoryList = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryList);
    }

    @Operation(summary = "Update an inventory record", description = "Updates an existing inventory record by shipment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{shipmentId}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Integer shipmentId,
                                                        @Valid @RequestBody InventoryDTO inventoryDTO) {
        inventoryDTO.setShipmentId(shipmentId);
        InventoryDTO updatedInventory = inventoryService.updateInventory(inventoryDTO);
        return ResponseEntity.ok(updatedInventory);
    }

    @Operation(summary = "Delete an inventory record", description = "Deletes an inventory record by its shipment ID")
    @ApiResponse(responseCode = "204", description = "Inventory record deleted successfully")
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer shipmentId) {
        inventoryService.deleteInventory(shipmentId);
        return ResponseEntity.noContent().build();
    }
}