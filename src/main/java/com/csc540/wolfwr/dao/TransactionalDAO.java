package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.dto.ReturnItemDTO;
import com.csc540.wolfwr.dto.TransferDTO;
import com.csc540.wolfwr.model.*;
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

    public void processReturn(ReturnItemDTO returnItemDTO) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // 1. Verify original transaction contains the product
            String itemQuery = "SELECT * FROM TransactionItems WHERE transaction_ID = ? AND product_batch_ID = ?";
            TransactionItem item;
            try (PreparedStatement ps = conn.prepareStatement(itemQuery)) {
                ps.setInt(1, returnItemDTO.getOgTid());
                ps.setInt(2, returnItemDTO.getShipmentId());
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
                ps.setInt(1, returnItemDTO.getOgTid());
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
                ps.setInt(5, returnItemDTO.getCashierId());
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
                ps.setInt(2, returnItemDTO.getOgTid());
                ps.executeUpdate();
            }

            // 5. Update inventory
            String updateInv = "UPDATE Inventory SET product_qty = product_qty + ? WHERE shipment_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateInv)) {
                ps.setInt(1, returnItemDTO.getQuantity());
                ps.setInt(2, returnItemDTO.getShipmentId());
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

    public void transferProductsAtomic(TransferDTO transferDTO) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Reduce stock from the source store
            String reduceSql = "UPDATE Inventory SET product_qty = product_qty - ? WHERE store_ID = ? AND shipment_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(reduceSql)) {
                ps.setInt(1, transferDTO.getProductQty());
                ps.setInt(2, transferDTO.getSourceStoreId());
                ps.setInt(3, transferDTO.getShipmentId());
                int rows = ps.executeUpdate();
                if (rows == 0) throw new IllegalArgumentException("Insufficient stock in source store.");
            }

            // Step 2: Get the original shipment
            Shipment sourceShipment;
            String shipmentSql = "SELECT * FROM Shipments WHERE shipment_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(shipmentSql)) {
                ps.setInt(1, transferDTO.getShipmentId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new RuntimeException("Shipment not found");

                    sourceShipment = new Shipment();
                    sourceShipment.setSupplierId(rs.getInt("supplier_ID"));
                    sourceShipment.setProductId(rs.getInt("product_ID"));
                    sourceShipment.setBuyPrice(rs.getBigDecimal("buy_price"));
                    sourceShipment.setProductionDate(rs.getDate("production_date").toLocalDate());
                    sourceShipment.setShipmentDate(rs.getDate("shipment_date").toLocalDate());
                    sourceShipment.setExpDate(rs.getDate("exp_date") != null ? rs.getDate("exp_date").toLocalDate() : null);
                }
            }

            // Step 3: Insert new shipment for receiving store
            String insertShipment = "INSERT INTO Shipments (supplier_ID, product_ID, store_ID, buy_price, production_date, shipment_date, exp_date, quantity, shipment_processed) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int newShipmentId;
            try (PreparedStatement ps = conn.prepareStatement(insertShipment, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, sourceShipment.getSupplierId());
                ps.setInt(2, transferDTO.getProductId());
                ps.setInt(3, transferDTO.getDestinationStoreId());
                ps.setBigDecimal(4, sourceShipment.getBuyPrice());
                ps.setDate(5, Date.valueOf(sourceShipment.getProductionDate()));
                ps.setDate(6, Date.valueOf(LocalDate.now()));
                ps.setDate(7, sourceShipment.getExpDate() != null ? Date.valueOf(sourceShipment.getExpDate()) : null);
                ps.setInt(8, transferDTO.getProductQty());
                ps.setBoolean(9, false); // Assuming unprocessed

                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) throw new RuntimeException("Failed to create new shipment");
                newShipmentId = rs.getInt(1);
            }

            // Step 4: Add inventory to receiving store
            String insertInventory = "INSERT INTO Inventory (store_ID, shipment_ID, product_ID, market_price, product_qty) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertInventory)) {
                ps.setInt(1, transferDTO.getDestinationStoreId());
                ps.setInt(2, newShipmentId);
                ps.setInt(3, transferDTO.getProductId());
                ps.setBigDecimal(4, sourceShipment.getBuyPrice());
                ps.setInt(5, transferDTO.getProductQty());
                ps.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Product transfer failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

}
