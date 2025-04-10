package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Store;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class StoreDAO {

    private final JdbcTemplate jdbcTemplate;

    public StoreDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Store> storeRowMapper = new RowMapper<Store>() {
        @Override
        public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
            Store store = new Store();
            store.setStoreId(rs.getInt("store_ID"));
            store.setPhone(rs.getString("phone"));
            store.setAddress(rs.getString("address"));
            store.setIsActive(rs.getBoolean("is_active"));
            // manager_ID is optional so we check for null
            int managerId = rs.getInt("manager_ID");
            store.setManagerId(rs.wasNull() ? null : managerId);
            return store;
        }
    };

    // Create a new store record and return the persisted Store
    public Store save(Store store) {
        String sql = "INSERT INTO Stores (phone, address, is_active) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, store.getPhone());
            ps.setString(2, store.getAddress());
            ps.setBoolean(3, store.getIsActive());
            return ps;
        }, keyHolder);

        // Set generated ID to store object
        Number key = keyHolder.getKey();
        if (key != null) {
            store.setStoreId(key.intValue());  // Assuming storeId is an Integer
        }

        return store;
    }

    // Retrieve a store by store_ID
    public Store getStoreById(Integer storeId) {
        String sql = "SELECT * FROM Stores WHERE store_ID = ?";
        return jdbcTemplate.queryForObject(sql, storeRowMapper, storeId);
    }

    // Retrieve all stores
    public List<Store> getAllStores() {
        String sql = "SELECT * FROM Stores";
        return jdbcTemplate.query(sql, storeRowMapper);
    }

    // Update an existing store record
    public int update(Store store) {
        String sql = "UPDATE Stores SET phone = ?, address = ?, is_active = ?, manager_ID = ? WHERE store_ID = ?";
        return jdbcTemplate.update(sql,
                store.getPhone(),
                store.getAddress(),
                store.getIsActive(),
                store.getManagerId(),
                store.getStoreId());
    }

    // Delete a store record
    public int delete(Integer storeId) {
        String sql = "DELETE FROM Stores WHERE store_ID = ?";
        return jdbcTemplate.update(sql, storeId);
    }
    
 // Method to get storeId associated with a given managerId
    public Integer getStoreIdByManagerId(Integer managerId) {
        String sql = "SELECT store_ID FROM Stores WHERE manager_ID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, managerId);
    }
}