package com.csc540.wolfwr.model;

import lombok.Data;

@Data
public class Supplier {
    private Integer supplierId;
    private String name;
    private String phone;
    private String email;
    private String address;
}
