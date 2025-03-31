package com.csc540.wolfwr.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberLevelDTO {
    @NotNull(message = "Level Name is required")
    @Size(max = 16, message = "Level Name must be maximum 16 characters")
    private String levelName;

    @NotNull(message = "Cashback rate is required")
    @Min((long) 0)
    @Max((long) 1.0000)
    private float cashbackRate;
}
