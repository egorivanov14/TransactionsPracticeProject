package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatisticsRequest {

    @NotNull
    private Period period;

}
