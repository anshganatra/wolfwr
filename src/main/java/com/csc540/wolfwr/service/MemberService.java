package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.MemberDAO;
import com.csc540.wolfwr.dto.MemberDTO;
import com.csc540.wolfwr.model.Member;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
