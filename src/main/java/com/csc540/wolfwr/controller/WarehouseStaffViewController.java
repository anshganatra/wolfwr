package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.InventoryDTO;
import com.csc540.wolfwr.dto.ReturnItemDTO;
import com.csc540.wolfwr.dto.TransferDTO;
import com.csc540.wolfwr.service.InventoryService;
import com.csc540.wolfwr.service.ShipmentService;
import com.csc540.wolfwr.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@Tag(name = "Warehouse Staff View", description = "All operations performed by warehouse staff")
@RestController
@RequestMapping("/warehouse-staff")
public class WarehouseStaffViewController {

    private final InventoryService inventoryService;
    private final StoreService storeService;

    public WarehouseStaffViewController(InventoryService inventoryService, ShipmentService shipmentService, StoreService storeService) {
        this.inventoryService = inventoryService;
        this.storeService = storeService;
    }

    @PostMapping("/process-new-inventory")
    @Operation(summary = "Processes new inventory",
            description = "Updates inventory records with newly created shipments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory processed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<InventoryDTO> processNewInventory(@RequestParam(name = "shipmentId") Integer shipmentId, @RequestParam(name = "marketPrice") BigDecimal marketPrice) {
        InventoryDTO response = inventoryService.processNewInventory(shipmentId, marketPrice);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process-return")
    @Operation(summary = "Processes item return",
            description = "Updates inventory records accordingly using return request info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item returned successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<Object> processItemReturn(@RequestBody ReturnItemDTO returnItemDTO) {
        inventoryService.returnItem(returnItemDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer-products")
    @Operation(summary = "Transfers products across stores",
            description = "Updates inventory and shipment records for both stores accordingly given info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipments transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public void transferItems(@RequestBody TransferDTO transferDTO) {
        storeService.transferProductsAtomic(transferDTO);
    }




}
