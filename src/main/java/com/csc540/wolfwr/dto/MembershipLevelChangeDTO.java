package com.csc540.wolfwr.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembershipLevelChangeDTO {
    @Schema(description = "Member ID for the Member making a level change", example = "10", 
            required = true)
    @NotNull(message = "Member ID is required")
    private Integer memberId;

    @Schema(description = "Membership level name", example = "Gold", required = true)
    @NotBlank(message = "Membership level name is required")
    private String levelName;

    @Schema(description = "Level Change date", example = "2025-03-05", required = true)
    @NotNull(message = "Level change date is required")
    private LocalDate levelChangeDate;
}
