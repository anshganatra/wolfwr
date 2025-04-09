package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.StaffDTO;
import com.csc540.wolfwr.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin View for Staff API", description = "CRUD operations for staff members")
@RestController
@RequestMapping("/admin/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(summary = "Create a new staff member", description = "Creates a new staff member with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created staff member"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) {
        StaffDTO createdStaff = staffService.createStaff(staffDTO);
        return ResponseEntity.ok(createdStaff);
    }

    @Operation(summary = "Get a staff member by ID", description = "Retrieves a staff member by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved staff member"),
            @ApiResponse(responseCode = "404", description = "Staff member not found")
    })
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Integer staffId) {
        StaffDTO staffDTO = staffService.getStaffById(staffId);
        return ResponseEntity.ok(staffDTO);
    }

    @Operation(summary = "Get all staff members", description = "Retrieves all staff members")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        List<StaffDTO> list = staffService.getAllStaff();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Update an existing staff member", description = "Updates a staff member's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated staff member"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable Integer staffId,
                                                @Valid @RequestBody StaffDTO staffDTO) {
        // Ensure consistency between path and DTO
        staffDTO.setStaffId(staffId);
        StaffDTO updatedStaff = staffService.updateStaff(staffDTO);
        return ResponseEntity.ok(updatedStaff);
    }

    @Operation(summary = "Delete a staff member", description = "Deletes a staff member by their unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted staff member")
    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}