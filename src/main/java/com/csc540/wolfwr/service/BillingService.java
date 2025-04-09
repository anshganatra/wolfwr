package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionDAO;
import com.csc540.wolfwr.dao.TransactionItemDAO;
import com.csc540.wolfwr.dto.*;
import com.csc540.wolfwr.model.Transaction;
import com.csc540.wolfwr.model.TransactionItem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BillingService {

    private final TransactionDAO transactionDAO;
    private final TransactionItemDAO transactionItemDAO;

    public BillingService(TransactionDAO transactionDAO, TransactionItemDAO transactionItemDAO) {
        this.transactionDAO = transactionDAO;
        this.transactionItemDAO = transactionItemDAO;
    }

    public Integer createBill(BillRequestDTO billRequest) {
        // Step 1: Create Transaction
        Transaction transaction = new Transaction();
        transaction.setStoreId(billRequest.getStoreId());
        transaction.setCashierId(billRequest.getCashierId());
        transaction.setMemberId(billRequest.getMemberId());
        transaction.setTotalPrice(billRequest.getTotalPrice());
        transaction.setDate(billRequest.getDate() != null ? billRequest.getDate() : LocalDateTime.now());
        transaction.setType("PURCHASE");
        transaction.setCompletedStatus(false);

        transactionDAO.save(transaction); // assumes auto-incremented ID is populated

        // Step 2: Create TransactionItems
        for (BillItemDTO item : billRequest.getItems()) {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransactionId(transaction.getTransactionId()); // assuming set by DB or DAO
            transactionItem.setProductBatchId(item.getProductBatchId());
            transactionItem.setQuantity(item.getQuantity());
            transactionItem.setPrice(item.getPrice());
            transactionItem.setDiscountedPrice(item.getDiscountedPrice());

            transactionItemDAO.save(transactionItem);
        }
        transaction.setCompletedStatus(true);
        return transaction.getTransactionId();
    }
}
