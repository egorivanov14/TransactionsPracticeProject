package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetRequest {

    @NotBlank
    private String category;

    @NotNull
    @Min(0L)
    private Long limitAmount;

//    private LocalDate startDate;
//
//    @NotNull
//    private LocalDate endDate;
//
//    @NotBlank
//    private String periodType;
}
