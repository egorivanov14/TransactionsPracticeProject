package org.example.service;

import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    void addTransaction(TransactionRequest request, String email);

    void deleteTransaction(Long id, String email);

    Page<TransactionResponse> getAllTransactionsByUser(String email, Pageable pageable);

//    List<TransactionResponse> getAllByAccountAndUser(String account, String email);

//    List<TransactionResponse> getAllByCategoryAndUser(String category, String email);

    Page<TransactionResponse> getAllByBudgetIdAndUser(Long budgetId, String email, Pageable pageable);

//    List<TransactionResponse> getAllByBudgetIdAndCategory(Long budgetId, String category, String email);

    TransactionResponse getById(Long id, String email);

//    List<TransactionResponse> getAllByAmountAndUser(Long amount, String email);

//    List<TransactionResponse> getAllByCreatedAtAndUser(LocalDate createdAt, String email);

}
