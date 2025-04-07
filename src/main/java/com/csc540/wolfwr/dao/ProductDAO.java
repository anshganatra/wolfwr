package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setProductId(rs.getInt("product_ID"));
            product.setProductName(rs.getString("name"));
            return product;
        }
    };

    // Create a new product
    public int save(Product product) {
        String sqlQuery = "INSERT INTO Products (name) VALUES (?)";
        return jdbcTemplate.update(sqlQuery, product.getProductName());
    }

    // Get all products
    public List<Product> getAllProducts() {
        String sqlQuery = "SELECT * FROM Products";
        return jdbcTemplate.query(sqlQuery, productRowMapper);
    }

    // Get product by ID
    public Product getProductById(Integer productId){
        String sqlQuery = "SELECT * FROM Products WHERE product_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, productRowMapper, productId);
    }

    // Delete product by product_ID
    public int delete(Integer productId) {
        String sql = "DELETE FROM Products WHERE product_ID = ?";
        return jdbcTemplate.update(sql, productId);
    }
}
