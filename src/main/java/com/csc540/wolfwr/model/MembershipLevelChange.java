package com.csc540.wolfwr.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MembershipLevelChange {
    private Integer memberId;
    private String levelName;
    private LocalDate levelChangeDate;
}
