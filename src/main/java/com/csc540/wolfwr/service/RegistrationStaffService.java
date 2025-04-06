package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.RegistrationStaffDAO;
import com.csc540.wolfwr.dto.RegistrationStaffDTO;
import com.csc540.wolfwr.model.RegistrationStaff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationStaffService {

    private final RegistrationStaffDAO dao;

    public RegistrationStaffService(RegistrationStaffDAO dao) {
        this.dao = dao;
    }

    public RegistrationStaffDTO create(RegistrationStaffDTO dto) {
        RegistrationStaff staff = new RegistrationStaff();
        BeanUtils.copyProperties(dto, staff);
        dao.save(staff);
        return dto;
    }

    public RegistrationStaffDTO getById(Integer staffId) {
        RegistrationStaff staff = dao.getById(staffId);
        RegistrationStaffDTO dto = new RegistrationStaffDTO();
        BeanUtils.copyProperties(staff, dto);
        return dto;
    }

    public List<RegistrationStaffDTO> getAll() {
        return dao.getAll().stream().map(staff -> {
            RegistrationStaffDTO dto = new RegistrationStaffDTO();
            BeanUtils.copyProperties(staff, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public void delete(Integer staffId) {
        dao.delete(staffId);
    }
}
