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

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BudgetRepository budgetRepository;


    @Transactional
    @Override
    public void addTransaction(TransactionRequest request) {

        Budget budget = budgetRepository.findById(request.getBudgetId())
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setAccount(budget.getAccount());

        if(transactionRepository.sumAmountByBudgetId(budget.getId()) + transaction.getAmount() > budget.getLimitAmount()){

            throw new ExceedingBudgetException("The cost is more then the limit.");
        }

        transaction.setBudget(budget);

        transactionRepository.save(transaction);

    }

    @Transactional
    @Override
    public void deleteTransaction(Long id) {

        if(transactionRepository.existsById(id)){
            transactionRepository.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("No transaction with this id.");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByAccount(String account) {
        List<Transaction> transactions = transactionRepository.findAllByAccount(account);

        return transactions.stream().map(transactionMapper::toResponse).toList();

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCategory(String category) {

        List<Transaction> transactions = transactionRepository.findAllByCategory(category);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByBudgetId(Long budgetId) {

        List<Transaction> transactions = transactionRepository.findAllByBudgetId(budgetId);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }


    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByBudgetIdAndCategory(Long budgetId, String category) {

        List<Transaction> transactions = transactionRepository.findAllByBudgetIdAndCategory(budgetId, category);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

        return transactionMapper.toResponse(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByAmount(Long amount) {

        List<Transaction> transactions = transactionRepository.findAllByAmount(amount);

        return transactions.stream().map(transactionMapper::toResponse).toList();

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCreatedAt(LocalDate createdAt) {

        List<Transaction> transactions = transactionRepository.findAllByCreatedAt(createdAt);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }
}
