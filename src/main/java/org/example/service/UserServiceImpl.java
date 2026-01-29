package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.JwtService;
import org.example.dto.*;
import org.example.entity.User;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.WrongDataException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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

        User user = new User();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        RegisterResponse response = userMapper.toResponse(user);
        response.setToken(token);

        return response;
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("No user with this email."));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){

            String token = jwtService.generateToken(user);

            return new LoginResponse(token, user.getId(), user.getEmail());
        }
        else{
            throw new RuntimeException("Invalid password.");
        }

    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {

        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }
        else {
            throw new ResourceNotFoundException("No user with this id.");
        }
    }

    @Transactional
    @Override
    public void changeName(Long userId, String newName) {

        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){

            if(userRepository.existsByUserName(newName)){
                throw new DuplicateResourceException("User with this name already exists.");
            }

            User user = userOptional.get();

            user.setUserName(newName);
            userRepository.save(user);
        }
        else {
            throw new ResourceNotFoundException("No user with this id.");
        }
    }

    @Transactional
    @Override
    public void changeEmail(Long userId, String newEmail) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){

            if(userRepository.existsByEmail(newEmail)){
                throw new DuplicateResourceException("User with this email already exists.");
            }

            User user = userOptional.get();

            user.setEmail(newEmail);
            userRepository.save(user);
        }
        else {
            throw new ResourceNotFoundException("No user with this id.");
        }
    }

    @Transactional
    @Override
    public void changePassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        else {
            throw new ResourceNotFoundException("No user with this id.");
        }
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
                        () -> new ResourceNotFoundException("No user with this id.")));
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No user with this email."));

        return userMapper.toDto(user);

    }

    @Override
    public UserDto getUserByUserName(String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("No user with this UserName."));

        return userMapper.toDto(user);
    }

}
