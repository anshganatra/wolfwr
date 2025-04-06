package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionDAO;
import com.csc540.wolfwr.dto.TransactionDTO;
import com.csc540.wolfwr.model.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    // Create a new transaction
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        transactionDAO.save(transaction);
        return transactionDTO;
    }

    // Retrieve a transaction by ID as a DTO
    public TransactionDTO getTransactionDTOById(Integer transactionId) {
        Transaction transaction = transactionDAO.getTransactionById(transactionId);
        TransactionDTO dto = new TransactionDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }

    // Retrieve all transactions as DTOs
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        return transactions.stream().map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            BeanUtils.copyProperties(transaction, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing transaction
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        transactionDAO.update(transaction);
        return transactionDTO;
    }

    // Delete a transaction by ID
    public void deleteTransaction(Integer transactionId) {
        transactionDAO.delete(transactionId);
    }
}
