package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.ReturnTransaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReturnTransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    public ReturnTransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReturnTransaction> rowMapper = new RowMapper<>() {
        @Override
        public ReturnTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReturnTransaction transaction = new ReturnTransaction();
            transaction.setTransactionId(rs.getInt("transaction_ID"));
            transaction.setReferenceTransactionId(rs.getInt("reference_transaction_ID"));
            return transaction;
        }
    };

    public int save(ReturnTransaction transaction) {
        String sql = "INSERT INTO ReturnTransactions (transaction_ID, reference_transaction_ID) VALUES (?, ?)";
        return jdbcTemplate.update(sql, transaction.getTransactionId(), transaction.getReferenceTransactionId());
    }

    public ReturnTransaction getByTransactionId(Integer transactionId) {
        String sql = "SELECT * FROM ReturnTransactions WHERE transaction_ID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, transactionId);
    }

    public List<ReturnTransaction> getAll() {
        String sql = "SELECT * FROM ReturnTransactions";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int update(ReturnTransaction transaction) {
        String sql = "UPDATE ReturnTransactions SET reference_transaction_ID = ? WHERE transaction_ID = ?";
        return jdbcTemplate.update(sql, transaction.getReferenceTransactionId(), transaction.getTransactionId());
    }

    public int delete(Integer transactionId) {
        String sql = "DELETE FROM ReturnTransactions WHERE transaction_ID = ?";
        return jdbcTemplate.update(sql, transactionId);
    }
}
