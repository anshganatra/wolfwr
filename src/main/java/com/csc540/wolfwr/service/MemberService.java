package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.MemberDAO;
import com.csc540.wolfwr.dto.MemberDTO;
import com.csc540.wolfwr.model.Member;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberDAO memberDAO;

    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    // create a new membership record
    public MemberDTO createMember(MemberDTO memberDTO){
        Member member = new Member();
        BeanUtils.copyProperties(memberDTO, member);
        memberDAO.save(member);
        return memberDTO;
    }

    // get member by ID
    public MemberDTO getMemberById(Integer memberId){
        Member member = memberDAO.getMemberById(memberId);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(member, memberDTO);
        return memberDTO;
    }

    // get member by ID
    public List<MemberDTO> getMembers(){
        List<Member> members = memberDAO.getMembers();
        return members.stream().map(member -> {
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(member, memberDTO);
            return memberDTO;
        }).toList();
    }

    // update member details
    public MemberDTO updateMemberDetails(MemberDTO memberDTO){
        Member member = new Member();
        BeanUtils.copyProperties(memberDTO, member);
        memberDAO.updateMember(member);
        return memberDTO;
    }

    public int deleteMember(Integer memberId) {
        return memberDAO.delete(memberId);
    }

    private final List<String> validReportTypes = Arrays.asList("monthly", "quarterly", "annually");

    public List<Map<String, Object>> getCustomerGrowth(String reportType, LocalDate startDate, LocalDate endDate, Integer storeId) {
        // Validate reportType
        if (reportType == null || !validReportTypes.contains(reportType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid or missing reportType. Must be one of: monthly, quarterly, annually.");
        }

        // Ensure startDate is provided (the user requested it as required).
        if (startDate == null) {
            throw new IllegalArgumentException("startDate is required.");
        }

        // Default endDate to current date if not provided
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        return memberDAO.getCustomerGrowth(reportType, sqlStartDate, sqlEndDate, storeId);
    }
}
