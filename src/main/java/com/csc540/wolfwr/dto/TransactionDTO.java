package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    @Schema(description = "Unique identifier of the transaction", example = "1")
    private Integer transactionId;

    @Schema(description = "The store where the transaction occurred", example = "1")
    private Integer storeId;

    @Schema(description = "Total price for the transaction", example = "250.75")
    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @Schema(description = "Discounted Total price for the transaction", example = "230")
    @NotNull(message = "Discounted Total price is required")
    private BigDecimal discountedTotalPrice;

    @Schema(description = "Date and time of the transaction", example = "2025-04-05T13:30:00")
    private LocalDateTime date;

    @Schema(description = "Type of the transaction (e.g., sale, refund)", example = "sale")
    @NotNull(message = "Transaction type is required")
    private String type;

    @Schema(description = "ID of the cashier who handled the transaction", example = "101")
    private Integer cashierId;

    @Schema(description = "ID of the member involved in the transaction", example = "1001")
    private Integer memberId;

    @Schema(description = "Indicates if the transaction is completed", example = "false")
    private Boolean completedStatus;
}
