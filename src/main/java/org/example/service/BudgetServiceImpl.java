package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.entity.Budget;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.BudgetMapper;
import org.example.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    @Transactional
    @Override
    public void addBudget(BudgetRequest request) {
        if(budgetRepository.existsCurrentByCategory(request.getCategory()) ||
                budgetRepository.existsByCategoryAndDate(request.getCategory(), request.getStartDate())){
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
        if(budgetRepository.existsCurrentByCategory(category)){

            return budgetMapper.toResponse(budgetRepository.findCurrentByCategory(category).orElseThrow());
        }
        else {
            throw new ResourceNotFoundException("No budget with this category now.");
        }
    }

    @Transactional
    @Override
    public BudgetResponse getBudgetByCategoryAndDate(String category, LocalDate date) {
        if(budgetRepository.existsByCategoryAndDate(category, date)){

            return budgetMapper.toResponse(budgetRepository.findByCategoryAndDate(category, date).orElseThrow());
        }
        else {
            throw new ResourceNotFoundException("No budget with this date and category.");
        }
    }

    @Transactional
    @Override
    public void changeLimitAmount(String category, Long newLimitAmount) {

        if(budgetRepository.existsCurrentByCategory(category)){
            Budget budget = budgetRepository.findCurrentByCategory(category).orElseThrow();

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

        else if(budgetRepository.existsCurrentByCategory(oldCategory)){
            Budget budget = budgetRepository.findCurrentByCategory(oldCategory).orElseThrow();

            budget.setCategory(newCategory);
            budgetRepository.save(budget);

        }
        else {
            throw new ResourceNotFoundException("No budget with this category.");
        }
    }

}
