package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.SupplierDAO;
import com.csc540.wolfwr.dto.SupplierDTO;
import com.csc540.wolfwr.model.Supplier;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierService(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);
        supplierDAO.save(supplier);
        return supplierDTO;
    }

    public SupplierDTO getSupplierById(Integer supplierId) {
        Supplier supplier = supplierDAO.getSupplierById(supplierId);
        SupplierDTO dto = new SupplierDTO();
        BeanUtils.copyProperties(supplier, dto);
        return dto;
    }

    public List<SupplierDTO> getAllSuppliers() {
        return supplierDAO.getAllSuppliers().stream().map(supplier -> {
            SupplierDTO dto = new SupplierDTO();
            BeanUtils.copyProperties(supplier, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public SupplierDTO updateSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);
        supplierDAO.update(supplier);
        return supplierDTO;
    }

    public void deleteSupplier(Integer supplierId) {
        supplierDAO.delete(supplierId);
    }
}
