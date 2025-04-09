package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.ShipmentDTO;
import com.csc540.wolfwr.service.ShipmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Supplier View", description = "All operations performed by suppliers")
@RestController
@RequestMapping("/supplier")
public class SupplierViewController {

    private final ShipmentService shipmentService;

    public SupplierViewController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create-shipment")
    public ResponseEntity<Integer> createNewShipment(@RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO createdShipment = shipmentService.createShipment(shipmentDTO);
        return ResponseEntity.ok(createdShipment.getShipmentId());
    }
}
