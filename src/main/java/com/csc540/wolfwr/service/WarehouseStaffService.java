package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.WarehouseStaffDAO;
import com.csc540.wolfwr.dto.WarehouseStaffDTO;
import com.csc540.wolfwr.model.WarehouseStaff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseStaffService {
    private final WarehouseStaffDAO warehouseStaffDAO;

    public WarehouseStaffService(WarehouseStaffDAO warehouseStaffDAO) {
        this.warehouseStaffDAO = warehouseStaffDAO;
    }

    // Create a new warehouse staff record
    public WarehouseStaffDTO createWarehouseStaff(WarehouseStaffDTO warehouseStaffDTO) {
        WarehouseStaff warehouseStaff = new WarehouseStaff();
        BeanUtils.copyProperties(warehouseStaffDTO, warehouseStaff);
        warehouseStaffDAO.save(warehouseStaff);
        return warehouseStaffDTO;
    }

    // Retrieve a warehouse staff by ID
    public WarehouseStaffDTO getWarehouseStaffById(Integer warehouseStaffId) {
        WarehouseStaff warehouseStaff = warehouseStaffDAO.getWarehouseStaffById(warehouseStaffId);
        WarehouseStaffDTO dto = new WarehouseStaffDTO();
        BeanUtils.copyProperties(warehouseStaff, dto);
        return dto;
    }

    // Retrieve all warehouse staff
    public List<WarehouseStaffDTO> getAllWarehouseStaff() {
        List<WarehouseStaff> warehouseStaffs = warehouseStaffDAO.getAllWarehouseStaff();
        return warehouseStaffs.stream().map(warehouseStaff -> {
            WarehouseStaffDTO dto = new WarehouseStaffDTO();
            BeanUtils.copyProperties(warehouseStaff, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Delete a warehouse staff by ID
    public void deleteWarehouseStaff(Integer warehouseStaffId) {
        warehouseStaffDAO.delete(warehouseStaffId);
    }
}
