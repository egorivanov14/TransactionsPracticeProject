package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.JwtService;
import org.example.dto.*;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User with this email already exists.");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new RegisterResponse(token);
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found."));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){

            String token = jwtService.generateToken(user);

            return new LoginResponse(token);
        }
        else{
            throw new RuntimeException("Invalid password.");
        }

    }

    @Transactional
    @Override
    public void deleteUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void changeName(String newName, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (userRepository.existsByName(newName)) {
            throw new DuplicateResourceException("User with this name already exists.");
        }

        user.setName(newName);
        userRepository.save(user);
    }

//    @Transactional
//    @Override
//    public void changeEmail(Long userId, String newEmail) {
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        if(userOptional.isPresent()){
//
//            if(userRepository.existsByEmail(newEmail)){
//                throw new DuplicateResourceException("User with this email already exists.");
//            }
//
//            User user = userOptional.get();
//
//            user.setEmail(newEmail);
//            userRepository.save(user);
//        }
//        else {
//            throw new ResourceNotFoundException("No user with this id.");
//        }
//    }

    @Transactional
    @Override
    public void changePassword(String newPassword, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto getUserById(Long id) {

        return userMapper.toDto(
                userRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")));
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toDto(user);

    }

    @Override
    public UserDto getUserByName(String name) {

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toDto(user);
    }

}
