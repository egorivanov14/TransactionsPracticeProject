package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.entity.Budget;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.WrongDataException;
import org.example.mapper.BudgetMapper;
import org.example.repository.BudgetRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addBudget(BudgetRequest request) {

//        if (budgetRepository.existsCurrentByAccount(request.getAccount()) ||
//                budgetRepository.existsByAccountAndDate(request.getAccount(), request.getStartDate())) {
//            throw new DuplicateResourceException("Budget with this account already exists.");
//        } else {
//            Budget budget = budgetMapper.toEntity(request);
//
//            if (budget.getEndDate() != null && budget.getEndDate().isBefore(budget.getStartDate())) {
//                throw new WrongDataException("EndDate must be after startDate.");
//            }
//
//            budgetRepository.save(budget);
//
//        }

        Budget budget = budgetMapper.toEntity(request);

        budget.setUser(userRepository.findById(request.getUserId()).
                orElseThrow(() -> new ResourceNotFoundException("No user with this id.")));

        budgetRepository.save(budget);

    }

    @Transactional
    @Override
    public void deleteBudgetById(Long id) {

        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No budget with this id.");
        }
    }


    @Transactional
    @Override
    public List<BudgetResponse> getAllBudgets() {

        List<Budget> budgets = budgetRepository.findAll();

        return budgets.stream().map(budgetMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public BudgetResponse getBudgetById(Long budgetId) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        return budgetMapper.toResponse(budget);
    }

    @Transactional
    @Override
    public Long getSpendAmountByBudgetId(Long id) {

        if (budgetRepository.existsById(id)) {
            return transactionRepository.sumAmountByBudgetId(id);
        } else {
            throw new ResourceNotFoundException("No budget with this id.");
        }
    }

    @Transactional
    @Override
    public void changeLimitAmount(Long budgetId, Long newLimitAmount) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        budget.setLimitAmount(newLimitAmount);
        budgetRepository.save(budget);

    }

    @Transactional
    @Override
    public void changeAccount(Long budgetId, String newAccount) {


//        if (budgetRepository.existsCurrentByAccount(newAccount)) {
//            throw new DuplicateResourceException("Budget with this new account already exists.");
//        } else {
//            Budget budget = budgetRepository.findById(budgetId)
//                    .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));
//
//            budget.setAccount(newAccount);
//            budgetRepository.save(budget);
//
//        }

        Optional<Budget> budgetOptional = budgetRepository.findById(budgetId);

        if(budgetOptional.isPresent()){
            Budget budget = budgetOptional.get();

            budget.setAccount(newAccount);

            budgetRepository.save(budget);
        }
        else {
            throw new ResourceNotFoundException("No budget with this id.");
        }
    }

    @Transactional
    @Override
    public Long getBudgetRemains(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this id."));

        return budget.getLimitAmount() - transactionRepository.sumAmountByBudgetId(budgetId);
    }
}

