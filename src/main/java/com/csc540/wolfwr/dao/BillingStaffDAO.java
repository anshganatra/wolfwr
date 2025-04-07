package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.BillingStaff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BillingStaffDAO {

    private final JdbcTemplate jdbcTemplate;

    public BillingStaffDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BillingStaff> rowMapper = new RowMapper<>() {
        @Override
        public BillingStaff mapRow(ResultSet rs, int rowNum) throws SQLException {
            BillingStaff billingStaff = new BillingStaff();
            billingStaff.setBillingStaffId(rs.getInt("billing_staff_ID"));
            return billingStaff;
        }
    };

    public int save(BillingStaff billingStaff) {
        String sql = "INSERT INTO BillingStaff (billing_staff_ID) VALUES (?)";
        return jdbcTemplate.update(sql, billingStaff.getBillingStaffId());
    }

    public BillingStaff getById(Integer BillingStaffId) {
        String sql = "SELECT * FROM BillingStaff WHERE billing_staff_ID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, BillingStaffId);
    }

    public List<BillingStaff> getAll() {
        String sql = "SELECT * FROM BillingStaff";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int delete(Integer BillingStaffId) {
        String sql = "DELETE FROM BillingStaff WHERE billing_staff_ID = ?";
        return jdbcTemplate.update(sql, BillingStaffId);
    }
}
