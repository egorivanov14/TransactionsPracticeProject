package org.example.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.dto.user.TokenResponse;
import org.example.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    TokenResponse register(RegisterRequest request, HttpServletResponse response);

    TokenResponse login(LoginRequest request, HttpServletResponse response);

    void deleteUser(String email);

    void changeName(String newName, String email);

    TokenResponse changeEmail(String email, String newEmail);

    void changePassword(String newPassword, String email);

    Page<UserDto> getAllUsers(Pageable pageable);

    TokenResponse refresh(String refreshToken, HttpServletResponse response);

    void logout(String refreshToken, HttpServletResponse response);

}
