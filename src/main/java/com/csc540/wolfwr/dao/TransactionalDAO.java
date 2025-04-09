package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Inventory;
import com.csc540.wolfwr.model.ReturnTransaction;
import com.csc540.wolfwr.model.Transaction;
import com.csc540.wolfwr.model.TransactionItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TransactionalDAO {

    private final DataSource dataSource;

    public TransactionalDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void processReturn(int ogTid, int shipmentId, int productBatchId, int quantity) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // 1. Verify original transaction contains the product
            String itemQuery = "SELECT * FROM TransactionItems WHERE transaction_ID = ? AND product_batch_ID = ?";
            TransactionItem item;
            try (PreparedStatement ps = conn.prepareStatement(itemQuery)) {
                ps.setInt(1, ogTid);
                ps.setInt(2, productBatchId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new RuntimeException("Product not found in original transaction");

                item = new TransactionItem();
                item.setTransactionId(rs.getInt("transaction_ID"));
                item.setProductBatchId(rs.getInt("product_batch_ID"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                item.setQuantity(rs.getInt("quantity"));
            }

            // 2. Get original transaction
            String txQuery = "SELECT * FROM Transactions WHERE transaction_ID = ?";
            Transaction originalTx;
            try (PreparedStatement ps = conn.prepareStatement(txQuery)) {
                ps.setInt(1, ogTid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new RuntimeException("Original transaction not found");

                originalTx = new Transaction();
                originalTx.setStoreId(rs.getInt("store_ID"));
                originalTx.setCashierId(rs.getInt("cashier_ID"));
                originalTx.setMemberId(rs.getInt("member_ID"));
            }

            // 3. Insert return transaction
            String insertTx = "INSERT INTO Transactions (store_ID, total_price, date, type, cashier_ID, member_ID, completedStatus) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int newTxId;
            try (PreparedStatement ps = conn.prepareStatement(insertTx, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, originalTx.getStoreId());
                BigDecimal price = (item.getDiscountedPrice().compareTo(BigDecimal.ZERO) == 0) ? item.getPrice() : item.getDiscountedPrice();
                ps.setBigDecimal(2, price);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
                ps.setString(4, "RETURN");
                ps.setInt(5, originalTx.getCashierId());
                ps.setInt(6, originalTx.getMemberId());
                ps.setBoolean(7, true);

                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) throw new RuntimeException("Failed to insert return transaction");
                newTxId = rs.getInt(1);
            }

            // 4. Insert return mapping
            String insertReturn = "INSERT INTO ReturnTransactions (transaction_ID, reference_transaction_ID) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertReturn)) {
                ps.setInt(1, newTxId);
                ps.setInt(2, ogTid);
                ps.executeUpdate();
            }

            // 5. Update inventory
            String updateInv = "UPDATE Inventory SET product_qty = product_qty + ? WHERE shipment_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateInv)) {
                ps.setInt(1, quantity);
                ps.setInt(2, shipmentId);
                int rows = ps.executeUpdate();
                if (rows != 1) throw new RuntimeException("Failed to update inventory");
            }

            conn.commit();

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            throw new RuntimeException("Return flow failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }
}
