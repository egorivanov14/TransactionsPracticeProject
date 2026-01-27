package org.example.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String userName;

    private String email;

    private String password;
}
