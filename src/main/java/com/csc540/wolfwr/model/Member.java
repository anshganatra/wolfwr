package com.csc540.wolfwr.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Member {
    private Integer memberId;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String address;
    private LocalDate dob;
    private LocalDate doj;
    private String memberLevel;
    private boolean activeStatus;
    private LocalDate membershipExpiration;
    private Integer staffId;
}
