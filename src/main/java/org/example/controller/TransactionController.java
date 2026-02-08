package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<TransactionResponse>> getAllTransactionsByUser(@AuthenticationPrincipal
                                                                                  UserDetails userDetails,
                                                                              @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        String email = userDetails.getUsername();

        Page<TransactionResponse> transactions = transactionService.getAllTransactionsByUser(email, pageable);
        return ResponseEntity.ok(transactions);
    }

//    @GetMapping("/account/")
//    public ResponseEntity<List<TransactionResponse>> getAllByAccountAndUser(@RequestParam String account
//            , @AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//
//        List<TransactionResponse> transactions = transactionService.getAllByAccountAndUser(account, email);
//        return ResponseEntity.ok(transactions);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        TransactionResponse transaction = transactionService.getById(id, email);
        return ResponseEntity.ok(transaction);
    }

//    @GetMapping("/amount/")
//    public ResponseEntity<List<TransactionResponse>> getAllByAmountAndUser(@RequestParam Long amount
//            , @AuthenticationPrincipal UserDetails userDetails ){
//        String email = userDetails.getUsername();
//
//        List<TransactionResponse> transactions = transactionService.getAllByAmountAndUser(amount, email);
//
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping("/createdAt/")
//    public ResponseEntity<List<TransactionResponse>> getAllByCreatedAtAndUser(@RequestParam LocalDate createdAt
//            , @AuthenticationPrincipal UserDetails userDetails){
//        String email = userDetails.getUsername();
//
//        List<TransactionResponse> transactions = transactionService.getAllByCreatedAtAndUser(createdAt, email);
//
//        return ResponseEntity.ok(transactions);
//    }

//    @GetMapping("/category/")
//    public ResponseEntity<List<TransactionResponse>> getAllByCategoryAndUser(@RequestParam String category
//            , @AuthenticationPrincipal UserDetails userDetails){
//        String email = userDetails.getUsername();
//
//        List<TransactionResponse> transactions = transactionService.getAllByCategoryAndUser(category, email);
//
//        return ResponseEntity.ok(transactions);
//    }

    @GetMapping("/budgetId/{budgetId}")
    public ResponseEntity<Page<TransactionResponse>> getAllByBudgetId(@PathVariable Long budgetId,
                                                                      @AuthenticationPrincipal UserDetails userDetails,
                                                                      @PageableDefault(size = 10, sort = "id") Pageable pageable){
        String email = userDetails.getUsername();

        return ResponseEntity.ok(transactionService.getAllByBudgetIdAndUser(budgetId, email, pageable));
    }

//    @GetMapping("/budgetId/{budgetId}/category/")
//    public ResponseEntity<List<TransactionResponse>> getAllByBudgetIdAndCategoryAndUser(
//            @PathVariable Long budgetId,
//            @RequestParam String category,
//            @AuthenticationPrincipal UserDetails userDetails){
//        String email = userDetails.getUsername();
//
//        return ResponseEntity.ok(transactionService.getAllByBudgetIdAndCategory(budgetId, category, email));
//    }
}