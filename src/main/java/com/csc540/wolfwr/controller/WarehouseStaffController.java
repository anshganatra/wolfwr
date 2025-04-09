package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.InventoryDTO;
import com.csc540.wolfwr.dto.ShipmentDTO;
import com.csc540.wolfwr.service.InventoryService;
import com.csc540.wolfwr.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Warehouse Staff View", description = "All operations performed by warehouse staff")
@RestController
@RequestMapping("/warehouse-staff")
public class WarehouseStaffController {

    private final InventoryService inventoryService;

    public WarehouseStaffController(InventoryService inventoryService, ShipmentService shipmentService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/process-new-inventory")
    @Operation(summary = "Processes new inventory",
            description = "Updates inventory records with newly created shipments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory processed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public InventoryDTO processNewInventory(@RequestBody Integer shipmentId) {
        return inventoryService.processNewInventory(shipmentId);
    }


}
