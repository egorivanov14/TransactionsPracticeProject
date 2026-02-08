package org.example.service;

import org.example.dto.StatisticsRequest;
import org.example.dto.StatisticsResponse;

public interface StatisticsService {

    StatisticsResponse getStatistics(Long budgetId , StatisticsRequest request, String email);
}
