package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request, String email);

    void deleteBudgetById(Long id, String email);

    List<BudgetResponse> getAllBudgetsByUser(String email);

    BudgetResponse getBudgetByIdAndUser(Long budgetId, String email);

    Long getSpendAmountByBudgetIdAndUser(Long id, String email);

    void changeLimitAmount(Long budgetId, Long newLimitAmount, String email);

    void changeAccount(Long budgetId, String newAccount, String email);

    Long getBudgetRemains(Long budgetId, String email);

}
