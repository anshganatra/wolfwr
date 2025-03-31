package com.csc540.wolfwr.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Staff {
    private Integer staffId; // maps to staff_ID (auto-increment)
    private String fname;
    private String lname;
    private String title; // ENUM in DB but represented as String here
    private String address;
    private String phone;
    private String email;
    private LocalDate dob;
    private LocalDate doj;
    private Integer storeId; // foreign key from Stores table
}