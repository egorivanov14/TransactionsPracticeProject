package org.example.service;

import org.example.dto.*;

import java.util.List;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void deleteUser(String email);

    void changeName(String newName, String email);

//    void changeEmail(String email, String newEmail);

    void changePassword(String newPassword, String email);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto getUserByName(String name);
}
