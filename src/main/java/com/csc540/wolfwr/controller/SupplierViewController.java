package com.csc540.wolfwr.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Supplier View", description = "All operations performed by suppliers")
@RestController
@RequestMapping("/supplier")
public class SupplierViewController {
}
