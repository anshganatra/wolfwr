package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDTO {

    @Schema(description = "Unique identifier of the store", example = "1")
    private Integer storeId;

    @Schema(description = "Phone number of the store", example = "123-456-7890", required = true)
    @NotBlank(message = "Phone is required")
    private String phone;

    @Schema(description = "Address of the store", example = "123 Main St", required = true)
    @NotBlank(message = "Address is required")
    private String address;

    @Schema(description = "Indicates if the store is active", example = "true", required = true)
    @NotNull(message = "Store active status is required")
    private Boolean isActive;

    @Schema(description = "Manager ID for the store", example = "101")
    private Integer managerId;
}