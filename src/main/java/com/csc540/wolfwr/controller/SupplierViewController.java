package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.ShipmentDTO;
import com.csc540.wolfwr.service.ProductService;
import com.csc540.wolfwr.service.ShipmentService;
import com.csc540.wolfwr.service.StoreService;
import com.csc540.wolfwr.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@Tag(name = "Supplier View", description = "All operations performed by suppliers")
@RestController
@RequestMapping("/supplier")
public class SupplierViewController {

    private final ShipmentService shipmentService;
    private final SupplierService supplierService;
    private final StoreService storeService;
    private final ProductService productService;

    public SupplierViewController(ShipmentService shipmentService, SupplierService supplierService, StoreService storeService, ProductService productService) {
        this.shipmentService = shipmentService;
        this.supplierService = supplierService;
        this.storeService = storeService;
        this.productService = productService;
    }

    private void validateRequest(ShipmentDTO shipmentDTO) {
        // check if the supplier ID is valid
        try {
            supplierService.getSupplierById(shipmentDTO.getSupplierId());
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("Supplier ID is invalid");
        }
        try {
            productService.getProductById(shipmentDTO.getProductId());
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("Product ID is invalid");
        }
        if (Objects.nonNull(shipmentDTO.getExpDate()) && shipmentDTO.getProductionDate().isAfter(shipmentDTO.getExpDate())) {
            throw new IllegalArgumentException("Production date must be before expiration date");
        }
    }

    @PostMapping("/create-shipment")
    public ResponseEntity<Integer> createNewShipment(@RequestBody ShipmentDTO shipmentDTO) {
        validateRequest(shipmentDTO);
        ShipmentDTO createdShipment = shipmentService.createShipment(shipmentDTO);
        return ResponseEntity.ok(createdShipment.getShipmentId());
    }

    // Exception handler for IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Return 400 BadRequest with exception message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
