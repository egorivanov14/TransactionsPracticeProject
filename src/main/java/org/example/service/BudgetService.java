package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request);

    void deleteBudgetById(Long id);

    void deleteCurrentBudgetByCategory(String category);

    void deleteBudgetByCategoryAndDate(String category, LocalDate date);

    List<BudgetResponse> getAllBudgets();

    BudgetResponse getCurrentBudgetByCategory(String category);

    BudgetResponse getBudgetByCategoryAndDate(String category, LocalDate date);

    void changeLimitAmount(String category, Long newLimitAmount);

    void changeCategory(String oldCategory, String newCategory);



}
