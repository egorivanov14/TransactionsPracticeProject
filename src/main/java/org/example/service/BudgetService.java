package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request);

    void deleteBudgetById(Long id);

    void deleteBudgetByCategory(String category);

    List<BudgetResponse> getAllBudgets();

    void changeLimitAmount(String category, Long newLimitAmount);

    void changeCategory(String oldCategory, String newCategory);



}
