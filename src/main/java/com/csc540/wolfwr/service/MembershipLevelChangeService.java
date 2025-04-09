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

    public MembershipLevelChangeService(MembershipLevelChangeDAO membershipLevelChangeDAO) {
        this.membershipLevelChangeDAO = membershipLevelChangeDAO;
    }

    // Create a new membership level change record,
    // then fetch the current state of the record from the DB.
    public MembershipLevelChangeDTO createMembershipLevelChange(MembershipLevelChangeDTO dto) {
        // Copy the DTO into a domain model and persist it
        MembershipLevelChange change = new MembershipLevelChange();
        BeanUtils.copyProperties(dto, change);
        membershipLevelChangeDAO.save(change);

        // Now fetch the persisted record based on the memberId and levelChangeDate.
        MembershipLevelChange persistedChange = membershipLevelChangeDAO.getByMemberIdAndDate(dto.getMemberId(), dto.getLevelChangeDate());

        MembershipLevelChangeDTO result = new MembershipLevelChangeDTO();
        BeanUtils.copyProperties(persistedChange, result);
        return result;
    }

    // Retrieve all membership level change records.
    public List<MembershipLevelChangeDTO> getAllMembershipLevelChanges() {
        List<MembershipLevelChange> changes = membershipLevelChangeDAO.getAllMembershipLevelChanges();
        return changes.stream().map(change -> {
            MembershipLevelChangeDTO dto = new MembershipLevelChangeDTO();
            BeanUtils.copyProperties(change, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing membership level change record,
    // then fetch the updated record from the DB.
    public MembershipLevelChangeDTO updateMembershipLevelChange(MembershipLevelChangeDTO dto, Integer memberId, LocalDate date) {
        // Copy the DTO into a domain model and update it
        MembershipLevelChange change = new MembershipLevelChange();
        BeanUtils.copyProperties(dto, change);
        membershipLevelChangeDAO.update(change, memberId, date);

        // Fetch the updated record
        MembershipLevelChange persistedChange = membershipLevelChangeDAO.getByMemberIdAndDate(memberId, date);
        MembershipLevelChangeDTO result = new MembershipLevelChangeDTO();
        BeanUtils.copyProperties(persistedChange, result);
        return result;
    }

    // Delete a membership level change record by member ID and date.
    public void deleteMembershipLevelChange(Integer memberId, LocalDate date) {
        membershipLevelChangeDAO.delete(memberId, date);
    }
}