package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequest {

    @NotNull
    private Long amount;

    @NotBlank
    private String account;

//    @NotBlank
//    private String tag;
}
