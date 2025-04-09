package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ShipmentDTO {

    @Schema(description = "Shipment identifier", example = "1")
    private Integer shipmentId;

    @Schema(description = "Supplier ID", example = "101", required = true)
    @NotNull(message = "Supplier ID is required")
    private Integer supplierId;

    @Schema(description = "Product ID", example = "205", required = true)
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @Schema(description = "Store ID", example = "10", required = true)
    @NotNull(message = "Store ID is required")
    private Integer storeId;

    @Schema(description = "Buy price", example = "12.50", required = true)
    @NotNull(message = "Buy price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Buy price must be greater than zero")
    private BigDecimal buyPrice;

    @Schema(description = "Production date", example = "2025-03-01", required = true)
    @NotNull(message = "Production date is required")
    private LocalDate productionDate;

    @Schema(description = "Shipment date", example = "2025-03-05", required = true)
    @NotNull(message = "Shipment date is required")
    private LocalDate shipmentDate;

    @Schema(description = "Expiration date (optional)", example = "2025-06-01")
    private LocalDate expDate;

    @Schema(description = "Quantity (must be > 0)", example = "100", required = true)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;

    @Schema(description = "Shipment status flag", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Shipment process status is required")
    private Boolean shipmentProcessed;
}