package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.MembershipLevelChangeDTO;
import com.csc540.wolfwr.service.MembershipLevelChangeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "MembershipLevelChange API", description = "CRUD operations for Membership Level Changes")
@RestController
@RequestMapping("/admin/membership-level-change")
public class MembershipLevelChangeController {
    private final MembershipLevelChangeService membershipLevelChangeService;

    public MembershipLevelChangeController(MembershipLevelChangeService membershipLevelChangeService) {
        this.membershipLevelChangeService = membershipLevelChangeService;
    }

    @Operation(summary = "Create a new member level change", 
               description = "Creates a new membership level change with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created membership level change"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<MembershipLevelChangeDTO> createMembershipLevelChange(@Valid @RequestBody MembershipLevelChangeDTO membershipLevelChangeDTO) {
        MembershipLevelChangeDTO createdMembershipLevelChange = membershipLevelChangeService.createMembershipLevelChange(membershipLevelChangeDTO);
        return ResponseEntity.ok(createdMembershipLevelChange);
    }

    @Operation(summary = "Get all membership level changes", description = "Retrieves all membership level changes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<MembershipLevelChangeDTO>> getAllMembershipLevelChanges() {
        List<MembershipLevelChangeDTO> list = membershipLevelChangeService.getAllMembershipLevelChanges();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Update an existing membership level change", description = "Updates a membership level change")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated membership level change"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping
    public ResponseEntity<MembershipLevelChangeDTO> updateStaff(@RequestParam Integer memberId, 
                                                @RequestParam LocalDate date,
                                                @Valid @RequestBody MembershipLevelChangeDTO membershipLevelChangeDTO) {
        MembershipLevelChangeDTO updatedMembershipLevelChange = membershipLevelChangeService.updateMembershipLevelChange(membershipLevelChangeDTO, memberId, date);
        return ResponseEntity.ok(updatedMembershipLevelChange);
    }

    @Operation(summary = "Delete a member level change", description = "Deletes a member level change based on member and product batch ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted member level change")
    @DeleteMapping
    public ResponseEntity<Void> deleteMembershipLevelChange(@RequestParam Integer memberId, @RequestParam LocalDate date) {
        membershipLevelChangeService.deleteMembershipLevelChange(memberId, date);
        return ResponseEntity.noContent().build();
    }
}
