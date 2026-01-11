package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionRequest request) {
        transactionService.addTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<List<TransactionResponse>> getAllByAccount(@PathVariable String account) {
        List<TransactionResponse> transactions = transactionService.getAllByAccount(account);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/amount/{amount}")
    public ResponseEntity<List<TransactionResponse>> getAllByAmount(@PathVariable Long amount){
        List<TransactionResponse> transactions = transactionService.getAllByAmount(amount);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/createdAt/{createdAt}")
    public ResponseEntity<List<TransactionResponse>> getAllByCreatedAt(@PathVariable LocalDate createdAt){
        List<TransactionResponse> transactions = transactionService.getAllByCreatedAt(createdAt);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponse>> getAllByCategory(@PathVariable String category){
        List<TransactionResponse> transactions = transactionService.getAllByCategory(category);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/budgetId/{budgetId}")
    public ResponseEntity<List<TransactionResponse>> getAllByBudgetId(@PathVariable Long budgetId){
        return ResponseEntity.ok(transactionService.getAllByBudgetId(budgetId));
    }

    @GetMapping("/budgetId/{budgetId}/category/{category}")
    public ResponseEntity<List<TransactionResponse>> getAllByBudgetIdAndCategory(@PathVariable Long budgetId, @PathVariable String category){
        return ResponseEntity.ok(transactionService.getAllByBudgetIdAndCategory(budgetId, category));
    }
}