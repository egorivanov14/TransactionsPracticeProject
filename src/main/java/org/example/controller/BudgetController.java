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

    @PostMapping("/changeLimitAmount/{category}/{newLimitAmount}")
    public ResponseEntity<Void> changeLimitAmount(@PathVariable String category, @PathVariable Long newLimitAmount){
        budgetService.changeLimitAmount(category, newLimitAmount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/changeCategory/{oldCategory}/{newCategory}")
    public ResponseEntity<Void> changeCategory(@PathVariable String oldCategory, @PathVariable String newCategory){
        budgetService.changeCategory(oldCategory, newCategory);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        budgetService.deleteBudgetById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<Void> deleteByCategory(@PathVariable String category){
        budgetService.deleteBudgetByCategory(category);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(){
        List<BudgetResponse> budgetResponses = budgetService.getAllBudgets();

        return ResponseEntity.ok(budgetResponses);
    }
}
