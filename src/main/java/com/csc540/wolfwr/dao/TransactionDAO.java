package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // Get total transactions in a day
    public List<Map<String, Object>> getTransactionsPerDay(LocalDate date, Integer storeId) {
        if (Objects.isNull(storeId)) {
            String sqlQuery = """
                    SELECT\s
                        CAST(date AS DATE) AS SaleDate,\s
                        store_ID AS Store,
                        SUM(total_price) AS TotalSales
                    FROM Transactions
                    WHERE type = 'Purchase' AND CAST(date AS DATE) = ?
                    GROUP BY CAST(date AS DATE), store_ID;
                    
                    """;
            return jdbcTemplate.queryForList(sqlQuery, date.toString());
        } else {
            String sqlQuery = """
                    SELECT\s
                        CAST(date AS DATE) AS SaleDate,\s
                        SUM(total_price) AS TotalSales, \s
                        store_ID AS Store\s
                    FROM Transactions
                    WHERE type = 'Purchase' AND store_ID = ?
                    GROUP BY CAST(date AS DATE);
                   \s""";
            return jdbcTemplate.queryForList(sqlQuery, storeId);
        }
    }
}
