package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.SchedulePoint;
import org.example.dto.StatisticsRequest;
import org.example.dto.StatisticsResponse;
import org.example.entity.Budget;
import org.example.dto.Type;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.BudgetRepository;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public StatisticsResponse getStatistics(Long budgetId, StatisticsRequest request, String email) {

        Budget budget = budgetRepository.findById(budgetId).
                orElseThrow(()->new ResourceNotFoundException("Budget not found."));

        if(!budget.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("Not your budget.");
        }
        Long initialAmount = budget.getInitialAmount();

        LocalDate startDate;

        if(budget.getStartDate().isAfter(request.getPeriod().getStartDate())){
            startDate = budget.getStartDate();
        }
        else {
            startDate = request.getPeriod().getStartDate();
        }

        Long totalIncome = transactionRepository.getTotalIncome(email, startDate, budgetId);
        Long totalExpenditure = transactionRepository.getTotalExpenditure(email, startDate, budgetId);
        Long netBalance = totalIncome - totalExpenditure;

        Long transactionsQuantity = transactionRepository.getTransactionsQuantity(email, startDate, budgetId);

        Long daysCount = Math.max(1, startDate.until(LocalDate.now(), ChronoUnit.DAYS));

        Long averageDailyIncome = totalIncome/daysCount;
        Long averageDailyExpenditure = totalExpenditure/daysCount;

        Long budgetStartAmount = initialAmount
                + transactionRepository.sumByPeriodAndType(budgetId, Type.INCOME,
                budget.getStartDate(), startDate.minusDays(1), email)
                - transactionRepository.sumByPeriodAndType(budgetId, Type.EXPENDITURE,
                budget.getStartDate(), startDate.minusDays(1), email);

        List<SchedulePoint> points = getPoints(budgetStartAmount, budgetId, startDate, daysCount, email);

        return new StatisticsResponse(points,
                totalIncome,
                totalExpenditure,
                averageDailyIncome,
                averageDailyExpenditure,
                netBalance,
                budgetStartAmount,
                transactionsQuantity);
    }

    private List<SchedulePoint> getPoints(Long balance,  Long budgetId,
                                          LocalDate date, Long daysCount, String email){

        List<SchedulePoint> points = new ArrayList<>();
        Long incomeByDay;
        Long expenditureByDay;

        points.add(new SchedulePoint(date, balance));

        for(int i = 0; i < daysCount; i++){

            incomeByDay = transactionRepository.sumByTypeAndDay(budgetId, Type.INCOME, date, email);
            expenditureByDay = transactionRepository.sumByTypeAndDay(budgetId, Type.EXPENDITURE, date, email);
            balance += (incomeByDay - expenditureByDay);

            points.add(new SchedulePoint(date, balance));

            date = date.plusDays(1);
        }

        return points;
    }
}