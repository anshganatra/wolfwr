package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Product;
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
    public Product save(Product product) {
        String sqlQuery = "INSERT INTO Products (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProductName());
            return ps;
        }, keyHolder);

        // Assuming the ID is of type long or int
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            product.setProductId(generatedId.intValue());
        }

        return product;
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
