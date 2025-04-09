package com.csc540.wolfwr.dao;


import com.csc540.wolfwr.model.Member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            member.setRegistrationStoreId(rs.getInt("registration_store_ID"));

            return member;
        }
    };

    public Integer save(Member member) {
        String sql = "INSERT INTO Members (fname, lname, phone, email, address, dob, doj, membership_level, membership_expiration, registration_staff_ID, active_status, registration_store_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getFname());
            ps.setString(2, member.getLname());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getEmail());
            ps.setString(5, member.getAddress());
            ps.setDate(6, Date.valueOf(member.getDob()));
            ps.setDate(7, Date.valueOf(member.getDoj()));
            ps.setString(8, member.getMemberLevel());
            ps.setDate(9, Date.valueOf(member.getMembershipExpiration()));
            ps.setInt(10, member.getStaffId());
            ps.setBoolean(11, member.isActiveStatus());
            ps.setInt(12, member.getRegistrationStoreId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
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
    public int updateMember(Member member) {
        String sqlQuery = "UPDATE Members SET fname = ?, lname = ?, phone = ?, email = ?, address = ?, dob = ?, doj = ?, " +
                "membership_level = ?, active_status = ?, membership_expiration = ?, registration_staff_ID = ?, registration_store_ID = ? " +
                "WHERE member_ID = ?";
        return jdbcTemplate.update(
                sqlQuery,
                member.getFname(),                    // 1
                member.getLname(),                    // 2
                member.getPhone(),                    // 3
                member.getEmail(),                    // 4
                member.getAddress(),                  // 5
                member.getDob(),                      // 6
                member.getDoj(),                      // 7
                member.getMemberLevel(),              // 8
                member.isActiveStatus(),              // 9
                member.getMembershipExpiration(),     // 10
                member.getStaffId(),                  // 11: registration_staff_ID
                member.getRegistrationStoreId(),      // 12: registration_store_ID
                member.getMemberId()                  // 13: WHERE member_ID = ?
        );
    }

    // delete member details
    // TODO : see if this method actually needs to exist
    public int delete(Integer memberId){
        String sqlQuery = "DELETE FROM Members WHERE member_id = ?";
        return jdbcTemplate.update(sqlQuery, memberId);
    }

    public List<Map<String, Object>> getCustomerGrowth(String reportType, Date startDate, Date endDate, Integer storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        // Build the grouping column based on reportType.
        switch (reportType.toLowerCase()) {
            case "monthly":
                // Group by month: Format as YYYY-MM
                sql.append("DATE_FORMAT(doj, '%Y-%m') AS reportPeriod, ");
                break;
            case "quarterly":
                // Group by quarter: e.g., "2025-Q1"
                sql.append("CONCAT(YEAR(doj), '-Q', QUARTER(doj)) AS reportPeriod, ");
                break;
            case "annually":
                // Group by year: YEAR(doj)
                sql.append("YEAR(doj) AS reportPeriod, ");
                break;
            default:
                throw new IllegalArgumentException("Invalid report type");
        }

        sql.append("registration_store_ID AS storeId, ");
        sql.append("COUNT(member_ID) AS totalGrowth ");
        sql.append("FROM Members ");
        sql.append("WHERE active_status = 1 ");
        sql.append("AND doj BETWEEN ? AND ? ");

        // Build parameters
        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);

        // Optional store filter
        if (storeId != null) {
            sql.append("AND registration_store_ID = ? ");
            params.add(storeId);
        }

        // Group by the period and storeId
        switch (reportType.toLowerCase()) {
            case "monthly":
                sql.append("GROUP BY DATE_FORMAT(doj, '%Y-%m'), registration_store_ID ");
                break;
            case "quarterly":
                sql.append("GROUP BY CONCAT(YEAR(doj), '-Q', QUARTER(doj)), registration_store_ID ");
                break;
            case "annually":
                sql.append("GROUP BY YEAR(doj), registration_store_ID ");
                break;
        }

        sql.append("ORDER BY reportPeriod ASC, storeId ASC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

}
