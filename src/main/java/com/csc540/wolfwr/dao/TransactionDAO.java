package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Transaction> transactionRowMapper = new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("transaction_ID"));
            transaction.setStoreId(rs.getInt("store_ID"));
            transaction.setTotalPrice(rs.getBigDecimal("total_price"));
            transaction.setDate(rs.getTimestamp("date").toLocalDateTime());
            transaction.setType(rs.getString("type"));
            transaction.setCashierId(rs.getInt("cashier_ID"));
            transaction.setMemberId(rs.getInt("member_ID"));
            transaction.setCompletedStatus(rs.getBoolean("completedStatus"));
            return transaction;
        }
    };

    // Save a new transaction
    public int save(Transaction transaction) {
        String sql = "INSERT INTO Transactions (store_ID, total_price, date, type, cashier_ID, member_ID, completedStatus) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                transaction.getStoreId(),
                transaction.getTotalPrice(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCashierId(),
                transaction.getMemberId(),
                transaction.getCompletedStatus());
    }

    // Retrieve a transaction by its ID
    public Transaction getTransactionById(Integer transactionId) {
        String sql = "SELECT * FROM Transactions WHERE transaction_ID = ?";
        return jdbcTemplate.queryForObject(sql, transactionRowMapper, transactionId);
    }

    // Retrieve all transactions
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM Transactions";
        return jdbcTemplate.query(sql, transactionRowMapper);
    }

    // Update an existing transaction
    public int update(Transaction transaction) {
        String sql = "UPDATE Transactions SET store_ID = ?, total_price = ?, date = ?, type = ?, cashier_ID = ?, " +
                     "member_ID = ?, completedStatus = ? WHERE transaction_ID = ?";
        return jdbcTemplate.update(sql,
                transaction.getStoreId(),
                transaction.getTotalPrice(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCashierId(),
                transaction.getMemberId(),
                transaction.getCompletedStatus(),
                transaction.getTransactionId());
    }

    // Delete a transaction by ID
    public int delete(Integer transactionId) {
        String sql = "DELETE FROM Transactions WHERE transaction_ID = ?";
        return jdbcTemplate.update(sql, transactionId);
    }
}
