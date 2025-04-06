package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.RegistrationStaff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RegistrationStaffDAO {

    private final JdbcTemplate jdbcTemplate;

    public RegistrationStaffDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RegistrationStaff> rowMapper = new RowMapper<>() {
        @Override
        public RegistrationStaff mapRow(ResultSet rs, int rowNum) throws SQLException {
            RegistrationStaff staff = new RegistrationStaff();
            staff.setRegistrationStaffId(rs.getInt("registration_staff_ID"));
            return staff;
        }
    };

    public int save(RegistrationStaff staff) {
        String sql = "INSERT INTO RegistrationStaff (registration_staff_ID) VALUES (?)";
        return jdbcTemplate.update(sql, staff.getRegistrationStaffId());
    }

    public RegistrationStaff getById(Integer staffId) {
        String sql = "SELECT * FROM RegistrationStaff WHERE registration_staff_ID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, staffId);
    }

    public List<RegistrationStaff> getAll() {
        String sql = "SELECT * FROM RegistrationStaff";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int delete(Integer staffId) {
        String sql = "DELETE FROM RegistrationStaff WHERE registration_staff_ID = ?";
        return jdbcTemplate.update(sql, staffId);
    }
}
