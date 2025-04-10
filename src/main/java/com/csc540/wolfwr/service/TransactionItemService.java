package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionItemDAO;
import com.csc540.wolfwr.dto.TransactionItemDTO;
import com.csc540.wolfwr.model.TransactionItem;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    // Retrieve all TransactionItems Purchased by a Customer between two dates
    public List<Map<String, Object>> getAllTransactionItemsBetweenTwoDates(LocalDate startDate,
                                                                           LocalDate endDate,
                                                                           Integer memberId) {
        return transactionItemDAO.getAllTransactionItemsBetweenTwoDates(startDate, endDate, memberId);
    }
  
    // Retrieve all transaction items in a transaction
    public List<TransactionItemDTO> getAllTransactionItemsFromATransaction(Integer transactionId) {
        List<TransactionItem> transactionItems = transactionItemDAO.getTransactionItems(transactionId);
        return transactionItems.stream().map(transactionItem -> {
            TransactionItemDTO dto = new TransactionItemDTO();
            BeanUtils.copyProperties(transactionItem, dto);
            return dto;
        }).toList();
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

    /**
     * Retrieves transaction items for the given transaction ID.
     *
     * @param transactionId the transaction ID
     * @return a list of maps, each representing a transaction item row
     */
    public List<Map<String, Object>> getTransactionItemsByTransactionId(Integer transactionId) {
        return transactionItemDAO.getTransactionItemsByTransactionId(transactionId);
    }
}
