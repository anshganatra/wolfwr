package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.*;
import com.csc540.wolfwr.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Cashier View", description = "All operations performed by cashiers")
@RestController
@RequestMapping("/cashier")
public class CashierViewController {

    private final BillingService billingService;
    private final CashierService cashierService;
    private final StoreService storeService;
    private final MemberService memberService;
    private final TransactionReportService transactionReportService;


    public CashierViewController(BillingService billingService, CashierService cashierService, StoreService storeService, MemberService memberService, TransactionReportService transactionReportService) {
        this.billingService = billingService;
        this.cashierService = cashierService;
        this.storeService = storeService;
        this.memberService = memberService;
        this.transactionReportService = transactionReportService;
    }

    private Boolean validateRequest(BillRequestDTO billRequest) {
        // check if the cashierID is valid
        List<Integer> validCashierIds = cashierService.getAllCashiers().stream().map(CashierDTO::getCashierId).toList();
        if (!validCashierIds.contains(billRequest.getCashierId())) {
            throw new IllegalArgumentException("Cashier ID is invalid");
        }
        List<Integer> validStoreIds = storeService.getAllStores().stream().filter(storeDTO -> Objects.equals(storeDTO.getIsActive(), true)).map(StoreDTO::getStoreId).toList();
        if (!validStoreIds.contains(billRequest.getStoreId())) {
            throw new IllegalArgumentException("Store ID is invalid");
        }
        List<Integer> validMembers = memberService.getMembers().stream().filter(memberDTO -> Objects.equals(memberDTO.isActiveStatus(), true)).map(MemberDTO::getMemberId).toList();
        if (!validMembers.contains(billRequest.getMemberId())) {
            throw new IllegalArgumentException(("Member ID is invalid"));
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

    // Exception handler for IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Return 400 BadRequest with exception message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Retrieves detailed transactions for a given member between the specified start and end date.
     * Each transaction returned will include its associated transaction items enriched with product names.
     *
     * @param memberId the member's id
     * @param startDate the beginning date/time of the period (ISO date-time format)
     * @param endDate the end date/time of the period (optional, defaults to now)
     * @return a list of transactions (as maps), each with a nested "items" list
     */
    @Operation(
            summary = "Get member detailed transactions",
            description = "Returns all transactions for a member between startDate and endDate, enriched with transaction items and product names."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/member-detailed-transactions")
    public ResponseEntity<List<Map<String, Object>>> getMemberDetailedTransactions(
            @RequestParam Integer memberId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate

//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<Map<String, Object>> detailedTransactions =
                transactionReportService.getEnrichedTransactionsByMemberAndDate(memberId, startDate, endDate);
        return ResponseEntity.ok(detailedTransactions);
    }
}
