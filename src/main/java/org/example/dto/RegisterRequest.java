package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3 ,max = 50)
    private String name;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    @NotBlank
    @Email(message = "Email is required")
    private String email;
}
