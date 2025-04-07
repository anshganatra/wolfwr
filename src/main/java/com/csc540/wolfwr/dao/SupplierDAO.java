package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Supplier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SupplierDAO {

    private final JdbcTemplate jdbcTemplate;

    public SupplierDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Supplier> supplierRowMapper = new RowMapper<>() {
        @Override
        public Supplier mapRow(ResultSet rs, int rowNum) throws SQLException {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(rs.getInt("supplier_ID"));
            supplier.setName(rs.getString("name"));
            supplier.setPhone(rs.getString("phone"));
            supplier.setEmail(rs.getString("email"));
            supplier.setAddress(rs.getString("address"));
            return supplier;
        }
    };

    public int save(Supplier supplier) {
        String sql = "INSERT INTO Suppliers (name, phone, email, address) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, supplier.getName(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress());
    }

    public Supplier getSupplierById(Integer supplierId) {
        String sql = "SELECT * FROM Suppliers WHERE supplier_ID = ?";
        return jdbcTemplate.queryForObject(sql, supplierRowMapper, supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        String sql = "SELECT * FROM Suppliers";
        return jdbcTemplate.query(sql, supplierRowMapper);
    }

    public int update(Supplier supplier) {
        String sql = "UPDATE Suppliers SET name = ?, phone = ?, email = ?, address = ? WHERE supplier_ID = ?";
        return jdbcTemplate.update(sql, supplier.getName(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress(), supplier.getSupplierId());
    }

    public int delete(Integer supplierId) {
        String sql = "DELETE FROM Suppliers WHERE supplier_ID = ?";
        return jdbcTemplate.update(sql, supplierId);
    }
}
