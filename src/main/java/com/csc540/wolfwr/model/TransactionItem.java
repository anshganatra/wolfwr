package com.csc540.wolfwr.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionItem {
    private Integer transactionId;
    private Integer productBatchId;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer quantity;
}
