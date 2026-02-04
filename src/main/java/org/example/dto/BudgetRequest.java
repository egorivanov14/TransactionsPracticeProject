package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetRequest {

    @NotBlank(message = "Account is required")

    private String account;

    @NotNull
    @Min(1L) @Max(Long.MAX_VALUE)
    private Long initialAmount;

}
