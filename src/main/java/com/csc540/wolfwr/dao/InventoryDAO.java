package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Inventory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public InventoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Inventory> inventoryRowMapper = new RowMapper<Inventory>() {
        @Override
        public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
            Inventory inventory = new Inventory();
            inventory.setStoreId(rs.getInt("store_ID"));
            inventory.setShipmentId(rs.getInt("shipment_ID"));
            inventory.setProductId(rs.getInt("product_ID"));
            inventory.setMarketPrice(rs.getBigDecimal("market_price"));
            inventory.setProductQty(rs.getInt("product_qty"));
            return inventory;
        }
    };

    // Create: Insert a new Inventory record
    public int save(Inventory inventory) {
        String sql = "INSERT INTO Inventory (store_ID, shipment_ID, product_ID, market_price, product_qty) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                inventory.getStoreId(),
                inventory.getShipmentId(),
                inventory.getProductId(),
                inventory.getMarketPrice(),
                inventory.getProductQty());
    }

    // Read: Retrieve an inventory record by shipment_ID (primary key)
    public Inventory getInventoryByShipmentId(Integer shipmentId) {
        String sql = "SELECT * FROM Inventory WHERE shipment_ID = ?";
        return jdbcTemplate.queryForObject(sql, inventoryRowMapper, shipmentId);
    }

    // Read: Retrieve all inventory records
    public List<Inventory> getAllInventory() {
        String sql = "SELECT * FROM Inventory";
        return jdbcTemplate.query(sql, inventoryRowMapper);
    }

    // Update: Update an existing Inventory record by shipment_ID
    public int update(Inventory inventory) {
        String sql = "UPDATE Inventory SET store_ID = ?, product_ID = ?, market_price = ?, product_qty = ? " +
                "WHERE shipment_ID = ?";
        return jdbcTemplate.update(sql,
                inventory.getStoreId(),
                inventory.getProductId(),
                inventory.getMarketPrice(),
                inventory.getProductQty(),
                inventory.getShipmentId());
    }

    // Delete: Remove an Inventory record by shipment_ID
    public int delete(Integer shipmentId) {
        String sql = "DELETE FROM Inventory WHERE shipment_ID = ?";
        return jdbcTemplate.update(sql, shipmentId);
    }
}