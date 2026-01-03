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

    /**
     * POST /api/transactions
     * Создание новой транзакции
     * В теле запроса передается TransactionRequest с amount и category
     * Возвращает статус 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionRequest request) {
        transactionService.addTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * DELETE /api/transactions/{id}
     * Удаление транзакции по ID
     * Возвращает статус 204 (No Content) при успехе
     * Возвращает 404 (Not Found) если транзакция не существует
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/transactions
     * Получение всех транзакций
     * Возвращает список TransactionResponse со статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/category/{category}
     * Получение транзакций по категории
     * {category} передается в URL (например, /category/Еда)
     * Возвращает список TransactionResponse или пустой список []
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponse>> getAllByCategory(@PathVariable String category) {
        List<TransactionResponse> transactions = transactionService.getAllByCategory(category);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/{id}
     * Получение одной транзакции по ID
     * Возвращает TransactionResponse со статусом 200 (OK)
     * Возвращает 404 (Not Found) если транзакция не существует
     */
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
}