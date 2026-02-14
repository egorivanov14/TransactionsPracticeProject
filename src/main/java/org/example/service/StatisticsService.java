package org.example.service;

import org.example.dto.chart.StatisticsRequest;
import org.example.dto.chart.StatisticsResponse;

public interface StatisticsService {

    StatisticsResponse getStatistics(Long budgetId , StatisticsRequest request, String email);
}
