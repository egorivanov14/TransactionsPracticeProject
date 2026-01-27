package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String userName;

    @Size(min = 6)
    private String password;

    @Email
    private String email;
}
