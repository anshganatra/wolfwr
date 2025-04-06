package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.BillingStaffDAO;
import com.csc540.wolfwr.dto.BillingStaffDTO;
import com.csc540.wolfwr.model.BillingStaff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingStaffService {

    private final BillingStaffDAO billingStaffDAO;

    public BillingStaffService(BillingStaffDAO billingStaffDAO) {
        this.billingStaffDAO = billingStaffDAO;
    }

    public BillingStaffDTO create(BillingStaffDTO dto) {
        BillingStaff entity = new BillingStaff();
        BeanUtils.copyProperties(dto, entity);
        billingStaffDAO.save(entity);
        return dto;
    }

    public BillingStaffDTO getById(Integer id) {
        BillingStaff entity = billingStaffDAO.getById(id);
        BillingStaffDTO dto = new BillingStaffDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public List<BillingStaffDTO> getAll() {
        return billingStaffDAO.getAll().stream().map(entity -> {
            BillingStaffDTO dto = new BillingStaffDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public void delete(Integer id) {
        billingStaffDAO.delete(id);
    }
}
