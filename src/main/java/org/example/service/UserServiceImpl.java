package org.example.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtService;
import org.example.dto.*;
import org.example.entity.RefreshToken;
import org.example.entity.User;
import org.example.exception.DuplicateResourceException;
import org.example.exception.InvalidRefreshTokenException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public TokenResponse register(RegisterRequest request, HttpServletResponse response) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User with this email already exists.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshTokenValue = jwtService.generateRefreshToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setTokenHash(jwtService.hashToken(refreshTokenValue));
        refreshTokenRepository.save(refreshToken);

        Cookie refreshCookie = new Cookie("refresh_token", refreshTokenValue);
        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/users");
        refreshCookie.setMaxAge(60*60*24*7);
        response.addCookie(refreshCookie);

        return new TokenResponse(accessToken);
    }

    @Transactional
    @Override
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found."));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){

            String accessToken = jwtService.generateToken(user);
            String refreshTokenValue = jwtService.generateRefreshToken();

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUserId(user.getId());
            refreshToken.setTokenHash(jwtService.hashToken(refreshTokenValue));
            refreshTokenRepository.save(refreshToken);

            Cookie refreshCookie = new Cookie("refresh_token", refreshTokenValue);
            refreshCookie.setHttpOnly(true);
//            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/users");
            refreshCookie.setMaxAge(60*60*24*7);
            response.addCookie(refreshCookie);

            return new TokenResponse(accessToken);
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
    public Page<UserDto> getAllUsers(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toDto);
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

    @Override
    public TokenResponse refresh(String refreshToken, HttpServletResponse response) {

        if(refreshToken == null){
            throw new InvalidRefreshTokenException("No refresh token.");
        }

        String refTokenHash = jwtService.hashToken(refreshToken);
        RefreshToken oldRefreshToken = refreshTokenRepository.findValidByHash(refTokenHash).orElseThrow(
                ()->new InvalidRefreshTokenException("Invalid token."));

        if(LocalDateTime.now().isAfter(oldRefreshToken.getExpirationDate())){
            throw new InvalidRefreshTokenException("Invalid token.");
        }

        User user = userRepository.findById(oldRefreshToken.getUserId()).orElseThrow(
                ()-> new ResourceNotFoundException("User not found."));

        String accessToken = jwtService.generateToken(user);
        String refreshTokenValue = jwtService.generateRefreshToken();

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setTokenHash(jwtService.hashToken(refreshTokenValue));
        refreshTokenEntity.setUserId(oldRefreshToken.getUserId());

        oldRefreshToken.setRevoked(true);
        oldRefreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(oldRefreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        Cookie refreshCookie = new Cookie("refresh_token", refreshTokenValue);
        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/users");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(refreshCookie);

        return new TokenResponse(accessToken);
    }

    @Override
    public void logout(String refreshToken, HttpServletResponse response) {

        if(refreshToken == null){
            throw new InvalidRefreshTokenException("No token.");
        }

        refreshTokenRepository.findValidByHash(jwtService.hashToken(refreshToken))
                        .ifPresent(token -> {
                            token.setRevoked(true);
                            token.setRevokedAt(LocalDateTime.now());
                            refreshTokenRepository.save(token);
                        });

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/users");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }

}
