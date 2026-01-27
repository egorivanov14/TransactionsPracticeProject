package org.example.service;


import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.RegisterResponse;
import org.example.dto.UserDto;

import java.util.List;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void login(LoginRequest request);

    void deleteUser(Long userId);

    void changeName(Long userId, String newName);

    void changeEmail(Long userId, String newEmail);

    void changePassword(Long userId, String newPassword);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserName(String userName);
}
