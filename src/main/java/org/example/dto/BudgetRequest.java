package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetRequest {

    @NotBlank
    private String account;

    @NotNull
    @Min(0L)
    private Long limitAmount;

}
