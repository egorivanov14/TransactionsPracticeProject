package org.example.dto;

import lombok.Data;
import org.example.entity.PeriodType;

import java.time.LocalDate;

@Data
public class BudgetResponse {

    private Long id;

    private Long userId;

    private String account;

    private Long limitAmount;

    private LocalDate startDate;
//
//    private LocalDate endDate;
//
//    private PeriodType periodType;
}
