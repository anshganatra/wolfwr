package com.csc540.wolfwr.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Shipment {
    private Integer shipmentId; // shipment_ID auto-increment
    private Integer supplierId; // supplier_ID
    private Integer productId;  // product_ID
    private Integer storeId;    // store_ID
    private BigDecimal buyPrice; // buy_price, DECIMAL(8,2)
    private LocalDate productionDate; // production_date
    private LocalDate shipmentDate;   // shipment_date
    private LocalDate expDate;        // exp_date, nullable
    private Integer quantity;         // quantity > 0
}