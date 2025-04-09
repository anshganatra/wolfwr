package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Shipment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;  // Add this import
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;  // Add this import
import java.sql.Statement;  // Add this import
import java.sql.Date;  // Add this import
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Objects;

@Repository
public class ShipmentDAO {

    private final JdbcTemplate jdbcTemplate;

    public ShipmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Shipment> shipmentRowMapper = new RowMapper<Shipment>() {
        @Override
        public Shipment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Shipment shipment = new Shipment();
            shipment.setShipmentId(rs.getInt("shipment_ID"));
            shipment.setSupplierId(rs.getInt("supplier_ID"));
            shipment.setProductId(rs.getInt("product_ID"));
            shipment.setStoreId(rs.getInt("store_ID"));
            shipment.setBuyPrice(rs.getBigDecimal("buy_price"));
            shipment.setProductionDate(rs.getDate("production_date").toLocalDate());
            shipment.setShipmentDate(rs.getDate("shipment_date").toLocalDate());
            // exp_date might be null:
            if (rs.getDate("exp_date") != null) {
                shipment.setExpDate(rs.getDate("exp_date").toLocalDate());
            }
            shipment.setQuantity(rs.getInt("quantity"));
            shipment.setShipmentProcessed(rs.getBoolean("shipment_processed"));
            return shipment;
        }
    };

    // Create
    public int save(Shipment shipment) {
        String sql = "INSERT INTO Shipments (supplier_ID, product_ID, store_ID, buy_price, production_date, shipment_date, exp_date, quantity, shipment_processed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                shipment.getSupplierId(),
                shipment.getProductId(),
                shipment.getStoreId(),
                shipment.getBuyPrice(),
                shipment.getProductionDate(),
                shipment.getShipmentDate(),
                shipment.getExpDate(),
                shipment.getQuantity(),
                shipment.getShipmentProcessed());
    }

    // Create a new shipment for the receiving store and return the generated shipment ID
    public int createShipmentForReceivingStore(Shipment shipment) {
        String sql = "INSERT INTO Shipments (supplier_ID, product_ID, store_ID, buy_price, production_date, shipment_date, exp_date, quantity, shipment_processed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create KeyHolder to capture generated key
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        // Execute insert query and capture generated keys
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, shipment.getSupplierId());
            ps.setInt(2, shipment.getProductId());
            ps.setInt(3, shipment.getStoreId());
            ps.setBigDecimal(4, shipment.getBuyPrice());
            ps.setDate(5, Date.valueOf(shipment.getProductionDate()));  // Use Date.valueOf for LocalDate
            ps.setDate(6, Date.valueOf(shipment.getShipmentDate()));  // Use Date.valueOf for LocalDate
            ps.setDate(7, Date.valueOf(shipment.getExpDate()));  // Use Date.valueOf for LocalDate
            ps.setInt(8, shipment.getQuantity());
            ps.setBoolean(9, shipment.getShipmentProcessed());
            return ps;
        }, keyHolder);

        // Set the generated shipment ID to the shipment object
        shipment.setShipmentId(keyHolder.getKey().intValue());
        return shipment.getShipmentId();  // Return the generated shipment ID
    }

    // Read by shipment_ID
    public Shipment getShipmentById(Integer shipmentId) {
        String sql = "SELECT * FROM Shipments WHERE shipment_ID = ?";
        return jdbcTemplate.queryForObject(sql, shipmentRowMapper, shipmentId);
    }

    // Read all shipments
    public List<Shipment> getAllShipments() {
        String sql = "SELECT * FROM Shipments";
        return jdbcTemplate.query(sql, shipmentRowMapper);
    }

    // Retrieve all expired products via Shipments
    public List<Map<String, Object>> identifyExpiredProducts() {
        String sql = "SELECT shipment_ID, product_ID, store_ID, exp_date, quantity FROM Shipments WHERE exp_date < CURRENT_DATE";
        return jdbcTemplate.queryForList(sql);
    }

    // Update shipment by shipment_ID
    public int update(Shipment shipment) {
        String sql = "UPDATE Shipments SET supplier_ID = ?, product_ID = ?, store_ID = ?, buy_price = ?, production_date = ?, shipment_date = ?, exp_date = ?, quantity = ? , shipment_processed = ? " +
                "WHERE shipment_ID = ?";
        return jdbcTemplate.update(sql,
                shipment.getSupplierId(),
                shipment.getProductId(),
                shipment.getStoreId(),
                shipment.getBuyPrice(),
                shipment.getProductionDate(),
                shipment.getShipmentDate(),
                shipment.getExpDate(),
                shipment.getQuantity(),
                shipment.getShipmentId(),
                shipment.getShipmentProcessed());
    }

    // Delete shipment by shipment_ID
    public int delete(Integer shipmentId) {
        String sql = "DELETE FROM Shipments WHERE shipment_ID = ?";
        return jdbcTemplate.update(sql, shipmentId);
    }


    public List<Map<String, Object>> getItemizedBill(Integer supplierId, Integer storeId, LocalDate shipmentDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("supplier_ID AS supplierId, ")
                .append("product_ID AS productId, ")
                .append("store_ID AS storeId, ")
                .append("quantity, ")
                .append("buy_price, ")
                .append("shipment_date AS shipmentDate, ")
                .append("(quantity * buy_price) AS shipmentCost ")
                .append("FROM Shipments ");

        List<Object> params = new ArrayList<>();

        LocalDate effectiveDate = (shipmentDate == null) ? LocalDate.now() : shipmentDate;
        sql.append("WHERE shipment_date = ? ");
        params.add(java.sql.Date.valueOf(effectiveDate));

        // Add additional filters if provided.
        if (supplierId != null) {
            sql.append("AND supplier_ID = ? ");
            params.add(supplierId);
        }
        if (storeId != null) {
            sql.append("AND store_ID = ? ");
            params.add(storeId);
        }

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    // Update inventory to reduce stock for the sending store
    public int reduceInventoryStock(Integer storeId, Integer shipmentId, Integer qty) {
        String sql = "UPDATE Inventory SET product_qty = product_qty - ? WHERE store_ID = ? AND shipment_ID = ?";
        return jdbcTemplate.update(sql, qty, storeId, shipmentId);
    }

    // Get expired products
    public List<Map<String, Object>> getExpiredShipments(Integer storeId) {
        String sqlQuery = "";
        if (Objects.nonNull(storeId)) {
            sqlQuery = "SELECT shipment_ID, product_ID, exp_date, quantity  FROM Shipments  WHERE exp_date < CURRENT_DATE AND store_ID = ?";
            return jdbcTemplate.queryForList(sqlQuery, storeId);
        } else {
            sqlQuery = "SELECT shipment_ID, product_ID, exp_date, quantity  FROM Shipments  WHERE exp_date < CURRENT_DATE";
            return jdbcTemplate.queryForList(sqlQuery);
        }

    }
}
