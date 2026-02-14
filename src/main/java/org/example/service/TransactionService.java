package org.example.service;

import org.example.dto.transaction.TransactionRequest;
import org.example.dto.transaction.TransactionResponse;
import org.example.dto.transaction.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    void addTransaction(TransactionRequest request, String email);

    void deleteTransaction(Long id, String email);

    Page<TransactionResponse> getAllTransactionsByUser(String email, Pageable pageable);

    Page<TransactionResponse> getAllByBudgetIdAndUser(Long budgetId, String email, Pageable pageable);

    TransactionResponse getById(Long id, String email);

    Long getSumByType(String email, Long budgetId, Type type);

}
