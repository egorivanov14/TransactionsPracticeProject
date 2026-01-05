package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.entity.Budget;
import org.example.entity.Transaction;
import org.example.exception.ExceedingBudgetException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.TransactionMapper;
import org.example.repository.BudgetRepository;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BudgetRepository budgetRepository;


    @Transactional
    @Override
    public void addTransaction(TransactionRequest request) {

        Transaction transaction = transactionMapper.toEntity(request);

        Optional<Budget> budgetOptional = budgetRepository.findCurrentByCategory(transaction.getCategory());

        if(budgetOptional.isPresent()){

            Budget budget = budgetOptional.get();

            if(transactionRepository.sumAmountByBudgetId(budget.getId()) + transaction.getAmount() > budget.getLimitAmount()){
                throw new ExceedingBudgetException("The cost is more then the limit.");
            }

            budgetRepository.save(budget);
            transaction.setBudget(budget);
        }

        transactionRepository.save(transaction);

    }

    @Transactional
    @Override
    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

        transactionRepository.deleteById(id);

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("No transactions in database.");
        }
        else {
            return transactions.stream().map(transactionMapper::toResponse).toList();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCategory(String category) {
        List<Transaction> transactions = transactionRepository.findAllByCategory(category);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this category.");
        }
        else {
            return transactions.stream().map(transactionMapper::toResponse).toList();
        }

    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    @Override
    public List<TransactionResponse> getAllByAmount(Long amount) {

        List<Transaction> transactions = transactionRepository.findAllByAmount(amount);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this amount.");
        }
        else {
            return transactions.stream().map(transactionMapper::toResponse).toList();
        }

    }

    @Transactional
    @Override
    public List<TransactionResponse> getAllByCreatedAt(LocalDate createdAt) {

        List<Transaction> transactions = transactionRepository.findAllByCreatedAt(createdAt);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this date.");
        }
        else {
            return transactions.stream().map(transactionMapper::toResponse).toList();
        }
    }
}
