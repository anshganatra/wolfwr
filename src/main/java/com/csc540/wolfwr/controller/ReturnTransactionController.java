package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.ReturnTransactionDTO;
import com.csc540.wolfwr.service.ReturnTransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Return Transaction API", description = "CRUD operations for return transactions")
@RestController
@RequestMapping("/return-transactions")
public class ReturnTransactionController {

    private final ReturnTransactionService service;

    public ReturnTransactionController(ReturnTransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReturnTransactionDTO> create(@Valid @RequestBody ReturnTransactionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ReturnTransactionDTO> getById(@PathVariable Integer transactionId) {
        return ResponseEntity.ok(service.getById(transactionId));
    }

    @GetMapping
    public ResponseEntity<List<ReturnTransactionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<ReturnTransactionDTO> update(@PathVariable Integer transactionId,
                                                       @Valid @RequestBody ReturnTransactionDTO dto) {
        dto.setTransactionId(transactionId);
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> delete(@PathVariable Integer transactionId) {
        service.delete(transactionId);
        return ResponseEntity.noContent().build();
    }
}
