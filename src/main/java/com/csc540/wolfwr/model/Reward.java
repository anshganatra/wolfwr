package com.csc540.wolfwr.model;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Reward {
    private Integer memberId;
    private Integer year;
    private BigDecimal rewardTotal;
}