package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillItemResponseDTO {

    @Schema(description = "Product name", example = "Wireless Earbuds Pro")
    private String productName;

    @Schema(description = "Product batch ID", example = "2001")
    private Integer productBatchId;

    @Schema(description = "Quantity purchased", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price of the product", example = "149.99")
    private BigDecimal unitPrice;

    @Schema(description = "Final price after applying discount (if any)", example = "299.98")
    private BigDecimal finalPrice;
}
