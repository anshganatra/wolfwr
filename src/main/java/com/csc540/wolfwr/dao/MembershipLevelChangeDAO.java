package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.MembershipLevelChange;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MembershipLevelChangeDAO {
    private final JdbcTemplate jdbcTemplate;

    public MembershipLevelChangeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MembershipLevelChange> membershipLevelChangeRowMapper = new RowMapper<MembershipLevelChange>() {
        @Override
        public MembershipLevelChange mapRow(ResultSet rs, int rowNum) throws SQLException {
            MembershipLevelChange memberLevelChange = new MembershipLevelChange();
            memberLevelChange.setMemberId(rs.getInt("member_ID"));
            memberLevelChange.setLevelName(rs.getString("level_name"));
            memberLevelChange.setLevelChangeDate(rs.getDate("date").toLocalDate());
            memberLevelChange.setRegistrationStaffID(rs.getInt("registration_staff_ID"));
            return memberLevelChange;
        }
    };

    // Create - Insert a new MembershipLevelChange record
    public int save(MembershipLevelChange membershipLevelChange) {
        String sql = "INSERT INTO MembershipLevelChange (member_ID, level_name, registration_staff_ID) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                membershipLevelChange.getMemberId(),
                membershipLevelChange.getLevelName(),
                membershipLevelChange.getRegistrationStaffID());
    }

    // Read - Get all MembershipLevelChange records
    public List<MembershipLevelChange> getAllMembershipLevelChanges() {
        String sql = "SELECT * FROM MembershipLevelChange";
        return jdbcTemplate.query(sql, membershipLevelChangeRowMapper);
    }

    // Update - Update an existing MembershipLevelChange record.
    public int update(MembershipLevelChange membershipLevelChange, Integer memberId, LocalDate date) {
        String sql = "UPDATE MembershipLevelChange SET member_ID = ?, level_name = ?, date = ?, registration_staff_ID = ? WHERE member_ID = ? AND date = ?";
        return jdbcTemplate.update(sql,
                membershipLevelChange.getMemberId(),
                membershipLevelChange.getLevelName(),
                membershipLevelChange.getLevelChangeDate(),
                membershipLevelChange.getRegistrationStaffID(),
                memberId,
                date);
    }

    // Delete - Remove a MembershipLevelChange record by member ID and date.
    public int delete(Integer memberId, LocalDate date) {
        String sql = "DELETE FROM MembershipLevelChange WHERE member_ID = ? AND date = ?";
        return jdbcTemplate.update(sql, memberId, date);
    }

    public MembershipLevelChange getByMemberIdAndDate(Integer memberId, LocalDate date) {
        String sql = "SELECT * FROM MembershipLevelChange WHERE member_ID = ? AND date = ?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{memberId, java.sql.Date.valueOf(date)},
                membershipLevelChangeRowMapper);
    }
}
