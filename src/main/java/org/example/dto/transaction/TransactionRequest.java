package org.example.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @Min(1L)
    private Long amount;

    @NotNull
    private Long budgetId;

    @NotNull
    private Type type;

    @NotBlank(message = "Category is required")
    private String category;
}
