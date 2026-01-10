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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void addBudget(BudgetRequest request) {

        if(budgetRepository.existsCurrentByAccount(request.getAccount()) ||
                budgetRepository.existsByAccountAndDate(request.getAccount(), request.getStartDate())){
            throw new DuplicateResourceException("Budget with this account already exists.");
        }
        else {
            Budget budget = budgetMapper.toEntity(request);

            if(budget.getEndDate() != null && budget.getEndDate().isBefore(budget.getStartDate())){
                throw new WrongDataException("EndDate must be after startDate.");
            }

                budgetRepository.save(budget);

        }
    }

    @Transactional
    @Override
    public void deleteBudgetById(Long id) {

        if(budgetRepository.existsById(id)){
            budgetRepository.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("No budget with this id.");
        }
    }

    @Transactional
    @Override
    public void deleteCurrentBudgetByAccount(String account) {

        if(budgetRepository.existsCurrentByAccount(account)){
            budgetRepository.deleteCurrentByAccount(account);
        }
        else {
            throw new ResourceNotFoundException("No budget with ths account.");
        }
    }

    @Transactional
    @Override
    public void deleteBudgetByAccountAndDate(String account, LocalDate date) {

        if(budgetRepository.existsByAccountAndDate(account, date)){
            budgetRepository.deleteByAccountAndDate(account, date);
        }
        else {
            throw new ResourceNotFoundException("No budget with this account and date.");
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
    public BudgetResponse getCurrentBudgetByAccount(String account) {

        return budgetMapper.toResponse(budgetRepository.findCurrentByAccount(account).orElseThrow());

    }

    @Transactional
    @Override
    public BudgetResponse getBudgetByAccountAndDate(String account, LocalDate date) {

        return budgetMapper.toResponse(budgetRepository.findByAccountAndDate(account, date).orElseThrow());
    }

    @Transactional
    @Override
    public Long getSpendAmountByBudgetId(Long id) {

        if(budgetRepository.existsById(id)){
            return transactionRepository.sumAmountByBudgetId(id);
        }
        else {
            throw new ResourceNotFoundException("No budget with this id.");
        }
    }

    @Transactional
    @Override
    public void changeLimitAmount(String account, Long newLimitAmount) {

        Optional<Budget> budgetOptional = budgetRepository.findCurrentByAccount(account);

        if(budgetOptional.isPresent()){
            Budget budget = budgetOptional.get();

            budget.setLimitAmount(newLimitAmount);
            budgetRepository.save(budget);
        }
        else {
            throw new ResourceNotFoundException("No budget with this account.");
        }

    }

    @Transactional
    @Override
    public void changeAccount(String oldAccount, String newAccount) {

        if(budgetRepository.existsCurrentByAccount(newAccount)){
            throw new DuplicateResourceException("Budget with this new account already exists.");
        }
        else {
            Optional<Budget> budgetOptional = budgetRepository.findCurrentByAccount(oldAccount);

            if(budgetOptional.isPresent()){
                Budget budget = budgetOptional.get();

                budget.setAccount(newAccount);
                budgetRepository.save(budget);
            }
            else {
                throw new ResourceNotFoundException("No budget with this account.");
            }
        }
    }

    @Transactional
    @Override
    public Long getBudgetRemains(String account, LocalDate date) {

        if(account == null || account.trim().isEmpty()){
            throw new WrongDataException("Account cant be empty.");
        }

        LocalDate date1 = (date == null) ? LocalDate.now() : date;

        Budget budget = budgetRepository.findByAccountAndDate(account, date1)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this account and date."));

        return budget.getLimitAmount() - transactionRepository.sumAmountByBudgetId(budget.getId());

    }

}
