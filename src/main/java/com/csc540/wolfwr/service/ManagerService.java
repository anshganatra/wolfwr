package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.ManagerDAO;
import com.csc540.wolfwr.dto.ManagerDTO;
import com.csc540.wolfwr.dto.SetStoreManagerDTO;
import com.csc540.wolfwr.dto.StaffDTO;
import com.csc540.wolfwr.dto.StoreDTO;
import com.csc540.wolfwr.model.Manager;
import com.csc540.wolfwr.model.Staff;
import com.csc540.wolfwr.model.Store;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerDAO managerDAO;
    private final StaffService staffService;
    private final StoreService storeService;

    public ManagerService(ManagerDAO managerDAO, StaffService staffService, StoreService storeService) {
        this.managerDAO = managerDAO;
        this.staffService = staffService;
        this.storeService = storeService;
    }

    // Create a new manager record
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager manager = new Manager();
        BeanUtils.copyProperties(managerDTO, manager);
        managerDAO.save(manager);
        return managerDTO;
    }

    // Retrieve all managers
    public List<ManagerDTO> getAllManagers() {
        List<Manager> managerList = managerDAO.getAllManagers();
        return managerList.stream().map(manager -> {
            ManagerDTO dto = new ManagerDTO();
            BeanUtils.copyProperties(manager, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Delete a manager by their ID
    public void deleteManager(Integer managerId) {
        managerDAO.delete(managerId);
    }

    // Link a manager to their store and vice versa
    public SetStoreManagerDTO linkManagerAndStore(Integer managerId, Integer storeId) {
        StoreDTO storeDTO;
        try{
            Manager manager = managerDAO.getManagerById(managerId);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("Invalid manager ID");
        }
        try{
            storeDTO = storeService.getStoreDTOById(storeId);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("Invalid store ID");
        }
        // link the manager to the store
        StaffDTO managerStaff = staffService.getStaffById(managerId);
        managerStaff.setStoreId(storeId);
        staffService.updateStaff(managerStaff);
        // link the store to the manager
        storeDTO.setManagerId(managerId);
        storeService.updateStore(storeDTO);
        SetStoreManagerDTO setStoreManagerDTO = new SetStoreManagerDTO();
        setStoreManagerDTO.setManagerId(managerId);
        setStoreManagerDTO.setStoreId(storeId);
        return setStoreManagerDTO;
    }
}