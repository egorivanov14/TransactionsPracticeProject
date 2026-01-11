package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request);

    void deleteBudgetById(Long id);


    List<BudgetResponse> getAllBudgets();

    BudgetResponse getBudgetById(Long budgetId);

    Long getSpendAmountByBudgetId(Long id);

    void changeLimitAmount(Long budgetId, Long newLimitAmount);

    void changeAccount(Long budgetId, String newAccount);

    Long getBudgetRemains(Long budgetId);

}
