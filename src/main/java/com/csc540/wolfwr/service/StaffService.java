package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.StaffDAO;
import com.csc540.wolfwr.dto.StaffDTO;
import com.csc540.wolfwr.model.Staff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffDAO staffDAO;
    private final StoreService storeService; // Used to validate store existence

    public StaffService(StaffDAO staffDAO, StoreService storeService) {
        this.staffDAO = staffDAO;
        this.storeService = storeService;
    }

    // Create a new staff member with store existence check
    public StaffDTO createStaff(StaffDTO staffDTO) {
        if (staffDTO.getStoreId() != null) {
            // Check if the store exists
            if (storeService.getStoreById(staffDTO.getStoreId()) == null) {
                throw new IllegalArgumentException("Store with ID " + staffDTO.getStoreId() + " does not exist.");
            }
        }
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffDTO, staff);
        staffDAO.save(staff);
        return staffDTO;
    }

    // Retrieve a staff member by ID
    public StaffDTO getStaffById(Integer staffId) {
        Staff staff = staffDAO.getStaffById(staffId);
        StaffDTO dto = new StaffDTO();
        BeanUtils.copyProperties(staff, dto);
        return dto;
    }

    // Retrieve all staff members
    public List<StaffDTO> getAllStaff() {
        List<Staff> staffList = staffDAO.getAllStaff();
        return staffList.stream().map(staff -> {
            StaffDTO dto = new StaffDTO();
            BeanUtils.copyProperties(staff, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing staff member with store existence check
    public StaffDTO updateStaff(StaffDTO staffDTO) {
        if (staffDTO.getStoreId() != null) {
            // Check if the store exists
            if (storeService.getStoreById(staffDTO.getStoreId()) == null) {
                throw new IllegalArgumentException("Store with ID " + staffDTO.getStoreId() + " does not exist.");
            }
        }
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffDTO, staff);
        staffDAO.update(staff);
        return staffDTO;
    }

    // Delete a staff member by ID
    public void deleteStaff(Integer staffId) {
        staffDAO.delete(staffId);
    }
}