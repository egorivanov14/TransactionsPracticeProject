package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

}
