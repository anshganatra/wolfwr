package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.ManagerDAO;
import com.csc540.wolfwr.dto.ManagerDTO;
import com.csc540.wolfwr.model.Manager;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerDAO managerDAO;

    public ManagerService(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
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
}