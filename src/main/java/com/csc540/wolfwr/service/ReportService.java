package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.TransactionDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final TransactionDAO transactionDAO;

    public ReportService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    // Method to get membership level and reward total for each member with optional year filter
    public List<Map<String, Object>> getMembershipAndRewards(Integer year) {
        // Calling the DAO method to get the membership level and reward total
        return transactionDAO.getMembershipAndRewards(year);
    }
}