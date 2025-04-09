package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getSalesReport(String reportType, Date startDate, Date endDate, Integer storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        // Build the grouping column based on reportType.
        switch (reportType.toLowerCase()) {
            case "daily":
                // Group by day: DATE(date)
                sql.append("DATE(date) AS reportPeriod, ");
                break;
            case "monthly":
                // Group by month: Format as YYYY-MM
                sql.append("DATE_FORMAT(date, '%Y-%m') AS reportPeriod, ");
                break;
            case "quarterly":
                // Group by quarter: e.g., "2025-Q1"
                sql.append("CONCAT(YEAR(date), '-Q', QUARTER(date)) AS reportPeriod, ");
                break;
            case "annually":
                // Group by year: YEAR(date)
                sql.append("YEAR(date) AS reportPeriod, ");
                break;
            default:
                // This case should be caught before calling the DAO.
                throw new IllegalArgumentException("Invalid report type.");
        }

        sql.append("store_ID AS storeId, ");
        sql.append("SUM(total_price) AS totalSales ");
        sql.append("FROM Transactions ");
        sql.append("WHERE type = 'Purchase' AND completedStatus = 1 ");
        sql.append("AND date BETWEEN ? AND ? ");

        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);

        if (storeId != null) {
            sql.append("AND store_ID = ? ");
            params.add(storeId);
        }

        // Group by the chosen reportPeriod and store_ID.
        switch (reportType.toLowerCase()) {
            case "daily":
                sql.append("GROUP BY DATE(date), store_ID ");
                break;
            case "monthly":
                sql.append("GROUP BY DATE_FORMAT(date, '%Y-%m'), store_ID ");
                break;
            case "quarterly":
                sql.append("GROUP BY CONCAT(YEAR(date), '-Q', QUARTER(date)), store_ID ");
                break;
            case "annually":
                sql.append("GROUP BY YEAR(date), store_ID ");
                break;
        }

        sql.append("ORDER BY reportPeriod ASC, store_ID ASC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }
}
