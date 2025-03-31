package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryDTO {

    @Schema(description = "Store ID where the inventory is located", example = "10", required = true)
    @NotNull(message = "Store ID is required")
    private Integer storeId;

    @Schema(description = "Shipment ID (primary key)", example = "1001", required = true)
    @NotNull(message = "Shipment ID is required")
    private Integer shipmentId;

    @Schema(description = "Product ID in the inventory", example = "205", required = true)
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @Schema(description = "Market price of the product", example = "15.99", required = true)
    @NotNull(message = "Market price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Market price must be at least 0")
    private BigDecimal marketPrice;

    @Schema(description = "Quantity of the product in inventory (must be >= 0)", example = "50", required = true)
    @NotNull(message = "Product quantity is required")
    @Min(value = 0, message = "Product quantity must be greater than or equal to 0")
    private Integer productQty;
}