package org.example.dto;

import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;

    private String name;

    private String email;

    private String token;
}
