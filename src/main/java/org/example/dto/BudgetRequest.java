package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String account;

    @NotNull
    @Min(0L)
    private Long limitAmount;

//    @NotNull
//    private LocalDate startDate;

//    private LocalDate endDate;
//
//    private PeriodType periodType;


}
