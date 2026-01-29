package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.entity.Budget;
import org.example.entity.Transaction;
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
import java.util.Optional;

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

            if(transactionRepository.sumAmountByBudgetId(budget.getId(), email) + transaction.getAmount()
                    > budget.getLimitAmount()){

                throw new ExceedingBudgetException("The cost is more then the limit.");
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

        Optional<Transaction> transactionOptional = transactionRepository.findById(id);

        if(transactionOptional.isPresent()){
            Transaction transaction = transactionOptional.get();

            if(transaction.getUser().getEmail().equals(email)){
                transactionRepository.deleteById(id);
            }
            else {
                throw new AccessDeniedException("This transaction not for this user.");
            }
        }
        else {
            throw new ResourceNotFoundException("No transaction with this id.");
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
    public List<TransactionResponse> getAllByBudgetIdAndCategoryAndUser(
            Long budgetId, String category, String email) {

        List<Transaction> transactions = transactionRepository.findAllByBudgetIdAndCategoryAndUser(budgetId, category, email);

        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getByIdAndUser(Long id, String email) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

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
