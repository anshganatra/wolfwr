package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillRequestDTO {

    @Schema(description = "Store ID where the transaction occurred", example = "1")
    private Integer storeId;

    @Schema(description = "Cashier handling the transaction", example = "101")
    private Integer cashierId;

    @Schema(description = "Member/customer ID (nullable for guest)", example = "1001")
    private Integer memberId;

    @Schema(description = "Date of the transaction", example = "2025-04-09T10:15:00")
    private LocalDateTime date;

    @Schema(description = "List of items being purchased")
    private List<BillItemDTO> items;
}
