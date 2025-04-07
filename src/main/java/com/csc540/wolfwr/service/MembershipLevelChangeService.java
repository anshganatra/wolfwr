package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.MembershipLevelChangeDAO;
import com.csc540.wolfwr.dto.MembershipLevelChangeDTO;
import com.csc540.wolfwr.model.MembershipLevelChange;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipLevelChangeService {
    private final MembershipLevelChangeDAO membershipLevelChangeDAO;
    //private final MemberService memberService; // Used to validate member existence

    public MembershipLevelChangeService(MembershipLevelChangeDAO membershipLevelChangeDAO/*, MemberService memberService*/) {
        this.membershipLevelChangeDAO = membershipLevelChangeDAO;
        // this.memberService = memberService;
    }

    // Create a new member level change with member existence check
    public MembershipLevelChangeDTO createMembershipLevelChange(MembershipLevelChangeDTO membershipLevelChangeDTO) {
        if (membershipLevelChangeDTO.getMemberId() != null) {
            // Check if the store exists
            // if (memberService.getMemberById(membershipLevelChangeDTO.getMemberId()) == null) {
            //     throw new IllegalArgumentException("Store with ID " + membershipLevelChangeDTO.getMemberId() + " does not exist.");
            // }
        }
        MembershipLevelChange membershipLevelChange = new MembershipLevelChange();
        BeanUtils.copyProperties(membershipLevelChangeDTO, membershipLevelChange);
        membershipLevelChangeDAO.save(membershipLevelChange);
        return membershipLevelChangeDTO;
    }

    // Retrieve all membership level changes.
    public List<MembershipLevelChangeDTO> getAllMembershipLevelChanges() {
        List<MembershipLevelChange> membershipLevelChangeList = membershipLevelChangeDAO.getAllMembershipLevelChanges();
        return membershipLevelChangeList.stream().map(membershipLevelChange -> {
            MembershipLevelChangeDTO dto = new MembershipLevelChangeDTO();
            BeanUtils.copyProperties(membershipLevelChange, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing membership level change with member existence check
    public MembershipLevelChangeDTO updateMembershipLevelChange(MembershipLevelChangeDTO membershipLevelChangeDTO,
                                                                Integer memberId, LocalDate date) {
        // if (membershipLevelChangeDTO.getMemberId() != null) {
            // Check if the store exists
            // if (memberService.getStoreById(membershipLevelChangeDTO.getMemberId()) == null) {
            //     throw new IllegalArgumentException("Member with ID " + membershipLevelChangeDTO.getMemberId() + " does not exist.");
            // }
        // }
        MembershipLevelChange membershipLevelChange = new MembershipLevelChange();
        BeanUtils.copyProperties(membershipLevelChangeDTO, membershipLevelChange);
        membershipLevelChangeDAO.update(membershipLevelChange, memberId, date);
        return membershipLevelChangeDTO;
    }

    // Delete a membership level change by member Id and date
    public void deleteMembershipLevelChange(Integer memberId, LocalDate date) {
        membershipLevelChangeDAO.delete(memberId, date);
    }
}
