package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.MemberLevelDTO;
import com.csc540.wolfwr.service.MemberLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Member Level API", description = "Operations related to member levels")
@RestController
@RequestMapping("/member-levels")
public class MemberLevelController {

    private final MemberLevelService memberLevelService;

    public MemberLevelController(MemberLevelService memberLevelService) {
        this.memberLevelService = memberLevelService;
    }

    @Operation(summary = "Create a new member level", description = "Creates a new member level with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created member level"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<MemberLevelDTO> createMemberLevel(@Valid @RequestBody MemberLevelDTO memberLevelDTO) {
        MemberLevelDTO createdMemberLevel = memberLevelService.createMemberLevel(memberLevelDTO);
        return ResponseEntity.ok(createdMemberLevel);
    }

    @Operation(summary = "Get a member level by name", description = "Retrieves a member level based on its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved member level"),
            @ApiResponse(responseCode = "404", description = "Member level not found")
    })
    @GetMapping("/{levelName}")
    public ResponseEntity<MemberLevelDTO> getMemberLevelByName(@PathVariable String levelName) {
        MemberLevelDTO memberLevelDTO = memberLevelService.getMemberLevelByName(levelName);
        return ResponseEntity.ok(memberLevelDTO);
    }

    @Operation(summary = "Get all member levels", description = "Retrieves all available member levels")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<MemberLevelDTO>> getAllMemberLevels() {
        List<MemberLevelDTO> memberLevels = memberLevelService.getMemberLevels();
        return ResponseEntity.ok(memberLevels);
    }

    @Operation(summary = "Update an existing member level", description = "Updates the details of an existing member level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated member level"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PutMapping("/{levelName}")
    public ResponseEntity<MemberLevelDTO> updateMemberLevel(@PathVariable String levelName,
                                                            @Valid @RequestBody MemberLevelDTO memberLevelDTO) {
        // Enforce consistency between path and DTO
        memberLevelDTO.setLevelName(levelName);
        MemberLevelDTO updatedMemberLevel = memberLevelService.updateMemberLevel(memberLevelDTO);
        return ResponseEntity.ok(updatedMemberLevel);
    }

    @Operation(summary = "Delete a member level", description = "Deletes the member level identified by its name")
    @ApiResponse(responseCode = "204", description = "Successfully deleted member level")
    @DeleteMapping("/{levelName}")
    public ResponseEntity<Void> deleteMemberLevel(@PathVariable String levelName) {
        memberLevelService.deleteMemberLevel(levelName);
        return ResponseEntity.noContent().build();
    }
}