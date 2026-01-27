package org.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponse {

    private Long id;

    private Long userId;

    private String account;

    private String category;

    private Long amount;

    private LocalDate createdAt;
}
