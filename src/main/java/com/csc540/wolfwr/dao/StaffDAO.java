package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Staff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StaffDAO {

    private final JdbcTemplate jdbcTemplate;

    public StaffDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Staff> staffRowMapper = new RowMapper<Staff>() {
        @Override
        public Staff mapRow(ResultSet rs, int rowNum) throws SQLException {
            Staff staff = new Staff();
            staff.setStaffId(rs.getInt("staff_ID"));
            staff.setFname(rs.getString("fname"));
            staff.setLname(rs.getString("lname"));
            staff.setTitle(rs.getString("title"));
            staff.setAddress(rs.getString("address"));
            staff.setPhone(rs.getString("phone"));
            staff.setEmail(rs.getString("email"));
            // Convert SQL DATE to LocalDate
            staff.setDob(rs.getDate("dob").toLocalDate());
            staff.setDoj(rs.getDate("doj").toLocalDate());
            int storeId = rs.getInt("store_ID");
            staff.setStoreId(rs.wasNull() ? null : storeId);
            return staff;
        }
    };

    // Create - Insert a new Staff record
    public int save(Staff staff) {
        String sql = "INSERT INTO Staff (fname, lname, title, address, phone, email, dob, doj, store_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                staff.getFname(),
                staff.getLname(),
                staff.getTitle(),
                staff.getAddress(),
                staff.getPhone(),
                staff.getEmail(),
                staff.getDob(),
                staff.getDoj(),
                staff.getStoreId());
    }

    // Read - Get a Staff record by staff_ID
    public Staff getStaffById(Integer staffId) {
        String sql = "SELECT * FROM Staff WHERE staff_ID = ?";
        return jdbcTemplate.queryForObject(sql, staffRowMapper, staffId);
    }

    // Read - Get all Staff records
    public List<Staff> getAllStaff() {
        String sql = "SELECT * FROM Staff";
        return jdbcTemplate.query(sql, staffRowMapper);
    }

    // Update - Update an existing Staff record
    public int update(Staff staff) {
        String sql = "UPDATE Staff SET fname = ?, lname = ?, title = ?, address = ?, phone = ?, email = ?, dob = ?, doj = ?, store_ID = ? WHERE staff_ID = ?";
        return jdbcTemplate.update(sql,
                staff.getFname(),
                staff.getLname(),
                staff.getTitle(),
                staff.getAddress(),
                staff.getPhone(),
                staff.getEmail(),
                staff.getDob(),
                staff.getDoj(),
                staff.getStoreId(),
                staff.getStaffId());
    }

    // Delete - Remove a Staff record by staff_ID
    public int delete(Integer staffId) {
        String sql = "DELETE FROM Staff WHERE staff_ID = ?";
        return jdbcTemplate.update(sql, staffId);
    }
    
 // Method to get storeId of a staff member (manager) by their staffId
    public Integer getStoreIdByStaffId(Integer staffId) {
        String sql = "SELECT store_ID FROM Staff WHERE staff_ID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, staffId);  // Returns storeId of the manager
    }

    // Method to assign staff to a store
    public void assignStaffToStore(Integer staffId, Integer storeId) {
        String sql = "UPDATE Staff SET store_ID = ? WHERE staff_ID = ?";
        jdbcTemplate.update(sql, storeId, staffId);  // Update the storeId for the given staffId
    }
}