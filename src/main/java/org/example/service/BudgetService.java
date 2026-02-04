package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.dto.BudgetStatus;

import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request, String email);

    void deleteBudgetById(Long id, String email);

    List<BudgetResponse> getAllBudgetsByUser(String email);

    BudgetResponse getBudgetById(Long budgetId, String email);

    Long getExpenditure(Long id, String email);

    void changeInitialAmount(Long budgetId, Long newInitialAmount, String email);

    void changeAccount(Long budgetId, String newAccount, String email);

    BudgetStatus getBudgetStatus(Long budgetId, String email);

}
