package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.MemberDTO;
import com.csc540.wolfwr.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Member API", description = "Member-related operations")
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "Create a new member", description = "Creates a new membership record given the details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO newMember = memberService.createMember(memberDTO);
        return ResponseEntity.ok(newMember);
    }

    @Operation(summary = "Get info of a specific member", description = "Returns details of a member given their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member details returned succesfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Integer memberId) {
        MemberDTO memberDetails = memberService.getMemberById(memberId);
        return ResponseEntity.ok(memberDetails);
    }

    @Operation(summary = "Get info of all available members", description = "Returns details of all members present in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member details returned succesfully"),
            @ApiResponse(responseCode = "404", description = "No members found")
    })
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getMembers() {
        List<MemberDTO> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "Update details of an existing member", description = "Updates membership details given new values")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/update-member")
    public ResponseEntity<MemberDTO> updateMemberDetails(@RequestBody MemberDTO memberDTO) {
        MemberDTO updatedMember = memberService.updateMemberDetails(memberDTO);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "Delete a member record", description = "Deletes a member record by its unique ID")
    @ApiResponse(responseCode = "204", description = "Member record deleted successfully")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Integer memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
