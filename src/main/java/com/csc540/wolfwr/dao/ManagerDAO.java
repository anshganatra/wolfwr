package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Manager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ManagerDAO {

    private final JdbcTemplate jdbcTemplate;

    public ManagerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Manager> managerRowMapper = new RowMapper<Manager>() {
        @Override
        public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
            Manager manager = new Manager();
            manager.setManagerId(rs.getInt("manager_ID"));
            return manager;
        }
    };

    // Create: Insert a new manager record. Since the table has a single column,
    // we simply insert the manager_ID.
    public int save(Manager manager) {
        String sql = "INSERT INTO Managers (manager_ID) VALUES (?)";
        return jdbcTemplate.update(sql, manager.getManagerId());
    }

    // Read: Retrieve all managers
    public List<Manager> getAllManagers() {
        String sql = "SELECT * FROM Managers";
        return jdbcTemplate.query(sql, managerRowMapper);
    }

    // Delete: Remove a manager by ID
    public int delete(Integer managerId) {
        String sql = "DELETE FROM Managers WHERE manager_ID = ?";
        return jdbcTemplate.update(sql, managerId);
    }

    // Read: Retrieve manager by ID
    public Manager getManagerById(Integer managerId) {
        String sql = "SELECT * FROM Managers WHERE manager_id = ?";
        return jdbcTemplate.queryForObject(sql, managerRowMapper, managerId);
    }

    // Update: Not implemented because there is no additional field to update.
}