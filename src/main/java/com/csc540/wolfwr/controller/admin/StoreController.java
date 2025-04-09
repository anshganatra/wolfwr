package com.csc540.wolfwr.controller.admin;
import com.csc540.wolfwr.dto.TransferDTO;
import com.csc540.wolfwr.dto.StoreDTO;
import com.csc540.wolfwr.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Admin View for Store API", description = "CRUD operations for stores")
@RestController
@RequestMapping("/admin/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "Create a new store", description = "Creates a new store with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreDTO storeDTO) {
        StoreDTO createdStore = storeService.createStore(storeDTO);
        return ResponseEntity.ok(createdStore);
    }

    @Operation(summary = "Get a store by ID", description = "Retrieves a store by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Integer storeId) {
        StoreDTO storeDTO = storeService.getStoreDTOById(storeId);
        return ResponseEntity.ok(storeDTO);
    }

    @Operation(summary = "Get all stores", description = "Retrieves all stores")
    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        List<StoreDTO> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @Operation(summary = "Update a store", description = "Updates an existing store with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Integer storeId,
                                                @Valid @RequestBody StoreDTO storeDTO) {
        storeDTO.setStoreId(storeId);
        StoreDTO updatedStore = storeService.updateStore(storeDTO);
        return ResponseEntity.ok(updatedStore);
    }

    @Operation(summary = "Delete a store", description = "Deletes a store by its unique ID")
    @ApiResponse(responseCode = "204", description = "Store deleted successfully")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<String> transferProducts(@RequestBody TransferDTO transferDTO) {
        storeService.transferProducts(transferDTO);
        return ResponseEntity.ok("Transfer completed successfully");
    }
    
    @GetMapping("/inventory-turnover")
    public ResponseEntity<List<Map<String, Object>>> getInventoryTurnover() {
        List<Map<String, Object>> turnoverData = storeService.calculateInventoryTurnover();
        return ResponseEntity.ok(turnoverData);
    }
}