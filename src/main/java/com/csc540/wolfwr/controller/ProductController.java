package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.ProductDTO;
import com.csc540.wolfwr.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product API", description = "CRUD operations for Products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new Product", description = "Creates a new product given the product name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created succesfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO newProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(newProduct);
    }

    @Operation(summary = "Get info of a specific product", description = "Returns details of a product given its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product returned succesfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Get info of all available products", description = "Returns details of all products present in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products returned succesfully"),
            @ApiResponse(responseCode = "404", description = "No product found")
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
