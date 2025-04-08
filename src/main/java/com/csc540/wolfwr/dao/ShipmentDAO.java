package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Shipment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            return shipment;
        }
    };

    // Create
    public int save(Shipment shipment) {
        String sql = "INSERT INTO Shipments (supplier_ID, product_ID, store_ID, buy_price, production_date, shipment_date, exp_date, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                shipment.getSupplierId(),
                shipment.getProductId(),
                shipment.getStoreId(),
                shipment.getBuyPrice(),
                shipment.getProductionDate(),
                shipment.getShipmentDate(),
                shipment.getExpDate(),
                shipment.getQuantity());
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

    // Update shipment by shipment_ID
    public int update(Shipment shipment) {
        String sql = "UPDATE Shipments SET supplier_ID = ?, product_ID = ?, store_ID = ?, buy_price = ?, production_date = ?, shipment_date = ?, exp_date = ?, quantity = ? " +
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
                shipment.getShipmentId());
    }

    // Delete shipment by shipment_ID
    public int delete(Integer shipmentId) {
        String sql = "DELETE FROM Shipments WHERE shipment_ID = ?";
        return jdbcTemplate.update(sql, shipmentId);
    }

    // Get expired products
    public List<Shipment> getExpiredShipments(Integer storeId) {
        String sqlQuery = "SELECT shipment_ID, product_ID, exp_date, quantity  FROM Shipments  WHERE exp_date < CURRENT_DATE AND store_ID = ?";
        RowMapper<Shipment> customRowMapper = new RowMapper<Shipment>() {
            @Override
            public Shipment mapRow(ResultSet rs, int rowNum) throws SQLException {
                Shipment shipment = new Shipment();
                shipment.setShipmentId(rs.getInt("shipment_ID"));
                shipment.setProductId(rs.getInt("product_ID"));
                shipment.setExpDate(rs.getDate("exp_date").toLocalDate());
                shipment.setQuantity(rs.getInt("quantity"));
                return shipment;
            }
        };
        return jdbcTemplate.query(sqlQuery, customRowMapper, storeId);
    }
}