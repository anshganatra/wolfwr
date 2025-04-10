package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.TransactionItem;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionItemDAO {
    private final JdbcTemplate jdbcTemplate;

    public TransactionItemDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TransactionItem> transactionItemRowMapper = new RowMapper<TransactionItem>() {
        @Override
        public TransactionItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransactionId(rs.getInt("transaction_ID"));
            transactionItem.setProductBatchId(rs.getInt("product_batch_ID"));
            transactionItem.setPrice(rs.getBigDecimal("price"));
            transactionItem.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
            transactionItem.setQuantity(rs.getInt("quantity"));
            return transactionItem;
        }
    };

    // Create - Insert a new TransactionItem record
    public int save(TransactionItem transactionItem) {
        String sql = "INSERT INTO TransactionItems (transaction_ID, product_batch_ID, price, discounted_price, quantity) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                transactionItem.getTransactionId(),
                transactionItem.getProductBatchId(),
                transactionItem.getPrice(),
                transactionItem.getDiscountedPrice(),
                transactionItem.getQuantity());
    }

    // Read - Get all TransactionItem records
    public List<TransactionItem> getAllTransactionItems() {
        String sql = "SELECT * FROM TransactionItems";
        return jdbcTemplate.query(sql, transactionItemRowMapper);
    }

    // Retrieve all TransactionItems Purchased by a Customer between two dates
    public List<Map<String, Object>> getAllTransactionItemsBetweenTwoDates(LocalDate startDate,
                                                                           LocalDate endDate,
                                                                           Integer memberId) {
        String sql = "SELECT ti.transaction_ID, ti.product_batch_ID, ti.discounted_price, ti.quantity, "
            + "ti.discounted_price*ti.quantity AS total_price FROM TransactionItems ti JOIN "
            + "Transactions t ON ti.transaction_ID = t.transaction_ID WHERE t.member_ID = ? AND t.date "
            + "BETWEEN ? AND ?";
        return jdbcTemplate.queryForList(sql, memberId, startDate, endDate);
    }
  
    // Read - Get all TransactionItems for a Transaction
    public List<TransactionItem> getTransactionItems(Integer transactionId) {
        String sql = "SELECT * FROM TransactionItems WHERE transaction_id = ?";
        return jdbcTemplate.query(sql, transactionItemRowMapper, transactionId);
    }

    // Update - Update an existing TransactionItem record.
    public int update(TransactionItem transactionItem, Integer transactionId, Integer productBatchId) {
        String sql = "UPDATE TransactionItems SET transaction_ID = ?, product_batch_ID = ?, price = ?, discounted_price = ?, quantity = ? WHERE transaction_ID = ? AND product_batch_ID = ?";
        return jdbcTemplate.update(sql,
                transactionItem.getTransactionId(),
                transactionItem.getProductBatchId(),
                transactionItem.getPrice(),
                transactionItem.getDiscountedPrice(),
                transactionItem.getQuantity(),
                transactionId,
                productBatchId);
    }

    // Delete - Remove a TransactionItem record by transaction ID and product batch ID.
    public int delete(Integer transactionId, Integer productBatchId) {
        String sql = "DELETE FROM TransactionItems WHERE transaction_ID = ? AND product_batch_ID = ?";
        return jdbcTemplate.update(sql, transactionId, productBatchId);
    }

    /**
     * Retrieves all transaction items for the given transaction ID.
     * Each transaction item is returned as a Map where keys correspond to the column names.
     *
     * @param transactionId the transaction ID to filter on
     * @return a list of maps, each representing a transaction item row
     */
    public List<Map<String, Object>> getTransactionItemsByTransactionId(Integer transactionId) {
        String sql = "SELECT * FROM TransactionItems WHERE transaction_ID = ?";
        return jdbcTemplate.queryForList(sql, transactionId);
    }
}
