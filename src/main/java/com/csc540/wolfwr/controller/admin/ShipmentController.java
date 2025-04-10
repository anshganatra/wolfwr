package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.ShipmentDTO;
import com.csc540.wolfwr.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Admin View for Shipment API", description = "CRUD operations for shipments")
@RestController
@RequestMapping("/admin/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Operation(summary = "Create a new shipment", description = "Creates a new shipment with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ShipmentDTO> createShipment(@Valid @RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO createdShipment = shipmentService.createShipment(shipmentDTO);
        return ResponseEntity.ok(createdShipment);
    }

    @Operation(summary = "Get a shipment by ID", description = "Retrieves a shipment by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable Integer shipmentId) {
        ShipmentDTO shipmentDTO = shipmentService.getShipmentById(shipmentId);
        return Objects.isNull(shipmentDTO) ? ResponseEntity.notFound().build() : ResponseEntity.ok(shipmentDTO);
    }

    @Operation(summary = "Get all shipments", description = "Retrieves all shipment records")
    @ApiResponse(responseCode = "200", description = "Shipments retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getAllShipments() {
        List<ShipmentDTO> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @Operation(summary = "Identifies all expired products", description = "Identifies all expired products")
    @ApiResponse(responseCode = "200", description = "Expired products retrieved")
    @GetMapping("/expired-products")
    public ResponseEntity<List<Map<String, Object>>> identifyExpiredProducts() {
        List<Map<String, Object>> shipments = shipmentService.identifyExpiredProducts();
        return ResponseEntity.ok(shipments);
    }

    @Operation(summary = "Update an existing shipment", description = "Updates shipment details by shipment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{shipmentId}")
    public ResponseEntity<ShipmentDTO> updateShipment(@PathVariable Integer shipmentId,
                                                      @Valid @RequestBody ShipmentDTO shipmentDTO) {
        shipmentDTO.setShipmentId(shipmentId);
        ShipmentDTO updatedShipment = shipmentService.updateShipment(shipmentDTO);
        return ResponseEntity.ok(updatedShipment);
    }

    @Operation(summary = "Delete a shipment", description = "Deletes a shipment by its unique ID")
    @ApiResponse(responseCode = "204", description = "Shipment deleted successfully")
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Integer shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all expired products", description = "Get all expired shipments posisbly from a store")
    @ApiResponse(responseCode = "200", description = "Expired products returned successfully")
    @ApiResponse(responseCode = "404", description = "No shipments found")
    @GetMapping("/expired-shipments")
    public ResponseEntity<List<Map<String, Object>>> getExpiredShipments(@RequestParam(name = "storeId", required = false) Integer storeId) {
        List<Map<String, Object>> expiredShipments = shipmentService.getExpiredShipments(storeId);
        return ResponseEntity.ok(expiredShipments);
    }
}