package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    private Long amount;

    @NotNull
    private Long budgetId;

    @NotBlank(message = "Category is required")
    private String category;
}
