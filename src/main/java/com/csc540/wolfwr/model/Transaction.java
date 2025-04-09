package com.csc540.wolfwr.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private Integer transactionId;
    private Integer storeId;
    private BigDecimal totalPrice;
    private BigDecimal discountedTotalPrice;
    private LocalDateTime date;
    private String type;
    private Integer cashierId;
    private Integer memberId;
    private Boolean completedStatus;
}
