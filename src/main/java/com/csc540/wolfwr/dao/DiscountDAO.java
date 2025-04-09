package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DiscountDAO {

    private final JdbcTemplate jdbcTemplate;

    public DiscountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Discount> discountRowMapper = new RowMapper<Discount>() {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            Discount discount = new Discount();
            discount.setDiscountId(rs.getInt("discount_ID"));
            discount.setProductId(rs.getInt("product_ID"));
            int shipmentId = rs.getInt("shipment_ID");
            discount.setShipmentId(rs.wasNull() ? null : shipmentId);
            discount.setType(rs.getString("type"));
            discount.setValue(rs.getBigDecimal("value"));
            discount.setStartDate(rs.getDate("start_date").toLocalDate());
            discount.setEndDate(rs.getDate("end_date").toLocalDate());
            return discount;
        }
    };

    public int save(Discount discount) {
        String sql = "INSERT INTO Discounts (product_ID, shipment_ID, type, value, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                discount.getProductId(),
                discount.getShipmentId(),
                discount.getType(),
                discount.getValue(),
                discount.getStartDate(),
                discount.getEndDate());
    }

    public Discount getById(Integer id) {
        String sql = "SELECT * FROM Discounts WHERE discount_ID = ?";
        return jdbcTemplate.queryForObject(sql, discountRowMapper, id);
    }

    public List<Discount> getAll() {
        String sql = "SELECT * FROM Discounts";
        return jdbcTemplate.query(sql, discountRowMapper);
    }

    public List<Discount> getByProductIdOrShipmentId(Integer productId, Integer shipmentId) {
        String sql = "SELECT * FROM Discounts WHERE product_ID = ? OR shipment_ID = ?";
        return jdbcTemplate.query(sql, discountRowMapper, productId, shipmentId);
    }

    public int update(Discount discount) {
        String sql = "UPDATE Discounts SET product_ID = ?, shipment_ID = ?, type = ?, value = ?, start_date = ?, end_date = ? WHERE discount_ID = ?";
        return jdbcTemplate.update(sql,
                discount.getProductId(),
                discount.getShipmentId(),
                discount.getType(),
                discount.getValue(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getDiscountId());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM Discounts WHERE discount_ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
