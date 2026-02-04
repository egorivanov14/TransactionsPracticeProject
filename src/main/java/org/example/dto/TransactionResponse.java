package org.example.dto;

import lombok.Data;
import org.example.entity.Type;

import java.time.LocalDate;

@Data
public class TransactionResponse {

    private Long id;

    private String account;

    private String category;

    private Type type;

    private Long amount;

    private LocalDate createdAt;
}
