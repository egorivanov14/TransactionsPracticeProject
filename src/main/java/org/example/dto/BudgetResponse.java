package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetResponse {

    private Long id;

    private String category;

    private Long limitAmount;

    private Long spend;

//    private LocalDate startDate;
//
//    private LocalDate endDate;
//
//    private String periodType;
}
