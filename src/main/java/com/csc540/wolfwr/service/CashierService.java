package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.CashierDAO;
import com.csc540.wolfwr.dto.CashierDTO;
import com.csc540.wolfwr.model.Cashier;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashierService {

    private final CashierDAO cashierDAO;

    public CashierService(CashierDAO cashierDAO) {
        this.cashierDAO = cashierDAO;
    }

    // Create a new cashier record
    public CashierDTO createCashier(CashierDTO cashierDTO) {
        Cashier cashier = new Cashier();
        BeanUtils.copyProperties(cashierDTO, cashier);
        cashierDAO.save(cashier);
        return cashierDTO;
    }

    // Retrieve a cashier by ID
    public CashierDTO getCashierById(Integer cashierId) {
        Cashier cashier = cashierDAO.getCashierById(cashierId);
        CashierDTO dto = new CashierDTO();
        BeanUtils.copyProperties(cashier, dto);
        return dto;
    }

    // Retrieve all cashiers
    public List<CashierDTO> getAllCashiers() {
        List<Cashier> cashiers = cashierDAO.getAllCashiers();
        return cashiers.stream().map(cashier -> {
            CashierDTO dto = new CashierDTO();
            BeanUtils.copyProperties(cashier, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Delete a cashier by ID
    public void deleteCashier(Integer cashierId) {
        cashierDAO.delete(cashierId);
    }
}