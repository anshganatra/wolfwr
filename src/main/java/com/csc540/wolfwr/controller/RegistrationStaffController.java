package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.RegistrationStaffDTO;
import com.csc540.wolfwr.service.RegistrationStaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Registration Staff API", description = "CRUD operations for registration staff")
@RestController
@RequestMapping("/registration-staff")
public class RegistrationStaffController {

    private final RegistrationStaffService service;

    public RegistrationStaffController(RegistrationStaffService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RegistrationStaffDTO> create(@Valid @RequestBody RegistrationStaffDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<RegistrationStaffDTO> getById(@PathVariable Integer staffId) {
        return ResponseEntity.ok(service.getById(staffId));
    }

    @GetMapping
    public ResponseEntity<List<RegistrationStaffDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> delete(@PathVariable Integer staffId) {
        service.delete(staffId);
        return ResponseEntity.noContent().build();
    }
}
