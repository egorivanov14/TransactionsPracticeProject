package org.example.dto.chart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatisticsRequest {

    @NotNull
    private Period period;

}
