package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RewardDTO {

    @Schema(description = "Member ID", example = "101", required = true)
    @NotNull(message = "Member ID is required")
    private Integer memberId;

    @Schema(description = "Year of rewards", example = "2024", required = true)
    @NotNull(message = "Year is required")
    private Integer year;

    @Schema(description = "Total rewards accumulated", example = "150.50", required = true)
    @NotNull(message = "Reward total is required")
    @Min(value = 0, message = "Reward total cannot be negative")
    private BigDecimal rewardTotal;
}
