package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.dto.BudgetStatus;
import org.example.entity.Budget;
import org.example.dto.Type;
import org.example.entity.User;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.BudgetMapper;
import org.example.repository.BudgetRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addBudget(BudgetRequest request, String email) {

        Budget budget = budgetMapper.toEntity(request);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        budget.setUser(user);

        budgetRepository.save(budget);
        userRepository.save(user);

    }

    @Transactional
    @Override
    public void deleteBudgetById(Long id, String email) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if (budget.getUser().getEmail().equals(email)) {
            budgetRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("Not your budget.");
        }

    }

    @Transactional
    @Override
    public List<BudgetResponse> getAllBudgetsByUser(String email) {

        List<Budget> budgets = budgetRepository.findAllByUser(email);

        return budgets.stream().map(budgetMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public BudgetResponse getBudgetById(Long budgetId, String email) {

        Budget budget = budgetRepository.findByBudgetIdAndUser(budgetId, email)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id or it is not your budget."));

        return budgetMapper.toResponse(budget);
    }

    @Transactional
    @Override
    public Long getExpenditure(Long id, String email) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if (budget.getUser().getEmail().equals(email)) {
            return transactionRepository.sumByType(id, Type.EXPENDITURE, email);
        } else {
            throw new AccessDeniedException("Not your budget.");
        }
    }

    @Transactional
    @Override
    public void changeInitialAmount(Long budgetId, Long newInitialAmount, String email) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if(budget.getUser().getEmail().equals(email)){
            budget.setInitialAmount(newInitialAmount);
            budgetRepository.save(budget);
        }
        else {
            throw new AccessDeniedException("Not your budget.");
        }
    }

    @Transactional
    @Override
    public void changeAccount(Long budgetId, String newAccount, String email) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if(budget.getUser().getEmail().equals(email)){

            budget.setAccount(newAccount);
            budgetRepository.save(budget);
        }
        else {
            throw new AccessDeniedException("Not your budget.");
        }
    }

    @Transactional
    @Override
    public BudgetStatus getBudgetStatus(Long budgetId, String email) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        if(budget.getUser().getEmail().equals(email)){

            Long income = transactionRepository.sumByType(budgetId, Type.INCOME, email);

            Long expenditure = transactionRepository.sumByType(budgetId, Type.EXPENDITURE, email);

            Long remains = budget.getInitialAmount() + income - expenditure;

            return new BudgetStatus(
                    income,
                    expenditure,
                    remains
            );
        }
        else {
            throw new AccessDeniedException("Not your budget.");
        }
    }
}