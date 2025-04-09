package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    // Retrieve all transaction from a given member made between two dates
    public List<Map<String, Object>> getTransactionsByMemberAndDates(Integer memberId, LocalDate startDate,
                                                             LocalDate endDate) {
        String sql = "SELECT transaction_ID, total_price, date FROM Transactions WHERE member_ID = ? " + 
                     "AND date BETWEEN ? AND ?";
        return jdbcTemplate.queryForList(sql, memberId, startDate, endDate);
    }

    // Generate a report of sales growth within a given time period. 
    public List<Map<String, Object>> generateSalesGrowthReport(LocalDate currentPeriodStartDate, 
                                                               LocalDate currentPeriodEndDate,
                                                               LocalDate previousPeriodStartDate,
                                                               LocalDate previousPeriodEndDate,
                                                               Integer storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT store_ID, SUM(CASE WHEN date BETWEEN ? AND ? "
        + "THEN total_price ELSE 0 END) AS sales_current_period, SUM(CASE WHEN date BETWEEN ? "
        + "AND ? THEN total_price ELSE 0 END) AS sales_previous_period, "
        + "ROUND((SUM(CASE WHEN date BETWEEN ? AND ? THEN total_price ELSE 0 END) "
        + "- SUM(CASE WHEN date BETWEEN ? AND ? THEN total_price ELSE 0 "
        + "END)) / NULLIF(SUM(CASE WHEN date BETWEEN ? AND ? THEN "
        + "total_price ELSE 0 END), 0) * 100, 2) AS sales_growth_percentage FROM Transactions ");

        if (storeId != null) {
            sql.append("WHERE store_ID = ?");
            return jdbcTemplate.queryForList(sql.toString(), currentPeriodStartDate, currentPeriodEndDate, 
                   previousPeriodStartDate, previousPeriodEndDate, currentPeriodStartDate, 
                   currentPeriodEndDate, previousPeriodStartDate, previousPeriodEndDate, 
                   previousPeriodStartDate, previousPeriodEndDate, storeId);
        } else {
            sql.append("GROUP BY store_ID");
        }
        return jdbcTemplate.queryForList(sql.toString(), currentPeriodStartDate, currentPeriodEndDate, 
               previousPeriodStartDate, previousPeriodEndDate, currentPeriodStartDate, currentPeriodEndDate,
               previousPeriodStartDate, previousPeriodEndDate, previousPeriodStartDate, previousPeriodEndDate);
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
