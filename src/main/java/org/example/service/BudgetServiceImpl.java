package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.entity.Budget;
import org.example.entity.Transaction;
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

        if(budgetRepository.existsCurrentByCategory(request.getCategory()) ||
                budgetRepository.existsByCategoryAndDate(request.getCategory(), request.getStartDate())){
            throw new DuplicateResourceException("Budget with this category already exists.");
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
    public void deleteCurrentBudgetByCategory(String category) {

        if(budgetRepository.existsCurrentByCategory(category)){
            budgetRepository.deleteCurrentByCategory(category);
        }
        else {
            throw new ResourceNotFoundException("No budget with ths category.");
        }
    }

    @Transactional
    @Override
    public void deleteBudgetByCategoryAndDate(String category, LocalDate date) {

        if(budgetRepository.existsByCategoryAndDate(category, date)){
            budgetRepository.deleteByCategoryAndDate(category, date);
        }
        else {
            throw new ResourceNotFoundException("No budget with this category and date.");
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
    public BudgetResponse getCurrentBudgetByCategory(String category) {

        return budgetMapper.toResponse(budgetRepository.findCurrentByCategory(category).orElseThrow());

    }

    @Transactional
    @Override
    public BudgetResponse getBudgetByCategoryAndDate(String category, LocalDate date) {

        return budgetMapper.toResponse(budgetRepository.findByCategoryAndDate(category, date).orElseThrow());
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
    public void changeLimitAmount(String category, Long newLimitAmount) {

        Optional<Budget> budgetOptional = budgetRepository.findCurrentByCategory(category);

        if(budgetOptional.isPresent()){
            Budget budget = budgetOptional.get();

            budget.setLimitAmount(newLimitAmount);
            budgetRepository.save(budget);
        }
        else {
            throw new ResourceNotFoundException("No budget with this category.");
        }

    }

    @Transactional
    @Override
    public void changeCategory(String oldCategory, String newCategory) {

        if(budgetRepository.existsCurrentByCategory(newCategory)){
            throw new DuplicateResourceException("Budget with this new category already exists.");
        }
        else {
            Optional<Budget> budgetOptional = budgetRepository.findCurrentByCategory(oldCategory);

            if(budgetOptional.isPresent()){
                Budget budget = budgetOptional.get();

                budget.setCategory(newCategory);
                budgetRepository.save(budget);
            }
            else {
                throw new ResourceNotFoundException("No budget with this category.");
            }
        }
    }

    @Transactional
    @Override
    public Long getBudgetRemains(String category, LocalDate date) {

        if(category == null || category.trim().isEmpty()){
            throw new WrongDataException("Category cant be empty.");
        }

        LocalDate date1 = (date == null) ? LocalDate.now() : date;

        Budget budget = budgetRepository.findByCategoryAndDate(category, date1)
                .orElseThrow(() -> new ResourceNotFoundException("No budget with this category and date."));

        return budget.getLimitAmount() - transactionRepository.sumAmountByBudgetId(budget.getId());

    }

}
