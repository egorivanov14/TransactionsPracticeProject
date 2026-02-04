package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetStatus {

    private Long totalIncome;

    private Long totalExpenditure;

    private Long remains;
}
