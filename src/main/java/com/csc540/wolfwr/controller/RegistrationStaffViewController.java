package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.MemberDTO;
import com.csc540.wolfwr.dto.MemberLevelDTO;
import com.csc540.wolfwr.dto.MembershipLevelChangeDTO;
import com.csc540.wolfwr.dto.RegistrationStaffDTO;
import com.csc540.wolfwr.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Registration Staff View API", description = "Registration Staff View of the system")
@RestController
@RequestMapping("/registration-staff")
public class RegistrationStaffViewController {
    // 1. Sign up a new member
    // 2. Upgrade a membership level
    // 3. Cancel a membership

    private final MemberService memberService;
    private final RegistrationStaffService registrationStaffService;
    private final StaffService staffService;
    private final MembershipLevelChangeService membershipLevelChangeService;
    private final MemberLevelService memberLevelService;

    public RegistrationStaffViewController(MemberService memberService, RegistrationStaffService registrationStaffService, StaffService staffService, MembershipLevelChangeService membershipLevelChangeService, MemberLevelService memberLevelService) {
        this.memberService = memberService;
        this.registrationStaffService = registrationStaffService;
        this.staffService = staffService;
        this.membershipLevelChangeService = membershipLevelChangeService;
        this.memberLevelService = memberLevelService;
    }

    @PostMapping("/member-sign-up")
    public ResponseEntity<MemberDTO> signUpNewMember(@RequestBody @Valid MemberDTO memberDTO, @RequestParam Integer registrationStaffId, @RequestParam String membershipLevel) {
        //Ensure that the staff helping sign up is a valid registration staff
        RegistrationStaffDTO regStaffDTO = registrationStaffService.getById(registrationStaffId);
        if (regStaffDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid registration staff ID");
        }
        //Ensure that the provided membershiplevel is valid
        if(memberLevelService.getMemberLevelByName(membershipLevel) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid membership level");
        }

        Integer registrationStoreId = staffService.getStaffById(regStaffDTO.getRegistrationStaffId()).getStoreId();

        MemberLevelDTO memberLevelDTO = memberLevelService.getMemberLevelByName("Gold");

        memberDTO.setMemberId(null);
        memberDTO.setMemberLevel(membershipLevel);
        memberDTO.setActiveStatus(true);
        memberDTO.setRegistrationStoreId(registrationStoreId);
        memberDTO.setStaffId(regStaffDTO.getRegistrationStaffId());

        if (memberDTO.getDoj() == null) {
            memberDTO.setDoj(LocalDate.now());
        }

        if (memberDTO.getMembershipExpiration() == null) {
            memberDTO.setMembershipExpiration(memberDTO.getDoj().plusYears(1));
        }

        MemberDTO savedMemberDTO = memberService.createMember(memberDTO);

        MembershipLevelChangeDTO membershipLevelChangeDTO = new MembershipLevelChangeDTO();
        membershipLevelChangeDTO.setMemberId(savedMemberDTO.getMemberId());
        membershipLevelChangeDTO.setLevelName(membershipLevel);
        membershipLevelChangeDTO.setLevelChangeDate(LocalDate.now());
        membershipLevelChangeDTO.setRegistrationStaffID(registrationStaffId);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMemberDTO);
    }

    @PostMapping("/change-member-level")
    public ResponseEntity<MembershipLevelChangeDTO> changeMemberLevel(@RequestParam Integer memberId, @RequestParam String membershipLevel, @RequestParam Integer registrationStaffId) {
        //Ensure that the staff helping sign up is a valid registration staff
        if (registrationStaffService.getById(registrationStaffId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid registration staff ID");
        }
        //Ensure that the provided membershiplevel is valid
        if(memberLevelService.getMemberLevelByName(membershipLevel) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid membership level");
        }
        //Ensure that the provided memberId is valid
        if(memberService.getMemberById(memberId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid member id");
        }

        MembershipLevelChangeDTO membershipLevelChangeDTO = new MembershipLevelChangeDTO();
        membershipLevelChangeDTO.setMemberId(memberId);
        membershipLevelChangeDTO.setLevelName(membershipLevel);
        membershipLevelChangeDTO.setLevelChangeDate(LocalDate.now());
        membershipLevelChangeDTO.setRegistrationStaffID(registrationStaffId);

        MembershipLevelChangeDTO persistedMembershipLevelChangeDTO = membershipLevelChangeService.createMembershipLevelChange(membershipLevelChangeDTO);

        MemberDTO memberDTO = memberService.getMemberById(memberId);
        memberDTO.setMemberLevel(persistedMembershipLevelChangeDTO.getLevelName());
        memberDTO.setMembershipExpiration(LocalDate.now().plusYears(1));
        memberDTO.setActiveStatus(true);
        memberService.updateMemberDetails(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(persistedMembershipLevelChangeDTO);
    }

    @PutMapping("/cancel-membership")
    public ResponseEntity<MemberDTO> cancelMembership(
            @RequestParam Integer memberId,
            @RequestParam Integer registrationStaffId) {

        // Verify that the registration staff is valid
        RegistrationStaffDTO regStaffDTO = registrationStaffService.getById(registrationStaffId);
        if (regStaffDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid registration staff ID");
        }

        // Retrieve the member by its ID. If the member doesn't exist, throw an error.
        MemberDTO memberDTO = memberService.getMemberById(memberId);
        if (memberDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid member ID");
        }

        // Set cancellation fields: mark membership as inactive and set the expiration to today.
        memberDTO.setActiveStatus(false);
        memberDTO.setMembershipExpiration(LocalDate.now());

        // Update the member details in the database.
        MemberDTO updatedMemberDTO = memberService.updateMemberDetails(memberDTO);

        return ResponseEntity.ok(updatedMemberDTO);
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
}
