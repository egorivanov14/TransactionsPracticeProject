package org.example.dto;

import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;

    private String userName;

    private String email;

    private String token;
}
