package com.csc540.wolfwr.controller.admin;

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
import java.util.Map;

@Tag(name = "Admin View for Inventory API", description = "CRUD operations for inventory records")
@RestController
@RequestMapping("/admin/inventory")
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
        List<Map<String, Object>> lowStockInventory = inventoryService.getProductStock(storeId,productId);
        return ResponseEntity.ok(lowStockInventory);
    }

    @Operation(
            summary = "Get low stock inventory",
            description = "Retrieves inventory groups where the available quantity (the sum of product_qty for each product in a store) is less than 50. Optionally, filter by a specific store ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory summary retrieved successfully")
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockInventory(
            @RequestParam(value = "storeId", required = false) Integer storeId) {
        List<Map<String, Object>> lowStockInventory = inventoryService.getLowStockInventory(storeId);
        return ResponseEntity.ok(lowStockInventory);
    }
}