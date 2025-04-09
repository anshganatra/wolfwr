package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Inventory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // Create: Add inventory to the receiving store
    public int addInventoryToReceivingStore(Integer storeId, Integer shipmentId, Integer productId, BigDecimal marketPrice, Integer productQty) {
        String sql = "INSERT INTO Inventory (store_ID, shipment_ID, product_ID, market_price, product_qty) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, storeId, shipmentId, productId, marketPrice, productQty);
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

    public List<Map<String, Object>> getLowStockInventory(Integer storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT store_ID AS storeId, product_ID AS productId, SUM(product_qty) AS availableQty ")
                .append("FROM Inventory ");

        // Apply filter conditionally if a storeId is provided.
        if (storeId != null) {
            sql.append("WHERE store_ID = ? ");
            sql.append("GROUP BY store_ID, product_ID ");
            sql.append("HAVING SUM(product_qty) < 50");
            return jdbcTemplate.queryForList(sql.toString(), storeId);
        } else {
            sql.append("GROUP BY store_ID, product_ID ");
            sql.append("HAVING SUM(product_qty) < 50");
            return jdbcTemplate.queryForList(sql.toString());
        }
    }

    // Method to reduce the stock from the sending store's inventory
    public int reduceInventoryStock(Integer storeId, Integer shipmentId, Integer qty) {
        String sql = "UPDATE Inventory SET product_qty = product_qty - ? WHERE store_ID = ? AND shipment_ID = ?";
        return jdbcTemplate.update(sql, qty, storeId, shipmentId);
    }

    // Get : Get stock info of all products (within a store)
    public List<Map<String, Object>> getProductStock(Integer storeId, Integer productId) {
        StringBuilder sqlQuery = new StringBuilder("SELECT store_ID, product_ID, SUM(product_qty) AS Current_Stock FROM Inventory ");

        // Build query based on parameters
        boolean hasWhereClause = false;
        if (Objects.nonNull(storeId)) {
            sqlQuery.append("WHERE store_ID = ?");
            hasWhereClause = true;
        }

        if (Objects.nonNull(productId)) {
            // Add the condition for productId, ensuring we append "AND" if storeId is also used
            if (hasWhereClause) {
                sqlQuery.append(" AND product_ID = ?");
            } else {
                sqlQuery.append("WHERE product_ID = ?");
                hasWhereClause = true;
            }
        }

        sqlQuery.append(" GROUP BY store_ID, product_ID");

        // Execute the query with the appropriate parameters
        if (Objects.nonNull(storeId) && Objects.nonNull(productId)) {
            return jdbcTemplate.queryForList(sqlQuery.toString(), storeId, productId);
        } else if (Objects.nonNull(storeId)) {
            return jdbcTemplate.queryForList(sqlQuery.toString(), storeId);
        } else if (Objects.nonNull(productId)) {
            return jdbcTemplate.queryForList(sqlQuery.toString(), productId);
        } else {
            return jdbcTemplate.queryForList(sqlQuery.toString());
        }
    }


    public int updateReturn(Integer quantity, Integer storeId ,Integer shipmentId) {
        String sql = "UPDATE Inventory SET product_qty = product_qty + ? WHERE store_ID = ? AND shipment_ID = ?";
        return jdbcTemplate.update(sql, quantity, storeId, shipmentId);
    }


}
