package com.csc540.wolfwr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WarehouseStaffDTO {
    @Schema(description = "Unique identifier of a Warehouse Staff member (reference staff id)",
            example = "1")
    private Integer warehouseStaffId;
}
