package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.PeriodType;

import java.time.LocalDate;

@Data
public class BudgetRequest {

    @NotBlank
    private String category;

    @NotNull
    @Min(0L)
    private Long limitAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private PeriodType periodType;
}
