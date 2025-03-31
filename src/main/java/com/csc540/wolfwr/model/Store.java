package com.csc540.wolfwr.model;

import lombok.Data;

@Data
public class Store {
    private Integer storeId;
    private String phone;
    private String address;
    private Boolean isActive;
    private Integer managerId;
}