package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Void> changeLimitAmount(@PathVariable Long budgetId,
                                                  @PathVariable Long newLimitAmount,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        budgetService.changeLimitAmount(budgetId, newLimitAmount, email);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/budgetId/{budgetId}/account/")
    public ResponseEntity<Void> changeAccount(@PathVariable Long budgetId,
                                              @PathVariable String newAccount,
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
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        List<BudgetResponse> budgetResponses = budgetService.getAllBudgetsByUser(email);

        return ResponseEntity.ok(budgetResponses);
    }

    @GetMapping("id/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long budgetId,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(budgetService.getBudgetByIdAndUser(budgetId, email));
    }

    @GetMapping("/amount/id/{id}")
    public ResponseEntity<Long> getSpendAmountByBudgetId(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return  ResponseEntity.ok(budgetService.getSpendAmountByBudgetIdAndUser(id, email));
    }

    @GetMapping("/remains/{budgetId}")
    public ResponseEntity<Long> getBudgetRemains(@PathVariable Long budgetId,
                                                 @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(budgetService.getBudgetRemains(budgetId, email));
    }
}
