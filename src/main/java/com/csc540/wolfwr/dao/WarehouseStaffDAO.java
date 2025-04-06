package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.WarehouseStaff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WarehouseStaffDAO {

    private final JdbcTemplate jdbcTemplate;

    public WarehouseStaffDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<WarehouseStaff> warehouseStaffRowMapper = new RowMapper<WarehouseStaff>() {
        @Override
        public WarehouseStaff mapRow(ResultSet rs, int rowNum) throws SQLException {
            WarehouseStaff warehouseStaff = new WarehouseStaff();
            warehouseStaff.setWarehouseStaffId(rs.getInt("warehouse_staff_ID"));
            return warehouseStaff;
        }
    };

    // Create - Insert a new warehouse staff record
    public int save(WarehouseStaff warehouseStaff) {
        String sql = "INSERT INTO WarehouseStaff (warehouse_staff_ID) VALUES (?)";
        return jdbcTemplate.update(sql, warehouseStaff.getWarehouseStaffId());
    }

    // Read - Retrieve a warehouse staff by ID
    public WarehouseStaff getWarehouseStaffById(Integer warehouseStaffId) {
        String sql = "SELECT * FROM WarehouseStaff WHERE warehouse_staff_ID = ?";
        return jdbcTemplate.queryForObject(sql, warehouseStaffRowMapper, warehouseStaffId);
    }

    // Read - Retrieve all warehouse staff
    public List<WarehouseStaff> getAllWarehouseStaff() {
        String sql = "SELECT * FROM WarehouseStaff";
        return jdbcTemplate.query(sql, warehouseStaffRowMapper);
    }

    // Delete - Remove a warehouse staff by ID
    public int delete(Integer warehouseStaffId) {
        String sql = "DELETE FROM WarehouseStaff WHERE warehouse_staff_ID = ?";
        return jdbcTemplate.update(sql, warehouseStaffId);
    }
}
