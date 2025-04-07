package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillingStaffDTO {

    @Schema(description = "ID of the staff member who is a billing staff", example = "101")
    @NotNull(message = "Staff ID is required")
    private Integer billingStaffId;
}
