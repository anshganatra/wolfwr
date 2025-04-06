package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.DiscountDAO;
import com.csc540.wolfwr.dto.DiscountDTO;
import com.csc540.wolfwr.model.Discount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountDAO discountDAO;

    public DiscountService(DiscountDAO discountDAO) {
        this.discountDAO = discountDAO;
    }

    public DiscountDTO create(DiscountDTO dto) {
        Discount discount = new Discount();
        BeanUtils.copyProperties(dto, discount);
        discountDAO.save(discount);
        return dto;
    }

    public DiscountDTO getById(Integer id) {
        Discount discount = discountDAO.getById(id);
        DiscountDTO dto = new DiscountDTO();
        BeanUtils.copyProperties(discount, dto);
        return dto;
    }

    public List<DiscountDTO> getAll() {
        return discountDAO.getAll().stream().map(d -> {
            DiscountDTO dto = new DiscountDTO();
            BeanUtils.copyProperties(d, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public DiscountDTO update(DiscountDTO dto) {
        Discount discount = new Discount();
        BeanUtils.copyProperties(dto, discount);
        discountDAO.update(discount);
        return dto;
    }

    public void delete(Integer id) {
        discountDAO.delete(id);
    }
}
