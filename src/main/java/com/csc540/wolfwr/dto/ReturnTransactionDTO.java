package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnTransactionDTO {

    @Schema(description = "ID of the return transaction", example = "1001", required = true)
    @NotNull(message = "Transaction ID is required")
    private Integer transactionId;

    @Schema(description = "Referenced original transaction ID", example = "1000", required = true)
    @NotNull(message = "Reference transaction ID is required")
    private Integer referenceTransactionId;
}
