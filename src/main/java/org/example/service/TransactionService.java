package org.example.service;

import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    void addTransaction(TransactionRequest request, String email);

    void deleteTransaction(Long id, String email);

    List<TransactionResponse> getAllTransactionsByUser(String email);

    List<TransactionResponse> getAllByAccountAndUser(String account, String email);

    List<TransactionResponse> getAllByCategoryAndUser(String category, String email);

    List<TransactionResponse> getAllByBudgetIdAndUser(Long budgetId, String email);

    List<TransactionResponse> getAllByBudgetIdAndCategory(Long budgetId, String category, String email);

    TransactionResponse getById(Long id, String email);

    List<TransactionResponse> getAllByAmountAndUser(Long amount, String email);

    List<TransactionResponse> getAllByCreatedAtAndUser(LocalDate createdAt, String email);

}
