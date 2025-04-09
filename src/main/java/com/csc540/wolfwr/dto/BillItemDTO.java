package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillItemDTO {

    @Schema(description = "Product batch ID", example = "2001")
    private Integer productBatchId;

    @Schema(description = "Quantity purchased", example = "2")
    private Integer quantity;
}
