package com.csc540.wolfwr.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Inventory {
    private Integer storeId;      // References Stores(store_ID)
    private Integer shipmentId;   // Primary key, also used to reference a shipment
    private Integer productId;    // References Products(product_ID)
    private BigDecimal marketPrice; // DECIMAL(8,2)
    private Integer productQty;   // Must be >= 0
}