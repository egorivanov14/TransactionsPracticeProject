package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.budget.BudgetRequest;
import org.example.dto.budget.BudgetResponse;
import org.example.dto.budget.BudgetStatus;
import org.example.service.BudgetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Void> addBudget(@AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody BudgetRequest request){

        String email = userDetails.getUsername();

        budgetService.addBudget(request, email);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/budgetId/{budgetId}/limit/")
    public ResponseEntity<Void> changeInitialAmount(@PathVariable Long budgetId,
                                                    @RequestParam Long newInitialAmount,
                                                    @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        budgetService.changeInitialAmount(budgetId, newInitialAmount, email);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/budgetId/{budgetId}/account/")
    public ResponseEntity<Void> changeAccount(@PathVariable Long budgetId,
                                              @RequestParam String newAccount,
                                              @AuthenticationPrincipal UserDetails userDetails
                                              ){
        String email = userDetails.getUsername();

        budgetService.changeAccount(budgetId, newAccount, email);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails
                                           ){
        String email = userDetails.getUsername();
        budgetService.deleteBudgetById(id, email);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<BudgetResponse>> getAllBudgets(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PageableDefault(size = 10, sort = "id") Pageable pageable){
        String email = userDetails.getUsername();

        Page<BudgetResponse> budgetResponses = budgetService.getAllBudgetsByUser(email, pageable);

        return ResponseEntity.ok(budgetResponses);
    }

    @GetMapping("/id/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long budgetId,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(budgetService.getBudgetById(budgetId, email));
    }

    @GetMapping("/expenditure/id/{id}")
    public ResponseEntity<Long> getExpenditure(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return  ResponseEntity.ok(budgetService.getExpenditure(id, email));
    }

    @GetMapping("/status/{budgetId}")
    public ResponseEntity<BudgetStatus> getBudgetRemains(@PathVariable Long budgetId,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(budgetService.getBudgetStatus(budgetId, email));
    }
}
