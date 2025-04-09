package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReturnItemDTO {

    @Schema(description = "Original transaction ID from which the item is being returned", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer ogTid;

    @Schema(description = "Shipment ID associated with the returned item", example = "5678", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer shipmentId;

    @Schema(description = "Product ID (batch) being returned", example = "2001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer productId;

    @Schema(description = "Quantity of product being returned", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "Cashier ID who processed the return", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cashierId;
}
