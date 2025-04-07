package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.ReturnTransactionDAO;
import com.csc540.wolfwr.dto.ReturnTransactionDTO;
import com.csc540.wolfwr.model.ReturnTransaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReturnTransactionService {

    private final ReturnTransactionDAO dao;

    public ReturnTransactionService(ReturnTransactionDAO dao) {
        this.dao = dao;
    }

    public ReturnTransactionDTO create(ReturnTransactionDTO dto) {
        ReturnTransaction transaction = new ReturnTransaction();
        BeanUtils.copyProperties(dto, transaction);
        dao.save(transaction);
        return dto;
    }

    public ReturnTransactionDTO getById(Integer transactionId) {
        ReturnTransaction transaction = dao.getByTransactionId(transactionId);
        ReturnTransactionDTO dto = new ReturnTransactionDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }

    public List<ReturnTransactionDTO> getAll() {
        return dao.getAll().stream().map(transaction -> {
            ReturnTransactionDTO dto = new ReturnTransactionDTO();
            BeanUtils.copyProperties(transaction, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public ReturnTransactionDTO update(ReturnTransactionDTO dto) {
        ReturnTransaction transaction = new ReturnTransaction();
        BeanUtils.copyProperties(dto, transaction);
        dao.update(transaction);
        return dto;
    }

    public void delete(Integer transactionId) {
        dao.delete(transactionId);
    }
}
