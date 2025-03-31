package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CashierDTO {

    @Schema(description = "Unique identifier of the cashier (references the staff member's ID)", example = "1")
    private Integer cashierId;
}