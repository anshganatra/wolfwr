package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberDTO {

    @Schema(description = "Unique ID of the member", example = "1")
    private Integer memberId;

    @Schema(description = "First name of the member", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fname;

    @Schema(description = "Last name of the member", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lname;

    @Schema(description = "Phone number of the member", example = "123-456-7890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "Email ID of the member", example = "john.doe@random.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Address of the member", example = "12 Oak St, Raleigh, NC, 23608", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(description = "Date of birth of the member", example = "18 June 1998", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dob;

    @Schema(description = "Date of purchasing wolfwr membership", example = "12 February 2024", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate doj;

    @Schema(description = "Current level of membership", example = "Platinum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String memberLevel;

    @Schema(description = "Current status of membership", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean activeStatus;

    @Schema(description = "Expiration date of membership", example = "12 February 2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate membershipExpiration;

    @Schema(description = "ID of registration staff who processed the membership", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer staffId;

    @Schema(description = "ID of registration store where the member signed up", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer registrationStoreId;
}
