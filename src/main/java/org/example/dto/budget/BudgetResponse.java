package org.example.dto.budget;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BudgetResponse {

    private Long id;

    private String account;

    private Long initialAmount;

    private LocalDate startDate;

}
