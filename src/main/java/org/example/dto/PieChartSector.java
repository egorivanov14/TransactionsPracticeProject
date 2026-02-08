package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PieChartSector {

    private String category;

    private Long amount;

    private double percentage;

}
