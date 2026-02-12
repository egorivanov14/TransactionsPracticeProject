package org.example.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    TokenResponse register(RegisterRequest request, HttpServletResponse response);

    TokenResponse login(LoginRequest request, HttpServletResponse response);

    void deleteUser(String email);

    void changeName(String newName, String email);

//    void changeEmail(String email, String newEmail);

    void changePassword(String newPassword, String email);

    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto getUserByName(String name);

    TokenResponse refresh(String refreshToken, HttpServletResponse response);

    void logout(String refreshToken, HttpServletResponse response);

}
