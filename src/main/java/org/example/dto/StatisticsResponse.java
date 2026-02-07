package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatisticsResponse {

    private List<SchedulePoint> points;

    private Long totalIncome;
    private Long totalExpenditure;

    private Long averageDailyIncome;
    private Long averageDailyExpenditure;

    private Long netBalance;
    private Long balanceStartAmount;
    private Long transactionsQuantity;

}
