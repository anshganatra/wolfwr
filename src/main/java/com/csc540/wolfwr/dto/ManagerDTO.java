package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ManagerDTO {

    @Schema(description = "Unique identifier of the manager, referencing the staff member's ID", example = "1")
    private Integer managerId;
}