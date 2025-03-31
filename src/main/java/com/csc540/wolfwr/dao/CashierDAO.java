package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Cashier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CashierDAO {

    private final JdbcTemplate jdbcTemplate;

    public CashierDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cashier> cashierRowMapper = new RowMapper<Cashier>() {
        @Override
        public Cashier mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cashier cashier = new Cashier();
            cashier.setCashierId(rs.getInt("cashier_ID"));
            return cashier;
        }
    };

    // Create - Insert a new cashier record
    public int save(Cashier cashier) {
        String sql = "INSERT INTO Cashiers (cashier_ID) VALUES (?)";
        return jdbcTemplate.update(sql, cashier.getCashierId());
    }

    // Read - Retrieve a cashier by ID
    public Cashier getCashierById(Integer cashierId) {
        String sql = "SELECT * FROM Cashiers WHERE cashier_ID = ?";
        return jdbcTemplate.queryForObject(sql, cashierRowMapper, cashierId);
    }

    // Read - Retrieve all cashiers
    public List<Cashier> getAllCashiers() {
        String sql = "SELECT * FROM Cashiers";
        return jdbcTemplate.query(sql, cashierRowMapper);
    }

    // Delete - Remove a cashier by ID
    public int delete(Integer cashierId) {
        String sql = "DELETE FROM Cashiers WHERE cashier_ID = ?";
        return jdbcTemplate.update(sql, cashierId);
    }

    // Update is not typical here because there are no additional fields beyond the ID.
}