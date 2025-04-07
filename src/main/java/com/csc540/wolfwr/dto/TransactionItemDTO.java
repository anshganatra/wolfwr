package com.csc540.wolfwr.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionItemDTO {
    @Schema(description = "Transaction ID to which the TransactionItem is associated", example = "1")
    @NotNull(message = "Transaction ID is required")
    private Integer transactionId;

    @Schema(description = "Store ID where the staff is assigned", example = "1")
    @NotNull(message = "Product Batch ID is required")
    private Integer productBatchId;

    @Schema(description = "Price of the collection of the item", example = "12.50", required = true)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, 
                message = "Price must be greater than or equal to zero")
    private BigDecimal price;

    @Schema(description = "Discounted price of the collection of the item", example = "10.00", 
            required = true)
    @NotNull(message = "Discounted Price is required")
    @DecimalMin(value = "0.0", inclusive = false, 
                message = "Discounted price must be greater than or equal to zero")
    private BigDecimal discountedPrice;

    @Schema(description = "Quantity (must be > 0)", example = "1", required = true)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;
}
