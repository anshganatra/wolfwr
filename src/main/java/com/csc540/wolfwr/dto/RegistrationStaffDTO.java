package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegistrationStaffDTO {

    @Schema(description = "Unique identifier of the registration staff member", example = "1")
    private Integer registrationStaffId;
}
