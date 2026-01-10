package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Void> addBudget(@Valid @RequestBody BudgetRequest request){
        budgetService.addBudget(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/changeLimitAmount/{account}/{newLimitAmount}")
    public ResponseEntity<Void> changeLimitAmount(@PathVariable String account, @PathVariable Long newLimitAmount){
        budgetService.changeLimitAmount(account, newLimitAmount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/changeAccount/{oldAccount}/{newAccount}")
    public ResponseEntity<Void> changeAccount(@PathVariable String oldAccount, @PathVariable String newAccount){
        budgetService.changeAccount(oldAccount, newAccount);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        budgetService.deleteBudgetById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<Void> deleteCurrentByCategory(@PathVariable String category){
        budgetService.deleteCurrentBudgetByAccount(category);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/date/{account}/{date}")
    public ResponseEntity<Void> deleteByAccountAndDate(@PathVariable String account, @PathVariable LocalDate date){
        budgetService.deleteBudgetByAccountAndDate(account, date);

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(){
        List<BudgetResponse> budgetResponses = budgetService.getAllBudgets();

        return ResponseEntity.ok(budgetResponses);
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<BudgetResponse> getCurrentBudgetByAccount(@PathVariable String account){

        return ResponseEntity.ok(budgetService.getCurrentBudgetByAccount(account));
    }

    @GetMapping("/account/date/{account}/{date}")
    public ResponseEntity<BudgetResponse> getBudgetByAccountAndDate(@PathVariable String account, @PathVariable LocalDate date){

        return ResponseEntity.ok(budgetService.getBudgetByAccountAndDate(account, date));
    }

    @GetMapping("/amount/id/{id}")
    public ResponseEntity<Long> getSpendAmountByBudgetId(@PathVariable Long id){
        return  ResponseEntity.ok(budgetService.getSpendAmountByBudgetId(id));
    }

    @GetMapping("/remains/account/date/{account}/{date}")
    public ResponseEntity<Long> getBudgetRemains(@PathVariable String account, @PathVariable LocalDate date){
        return ResponseEntity.ok(budgetService.getBudgetRemains(account, date));
    }
}
