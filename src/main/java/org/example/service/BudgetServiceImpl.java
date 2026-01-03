package org.example.service;


import org.example.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.entity.Budget;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.BudgetMapper;
import org.example.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    @Transactional
    @Override
    public void addBudget(BudgetRequest request) {
        if(budgetRepository.existsByCategory(request.getCategory())){
            throw new DuplicateResourceException("Budget with this category already exists.");
        }
        else {
            budgetRepository.save(budgetMapper.toEntity(request));
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

    @Override
    public void deleteBudgetByCategory(String category) {

        if(budgetRepository.existsByCategory(category)){
            budgetRepository.deleteByCategory(category);
        }
        else {
            throw new ResourceNotFoundException("No budget with ths category.");
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
    public void changeLimitAmount(String category, Long newLimitAmount) {

        if(budgetRepository.existsByCategory(category)){
            Budget budget = budgetRepository.findByCategory(category).orElseThrow();

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
        if(budgetRepository.existsByCategory(oldCategory)){
            Budget budget = budgetRepository.findByCategory(oldCategory).orElseThrow();

            budget.setCategory(newCategory);
            budgetRepository.save(budget);
        }
        else {
            throw new ResourceNotFoundException("No budget with this category.");
        }
    }
}
