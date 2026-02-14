package org.example.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetStatus {

    private Long totalIncome;

    private Long totalExpenditure;

    private Long remains;
}
