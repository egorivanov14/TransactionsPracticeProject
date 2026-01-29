package org.example.service;


import org.example.dto.*;

import java.util.List;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void deleteUser(Long userId);

    void changeName(Long userId, String newName);

    void changeEmail(Long userId, String newEmail);

    void changePassword(Long userId, String newPassword);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserName(String userName);
}
