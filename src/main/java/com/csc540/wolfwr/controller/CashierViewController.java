package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.*;
import com.csc540.wolfwr.service.BillingService;
import com.csc540.wolfwr.service.CashierService;
import com.csc540.wolfwr.service.MemberService;
import com.csc540.wolfwr.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Tag(name = "Cashier View", description = "All operations performed by cashiers")
@RestController
@RequestMapping("/cashier")
public class CashierViewController {

    private final BillingService billingService;
    private final CashierService cashierService;
    private final StoreService storeService;
    private final MemberService memberService;

    public CashierViewController(BillingService billingService, CashierService cashierService, StoreService storeService, MemberService memberService) {
        this.billingService = billingService;
        this.cashierService = cashierService;
        this.storeService = storeService;
        this.memberService = memberService;
    }

    private Boolean validateRequest(BillRequestDTO billRequest) {
        // check if the cashierID is valid
        List<Integer> validCashierIds = cashierService.getAllCashiers().stream().map(CashierDTO::getCashierId).toList();
        if (!validCashierIds.contains(billRequest.getCashierId())) {
            System.out.println("cashier");
            return Boolean.FALSE;
        }
        List<Integer> validStoreIds = storeService.getAllStores().stream().filter(storeDTO -> Objects.equals(storeDTO.getIsActive(), true)).map(StoreDTO::getStoreId).toList();
        if (!validStoreIds.contains(billRequest.getStoreId())) {
            System.out.println("store");
            return Boolean.FALSE;
        }
        List<Integer> validMembers = memberService.getMembers().stream().filter(memberDTO -> Objects.equals(memberDTO.isActiveStatus(), true)).map(MemberDTO::getMemberId).toList();
        if (!validMembers.contains(billRequest.getMemberId())) {
            System.out.println(validMembers);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // generate a bill given the product information
    @PostMapping("/create-bill")
    @Operation(summary = "Create bill with line items", description = "Given info of all products, member and store, update DB with transaction records")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Transaction created succesfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    }
    )
    public ResponseEntity<BillResponseDTO> createBill(@RequestBody BillRequestDTO billRequest) {
        if (!validateRequest(billRequest)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(billingService.createBill(billRequest));
    }
}
