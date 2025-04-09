package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.BillRequestDTO;
import com.csc540.wolfwr.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cashier View", description = "All operations performed by cashiers")
@RestController
@RequestMapping("/cashier")
public class CashierViewController {

    private final BillingService billingService;

    public CashierViewController(BillingService billingService) {
        this.billingService = billingService;
    }

    // generate a bill given the product information
    @PostMapping("create-bill")
    @Operation(summary = "Create bill with line items", description = "Given info of all products, member and store, update DB with transaction records")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Transaction created succesfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    }
    )
    public ResponseEntity<Integer> createBill(@RequestBody BillRequestDTO billRequest) {
        return ResponseEntity.ok(billingService.createBill(billRequest));
    }
}
