package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.StatisticsRequest;
import org.example.dto.StatisticsResponse;
import org.example.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/{budgetId}")
    public ResponseEntity<StatisticsResponse> getStatistics(@PathVariable Long budgetId,
                                                            @Valid @RequestBody StatisticsRequest request,
                                                            @AuthenticationPrincipal UserDetails userDetails){

        String email = userDetails.getUsername();

        return ResponseEntity.ok(statisticsService.getStatistics(budgetId, request, email));
    }
}
