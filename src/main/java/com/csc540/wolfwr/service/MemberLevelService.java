package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.MemberLevelDAO;
import com.csc540.wolfwr.dto.MemberLevelDTO;
import com.csc540.wolfwr.model.MemberLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberLevelService {
    private final MemberLevelDAO memberLevelDAO;

    public MemberLevelService(MemberLevelDAO memberLevelDAO) {
        this.memberLevelDAO = memberLevelDAO;
    }

    //Retrieve all MemberLevels
    public List<MemberLevelDTO> getMemberLevels() {
        List<MemberLevel> memberLevels = memberLevelDAO.getMemberLevels();
        List<MemberLevelDTO> memberLevelDTOs = new ArrayList<>();
        for (MemberLevel memberLevel : memberLevels) {
            MemberLevelDTO memberLevelDTO = new MemberLevelDTO();
            BeanUtils.copyProperties(memberLevel, memberLevelDTO);
            memberLevelDTOs.add(memberLevelDTO);
        }
        return memberLevelDTOs;
    }

    // Create a new MemberLevel
    public MemberLevelDTO createMemberLevel(MemberLevelDTO memberLevelDTO) {
        MemberLevel memberLevel = new MemberLevel();
        BeanUtils.copyProperties(memberLevelDTO, memberLevel);
        memberLevelDAO.save(memberLevel);
        // Optionally, you could retrieve the saved entity and return a mapped DTO.
        return memberLevelDTO;
    }

    // Retrieve a MemberLevel by its name
    public MemberLevelDTO getMemberLevelByName(String levelName) {
        MemberLevel memberLevel = memberLevelDAO.getMemberLevelByName(levelName);
        MemberLevelDTO memberLevelDTO = new MemberLevelDTO();
        BeanUtils.copyProperties(memberLevel, memberLevelDTO);
        return memberLevelDTO;
    }

    // Update an existing MemberLevel
    public MemberLevelDTO updateMemberLevel(MemberLevelDTO memberLevelDTO) {
        // Map DTO to model object
        MemberLevel memberLevel = new MemberLevel();
        BeanUtils.copyProperties(memberLevelDTO, memberLevel);
        memberLevelDAO.update(memberLevel);
        return memberLevelDTO;
    }

    // Delete a MemberLevel by its name
    public void deleteMemberLevel(String levelName) {
        memberLevelDAO.delete(levelName);
    }
}
