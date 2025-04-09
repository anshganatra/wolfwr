package com.csc540.wolfwr.controller.admin;

import com.csc540.wolfwr.dto.DiscountDTO;
import com.csc540.wolfwr.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Discount API", description = "CRUD operations for discounts")
@RestController
@RequestMapping("/admin/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Operation(summary = "Create a new discount")
    @PostMapping
    public ResponseEntity<DiscountDTO> create(@RequestBody DiscountDTO dto) {
        return ResponseEntity.ok(discountService.create(dto));
    }

    @Operation(summary = "Get a discount by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(discountService.getById(id));
    }

    @Operation(summary = "Get all discounts")
    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAll() {
        return ResponseEntity.ok(discountService.getAll());
    }

    @Operation(summary = "Update a discount")
    @PutMapping("/{id}")
    public ResponseEntity<DiscountDTO> update(@PathVariable Integer id, @RequestBody DiscountDTO dto) {
        dto.setDiscountId(id);
        return ResponseEntity.ok(discountService.update(dto));
    }

    @Operation(summary = "Delete a discount")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
