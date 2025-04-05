package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDTO {

    @Schema(description = "Unique ID of the product", example = "1")
    private Integer productId;

    @Schema(description = "Product name", example = "Kirkland Signature Imported Basil Pesto", required = true)
    @NotBlank(message = "Product name is required")
    private String productName;
}
