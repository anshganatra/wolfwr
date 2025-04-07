package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountDTO {

    @Schema(description = "Unique ID of the discount", example = "1")
    private Integer discountId;

    @NotNull
    @Schema(description = "Product ID associated with the discount", example = "1001")
    private Integer productId;

    @Schema(description = "Optional shipment ID associated with the discount", example = "2001")
    private Integer shipmentId;

    @NotNull
    @Schema(description = "Type of discount (Percentage or Dollar Value)", example = "Percentage")
    private String type;

    @NotNull
    @Schema(description = "Value of the discount", example = "20.00")
    private BigDecimal value;

    @NotNull
    @Schema(description = "Start date of the discount", example = "2025-04-01")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "End date of the discount", example = "2025-04-30")
    private LocalDate endDate;
}
