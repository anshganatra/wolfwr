package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionDAO;
import com.csc540.wolfwr.dto.TransactionDTO;
import com.csc540.wolfwr.model.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import java.util.List;
import java.util.Map;
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
        int transactionId = transactionDAO.save(transaction);
        transactionDTO.setTransactionId(transactionId);
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

    // Retrieve all transaction from a given member made between two dates
    public List<Map<String, Object>> getTransactionsByMemberAndDates(Integer memberId, LocalDate startDate,
                                                                LocalDate endDate) {
        return transactionDAO.getTransactionsByMemberAndDates(memberId, startDate, endDate);
    }

    // Generate a report of sales growth within a given time period. 
    public List<Map<String, Object>> generateSalesGrowthReport(LocalDate currentPeriodStartDate, 
                                                               LocalDate currentPeriodEndDate,
                                                               LocalDate previousPeriodStartDate,
                                                               LocalDate previousPeriodEndDate,
                                                               Integer storeId) {
        return transactionDAO.generateSalesGrowthReport(currentPeriodStartDate, currentPeriodEndDate, 
               previousPeriodStartDate, previousPeriodEndDate, storeId);
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
    private final List<String> validReportTypes = Arrays.asList("daily", "monthly", "quarterly", "annually");

    public List<Map<String, Object>> getSalesReport(String reportType, LocalDate startDate, LocalDate endDate, Integer storeId) {
        // Validate reportType: exactly one of the valid options must be provided.
        if (reportType == null || !validReportTypes.contains(reportType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid or missing report type. Must be one of: daily, monthly, quarterly, annually.");
        }
        // Default endDate to current date if not provided.
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // Convert LocalDate to java.sql.Date for JDBC.
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        return transactionDAO.getSalesReport(reportType, sqlStartDate, sqlEndDate, storeId);
    }

    // Get transactions per day optionally per store
    public List<Map<String, Object>> getDailySales(LocalDate date, Integer storeId) {
        return transactionDAO.getTransactionsPerDay(date, storeId);
    }

    // Method to generate the total profit report
    public List<Map<String, Object>> getProfitReport(String reportType, LocalDate startDate, LocalDate endDate, Integer storeId) {
        // Validate the startDate and endDate
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must be provided");
        }
        
        if (endDate == null) {
            endDate = LocalDate.now();  // Default to the current date if endDate is not provided
        }

        // Convert LocalDate to java.sql.Date for the query
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Call the DAO method to get the profit report
        return transactionDAO.getTotalProfitReport(sqlStartDate, sqlEndDate, storeId);
    }
    
 // Method to generate the total revenue report
    public List<Map<String, Object>> getRevenueReport(String reportType, LocalDate startDate, LocalDate endDate, Integer storeId) {
        // Validate the startDate and endDate
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must be provided");
        }

        // If endDate is null, set it to the current date
        if (endDate == null) {
            endDate = LocalDate.now();  // Default to the current date if endDate is not provided
        }

        // Convert LocalDate to java.sql.Date for the query
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Call the DAO method to get the total revenue report
        return transactionDAO.getTotalRevenueReport(sqlStartDate, sqlEndDate, storeId);
    }
    
 // Method to generate the customer activity report (total purchase amount)
    public List<Map<String, Object>> getCustomerActivityReport(LocalDate startDate, LocalDate endDate, Integer memberId) {
        // Convert LocalDate to java.sql.Date for the query
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Call the DAO method to get the customer activity report
        return transactionDAO.getCustomerActivityReport(sqlStartDate, sqlEndDate, memberId);
    }
    
 // Method to get membership level and reward total for each member with optional year filter
    public List<Map<String, Object>> getMembershipAndRewards(Integer year) {
        // Calling the DAO method to get the membership level and reward total
        return transactionDAO.getMembershipAndRewards(year);
    }
    
    
}
