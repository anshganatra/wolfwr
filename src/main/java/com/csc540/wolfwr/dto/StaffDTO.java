package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffDTO {

    @Schema(description = "Unique identifier of the staff member", example = "1")
    private Integer staffId;

    @Schema(description = "First name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    private String fname;

    @Schema(description = "Last name", example = "Doe", required = true)
    @NotBlank(message = "Last name is required")
    private String lname;

    @Schema(description = "Title (e.g., 'Registration Staff', 'Billing Staff', etc.)", example = "Store Manager", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Address", example = "123 Main St", required = true)
    @NotBlank(message = "Address is required")
    private String address;

    @Schema(description = "Phone number", example = "123-456-7890", required = true)
    @NotBlank(message = "Phone is required")
    private String phone;

    @Schema(description = "Email address", example = "john.doe@example.com", required = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Date of birth", example = "1990-01-01", required = true)
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @Schema(description = "Date of joining", example = "2025-03-30", required = true)
    @NotNull(message = "Date of joining is required")
    private LocalDate doj;

    @Schema(description = "Store ID where the staff is assigned", example = "101")
    private Integer storeId;
}