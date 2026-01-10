package org.example.service;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {

    void addBudget(BudgetRequest request);

    void deleteBudgetById(Long id);

    void deleteCurrentBudgetByAccount(String account);

    void deleteBudgetByAccountAndDate(String account, LocalDate date);

    List<BudgetResponse> getAllBudgets();

    BudgetResponse getCurrentBudgetByAccount(String account);

    BudgetResponse getBudgetByAccountAndDate(String account, LocalDate date);

    Long getSpendAmountByBudgetId(Long id);

    void changeLimitAmount(String account, Long newLimitAmount);

    void changeAccount(String oldAccount, String newAccount);

    Long getBudgetRemains(String account, LocalDate date);

}
