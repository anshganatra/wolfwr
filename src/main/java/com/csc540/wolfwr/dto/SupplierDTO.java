package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierDTO {

    @Schema(description = "Unique identifier of the supplier", example = "1")
    private Integer supplierId;

    @Schema(description = "Supplier name", example = "Acme Corp", required = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Phone number", example = "555-123-4567", required = true)
    @NotBlank(message = "Phone is required")
    private String phone;

    @Schema(description = "Email address", example = "supplier@example.com", required = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Address", example = "123 Industrial Ave", required = true)
    @NotBlank(message = "Address is required")
    private String address;
}
