package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionItemDAO;
import com.csc540.wolfwr.dto.TransactionItemDTO;
import com.csc540.wolfwr.model.TransactionItem;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionItemService {
    private final TransactionItemDAO transactionItemDAO;
    //private final MemberService transactionService; // Used to validate member existence

    public TransactionItemService(TransactionItemDAO transactionItemDAODAO/*, MemberService memberService*/) {
        this.transactionItemDAO = transactionItemDAODAO;
        // this.transactionService = transactionService;
    }

    // Create a new transaction item with checks on the transaction and product batch IDs.
    public TransactionItemDTO createTransactionItem(TransactionItemDTO transactionItemDTO) {
        // Validity checks go here
        TransactionItem transactionItem = new TransactionItem();
        BeanUtils.copyProperties(transactionItemDTO, transactionItem);
        transactionItemDAO.save(transactionItem);
        return transactionItemDTO;
    }

    // Retrieve all transaction items.
    public List<TransactionItemDTO> getAllTransactionItems() {
        List<TransactionItem> transactionItemList = transactionItemDAO.getAllTransactionItems();
        return transactionItemList.stream().map(transactionItem -> {
            TransactionItemDTO dto = new TransactionItemDTO();
            BeanUtils.copyProperties(transactionItem, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing transaction items with checks on the transaction and product batch IDs.
    public TransactionItemDTO updateTransactionItem(TransactionItemDTO transactionItemDTO, Integer transactionId, Integer productBatchId) {
        // Checks go here.
        TransactionItem transactionItem = new TransactionItem();
        BeanUtils.copyProperties(transactionItemDTO, transactionItem);
        transactionItemDAO.update(transactionItem, transactionId, productBatchId);
        return transactionItemDTO;
    }

    // Delete a transaction item by transaction and product batch ID
    public void deleteTransactionItem(Integer transactionId, Integer productBatchId) {
        transactionItemDAO.delete(transactionId, productBatchId);
    }
}
