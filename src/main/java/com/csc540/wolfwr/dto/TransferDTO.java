package com.csc540.wolfwr.dto;

import lombok.Data;

@Data
public class TransferDTO {
    private Integer sourceStoreId;
    private Integer destinationStoreId;
    private Integer productQty;
    private Integer productId;
    private Integer shipmentId;
}
