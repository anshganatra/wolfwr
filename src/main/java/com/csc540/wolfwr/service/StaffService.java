package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.StaffDAO;
import com.csc540.wolfwr.dto.*;
import com.csc540.wolfwr.model.Staff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffDAO staffDAO;
    private final StoreService storeService; // Used to validate store existence
    private final RegistrationStaffService registrationStaffService;
    private final BillingStaffService billingStaffService;
    private final WarehouseStaffService warehouseStaffService;
    private final CashierService cashierService;
    private final ManagerService managerService;


    public StaffService(StaffDAO staffDAO, StoreService storeService, RegistrationStaffService registrationStaffService, BillingStaffService billingStaffService, WarehouseStaffService warehouseStaffService, CashierService cashierService, ManagerService managerService) {
        this.staffDAO = staffDAO;
        this.storeService = storeService;
        this.registrationStaffService = registrationStaffService;
        this.billingStaffService = billingStaffService;
        this.warehouseStaffService = warehouseStaffService;
        this.cashierService = cashierService;
        this.managerService = managerService;
    }

    // Create a new staff member with store existence check
    public StaffDTO createStaff(StaffDTO staffDTO) {
        if (staffDTO.getStoreId() != null) {
            // Check if the store exists
            if (storeService.getStoreById(staffDTO.getStoreId()) == null) {
                throw new IllegalArgumentException("Store with ID " + staffDTO.getStoreId() + " does not exist.");
            }
        }
        if (Objects.isNull(staffDTO.getDoj())) {
            staffDTO.setDoj(LocalDate.now());
        }
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffDTO, staff);
        staff.setAge(Period.between(staff.getDob(), LocalDate.now()).getYears());
        staffDAO.save(staff);
        updateSpecializedTables(staff.getTitle(), staff.getStaffId());
        return staffDTO;
    }

    private void updateSpecializedTables(String staffTitle, Integer staffId) {
        switch (staffTitle) {
            case "Registration Staff":
                RegistrationStaffDTO registrationStaffDTO = new RegistrationStaffDTO();
                registrationStaffDTO.setRegistrationStaffId(staffId);
                registrationStaffService.create(registrationStaffDTO);
                break;

            case "Billing Staff":
                BillingStaffDTO billingStaffDTO = new BillingStaffDTO();
                billingStaffDTO.setBillingStaffId(staffId);
                billingStaffService.create(billingStaffDTO);
                break;

            case "Warehouse Staff":
                WarehouseStaffDTO warehouseStaffDTO = new WarehouseStaffDTO();
                warehouseStaffDTO.setWarehouseStaffId(staffId);
                warehouseStaffService.createWarehouseStaff(warehouseStaffDTO);
                break;

            case "Cashier":
                CashierDTO cashierDTO = new CashierDTO();
                cashierDTO.setCashierId(staffId);
                cashierService.createCashier(cashierDTO);
                break;

            case "Store Manager":
                ManagerDTO storeManagerDTO = new ManagerDTO();
                storeManagerDTO.setManagerId(staffId);
                managerService.createManager(storeManagerDTO);
                break;
        }
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
    
 // Method to assign a staff member to the manager's store
    public void assignStaffToManagerStore(Integer managerId, Integer staffId) {
        // Get the storeId associated with the manager
        Integer managerStoreId = staffDAO.getStoreIdByStaffId(managerId);
        
        // If storeId exists, assign the staff to that store
        if (managerStoreId != null) {
            staffDAO.assignStaffToStore(staffId, managerStoreId);
        } else {
            throw new IllegalArgumentException("Manager not found or manager does not have a store assigned.");
        }
    }
}