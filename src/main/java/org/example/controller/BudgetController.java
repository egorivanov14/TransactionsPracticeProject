package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/budgetId/{budgetId}/changeLimitAmount/{newLimitAmount}")
    public ResponseEntity<Void> changeLimitAmount(@PathVariable Long budgetId, @PathVariable Long newLimitAmount){
        budgetService.changeLimitAmount(budgetId, newLimitAmount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/budgetId/{budgetId}/changeAccount/{newAccount}")
    public ResponseEntity<Void> changeAccount(@PathVariable Long budgetId, @PathVariable String newAccount){
        budgetService.changeAccount(budgetId, newAccount);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        budgetService.deleteBudgetById(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(){
        List<BudgetResponse> budgetResponses = budgetService.getAllBudgets();

        return ResponseEntity.ok(budgetResponses);
    }

    @GetMapping("id/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long budgetId){
        return ResponseEntity.ok(budgetService.getBudgetById(budgetId));
    }

    @GetMapping("/amount/id/{id}")
    public ResponseEntity<Long> getSpendAmountByBudgetId(@PathVariable Long id){
        return  ResponseEntity.ok(budgetService.getSpendAmountByBudgetId(id));
    }

    @GetMapping("/remains/{budgetId}")
    public ResponseEntity<Long> getBudgetRemains(@PathVariable Long budgetId){
        return ResponseEntity.ok(budgetService.getBudgetRemains(budgetId));
    }
}
