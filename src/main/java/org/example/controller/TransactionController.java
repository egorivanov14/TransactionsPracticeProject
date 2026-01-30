package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionRequest request
            , @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        transactionService.addTransaction(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        transactionService.deleteTransaction(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactionsByUser(@AuthenticationPrincipal
                                                                                  UserDetails userDetails) {
        String email = userDetails.getUsername();

        List<TransactionResponse> transactions = transactionService.getAllTransactionsByUser(email);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/")
    public ResponseEntity<List<TransactionResponse>> getAllByAccountAndUser(@PathVariable String account
            , @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        List<TransactionResponse> transactions = transactionService.getAllByAccountAndUser(account, email);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        TransactionResponse transaction = transactionService.getByIdAndUser(id, email);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/amount/")
    public ResponseEntity<List<TransactionResponse>> getAllByAmountAndUser(@PathVariable Long amount
            , @AuthenticationPrincipal UserDetails userDetails ){
        String email = userDetails.getUsername();

        List<TransactionResponse> transactions = transactionService.getAllByAmountAndUser(amount, email);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/createdAt/")
    public ResponseEntity<List<TransactionResponse>> getAllByCreatedAtAndUser(@PathVariable LocalDate createdAt
            , @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        List<TransactionResponse> transactions = transactionService.getAllByCreatedAtAndUser(createdAt, email);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/category/")
    public ResponseEntity<List<TransactionResponse>> getAllByCategoryAndUser(@PathVariable String category
            , @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        List<TransactionResponse> transactions = transactionService.getAllByCategoryAndUser(category, email);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/budgetId/{budgetId}")
    public ResponseEntity<List<TransactionResponse>> getAllByBudgetId(@PathVariable Long budgetId,
                                                                      @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        return ResponseEntity.ok(transactionService.getAllByBudgetIdAndUser(budgetId, email));
    }

    @GetMapping("/budgetId/{budgetId}/category/")
    public ResponseEntity<List<TransactionResponse>> getAllByBudgetIdAndCategoryAndUser(
            @PathVariable Long budgetId,
            @PathVariable String category,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        return ResponseEntity.ok(transactionService.getAllByBudgetIdAndCategoryAndUser(budgetId, category, email));
    }
}