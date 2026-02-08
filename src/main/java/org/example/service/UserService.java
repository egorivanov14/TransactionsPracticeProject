package org.example.service;

import org.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void deleteUser(String email);

    void changeName(String newName, String email);

//    void changeEmail(String email, String newEmail);

    void changePassword(String newPassword, String email);

    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto getUserByName(String name);

}
