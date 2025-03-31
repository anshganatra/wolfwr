package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.StoreDAO;
import com.csc540.wolfwr.dto.StoreDTO;
import com.csc540.wolfwr.model.Store;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreDAO storeDAO;

    public StoreService(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    // Create a new store
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = new Store();
        BeanUtils.copyProperties(storeDTO, store);
        storeDAO.save(store);
        return storeDTO;
    }

    // Retrieve a store by its ID (model)
    public Store getStoreById(Integer storeId) {
        return storeDAO.getStoreById(storeId);
    }

    // Retrieve a store by its ID as a DTO
    public StoreDTO getStoreDTOById(Integer storeId) {
        Store store = storeDAO.getStoreById(storeId);
        StoreDTO dto = new StoreDTO();
        BeanUtils.copyProperties(store, dto);
        return dto;
    }

    // Retrieve all stores as DTOs
    public List<StoreDTO> getAllStores() {
        List<Store> stores = storeDAO.getAllStores();
        return stores.stream().map(store -> {
            StoreDTO dto = new StoreDTO();
            BeanUtils.copyProperties(store, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Update an existing store
    public StoreDTO updateStore(StoreDTO storeDTO) {
        Store store = new Store();
        BeanUtils.copyProperties(storeDTO, store);
        storeDAO.update(store);
        return storeDTO;
    }

    // Delete a store by ID
    public void deleteStore(Integer storeId) {
        storeDAO.delete(storeId);
    }
}