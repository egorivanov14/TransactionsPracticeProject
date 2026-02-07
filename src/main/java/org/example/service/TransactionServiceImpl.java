package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.entity.Budget;
import org.example.entity.Transaction;
import org.example.dto.Type;
import org.example.exception.AccessDeniedException;
import org.example.exception.ExceedingBudgetException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.TransactionMapper;
import org.example.repository.BudgetRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
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
    private final UserRepository userRepository;


    @Transactional
    @Override
    public void addTransaction(TransactionRequest request, String email) {

        Budget budget = budgetRepository.findById(request.getBudgetId())
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if(budget.getUser().getEmail().equals(email)){
            Transaction transaction = transactionMapper.toEntity(request);

            if(transaction.getType().equals(Type.EXPENDITURE)){

                if(budget.getInitialAmount()
                        + transactionRepository.sumByType(budget.getId(), Type.INCOME, email)
                        - transactionRepository.sumByType(budget.getId(), Type.EXPENDITURE, email)
                        - transaction.getAmount()
                        < 0){

                    throw new ExceedingBudgetException("Not enough funds");
                }
            }

            transaction.setBudget(budget);
            transaction.setUser(userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found")));

            transactionRepository.save(transaction);
        }
        else {
            throw new AccessDeniedException("Not your budget.");
        }
    }

    @Transactional
    @Override
    public void deleteTransaction(Long id, String email) {

        Transaction transaction = transactionRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("No transaction with this id."));

        if (transaction.getUser().getEmail().equals(email)) {
            transactionRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("This transaction not for this user.");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllTransactionsByUser(String email) {
        List<Transaction> transactions = transactionRepository.findAllByUser(email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByAccountAndUser(String account, String email) {

        List<Transaction> transactions = transactionRepository.findAllByAccountAndUser(account, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCategoryAndUser(String category, String email) {

        List<Transaction> transactions = transactionRepository.findAllByCategoryAndUser(category, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByBudgetIdAndUser(Long budgetId, String email) {

        List<Transaction> transactions = transactionRepository.findAllByBudgetIdAndUser(budgetId, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByBudgetIdAndCategory(
            Long budgetId, String category, String email) {

        List<Transaction> transactions = transactionRepository
                .findAllByBudgetIdAndCategoryAndUser(budgetId, category, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(Long id, String email) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

        if(transaction.getUser().getEmail().equals(email)){
            return transactionMapper.toResponse(transaction);
        }
        else {
            throw new AccessDeniedException("Not your transaction");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByAmountAndUser(Long amount, String email) {

        List<Transaction> transactions = transactionRepository.findAllByAmountAndUser(amount, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCreatedAtAndUser(LocalDate createdAt, String email) {

        List<Transaction> transactions = transactionRepository.findAllByCreatedAtAndUser(createdAt, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }
}
