package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.ManagerDTO;
import com.csc540.wolfwr.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manager API", description = "CRUD operations for managers")
@RestController
@RequestMapping("/admin/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(summary = "Create a new manager", description = "Creates a new manager record linking a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ManagerDTO> createManager(@Valid @RequestBody ManagerDTO managerDTO) {
        ManagerDTO createdManager = managerService.createManager(managerDTO);
        return ResponseEntity.ok(createdManager);
    }

    @Operation(summary = "Get all managers", description = "Retrieves all managers")
    @ApiResponse(responseCode = "200", description = "Managers retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        List<ManagerDTO> managers = managerService.getAllManagers();
        return ResponseEntity.ok(managers);
    }

    @Operation(summary = "Delete a manager", description = "Deletes a manager by their unique ID")
    @ApiResponse(responseCode = "204", description = "Manager deleted successfully")
    @DeleteMapping("/{managerId}")
    public ResponseEntity<Void> deleteManager(@PathVariable Integer managerId) {
        managerService.deleteManager(managerId);
        return ResponseEntity.noContent().build();
    }
}