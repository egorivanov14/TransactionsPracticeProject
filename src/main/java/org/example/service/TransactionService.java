package org.example.service;

import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    void addTransaction(TransactionRequest request);

    void deleteTransaction(Long id);

    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getAllByCategory(String category);

    TransactionResponse getById(Long id);

    List<TransactionResponse> getAllByAmount(Long amount);

    List<TransactionResponse> getAllByCreatedAt(LocalDate createdAt);

}
