package com.csc540.wolfwr.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Discount {
    private Integer discountId;
    private Integer productId;
    private Integer shipmentId;
    private String type; // ENUM('Percentage', 'Dollar Value')
    private BigDecimal value;
    private LocalDate startDate;
    private LocalDate endDate;
}
