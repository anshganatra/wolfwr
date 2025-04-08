package com.csc540.wolfwr.dao;


import com.csc540.wolfwr.model.Member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class MemberDAO {

    private final JdbcTemplate jdbcTemplate;

    public MemberDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = new RowMapper<Member>() {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            Member member = new Member();
            member.setMemberId(rs.getInt("member_ID"));
            member.setFname(rs.getString("fname"));
            member.setLname(rs.getString("lname"));
            member.setPhone(rs.getString("phone"));
            member.setEmail(rs.getString("email"));
            member.setAddress(rs.getString("address"));
            member.setDob(rs.getDate("dob").toLocalDate());
            member.setDoj(rs.getDate("doj").toLocalDate());
            member.setMemberLevel(Objects.isNull(rs.getString("membership_level")) ? "" : rs.getString("membership_level"));
            member.setActiveStatus(rs.getBoolean("active_status"));
            member.setMembershipExpiration(rs.getDate("membership_expiration").toLocalDate());
            // TODO: check if this should be nullable or not
            member.setStaffId(rs.getInt("registration_staff_ID"));
            return member;
        }
    };

    public int save(Member member) {
        String sqlStatement = "INSERT INTO Members (fname, lname, phone, email, address, dob, doj, membership_level, membership_expiration, registration_staff_ID, active_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        return jdbcTemplate.update(sqlStatement, member.getFname(), member.getLname(), member.getPhone(), member.getEmail(), member.getAddress(), member.getDob(), member.getDoj(), member.getMemberLevel(), member.getMembershipExpiration(), member.getStaffId(), member.isActiveStatus());
    }

    // get member by ID
    public Member getMemberById(Integer memberId) {
        String sqlQuery = "SELECT * FROM Members WHERE member_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, memberRowMapper, memberId);
    }

    // get all members
    public List<Member> getMembers(){
        String sqlQuery = "SELECT * FROM Members";
        return jdbcTemplate.query(sqlQuery, memberRowMapper);
    }

    // update member details by member ID
    public int updateMember(Member member){
        String sqlQuery = "UPDATE Members SET fname = ?, lname = ?, phone = ?, email = ?, address = ?, dob = ?, doj = ?, " +
                "membership_level = ?, active_status = ?, membership_expiration = ?, registration_staff_ID = ? " +
                "WHERE member_ID = ?";
        return jdbcTemplate.update(sqlQuery, member.getFname(), member.getLname(), member.getPhone(), member.getEmail(),
                member.getAddress(), member.getDob(), member.getDoj(), member.getMemberLevel(), member.isActiveStatus(),
                member.getMembershipExpiration(), member.getStaffId(), member.getMemberId());
    }

    // delete member details
    // TODO : see if this method actually needs to exist
    public int delete(Integer memberId){
        String sqlQuery = "DELETE FROM Members WHERE member_id = ?";
        return jdbcTemplate.update(sqlQuery, memberId);
    }

}
