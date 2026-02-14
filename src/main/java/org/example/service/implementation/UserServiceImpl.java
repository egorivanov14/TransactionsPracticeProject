package org.example.service.implementation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtService;
import org.example.dto.user.*;
import org.example.entity.RefreshToken;
import org.example.entity.User;
import org.example.exception.DuplicateResourceException;
import org.example.exception.InvalidRefreshTokenException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.example.service.MailService;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;

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

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshTokenValue)
                .httpOnly(true)
//                .secure(true)
//                .sameSite("Lax")
                .maxAge(Duration.ofHours(24))
                .path("/api")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        mailService.sendWelcomeMessage(user.getEmail(), user.getName());

        return new TokenResponse(accessToken);
    }

    @Transactional
    @Override
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found."));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){

             List<RefreshToken> refreshTokens = refreshTokenRepository.findValidByUserId(user.getId());

             refreshTokens.forEach(refreshToken -> {
                 refreshToken.setRevoked(true);
                 refreshToken.setRevokedAt(LocalDateTime.now());
             });

             refreshTokenRepository.saveAll(refreshTokens);

            String accessToken = jwtService.generateToken(user);
            String refreshTokenValue = jwtService.generateRefreshToken();

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUserId(user.getId());
            refreshToken.setTokenHash(jwtService.hashToken(refreshTokenValue));
            refreshTokenRepository.save(refreshToken);

            ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshTokenValue)
                    .httpOnly(true)
//                    .secure(true)
//                    .sameSite("Lax")
                    .maxAge(Duration.ofHours(24))
                    .path("/api")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

            mailService.sendWelcomeMessage(user.getEmail(), user.getName());

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

        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUser(user.getId());
        refreshTokenRepository.deleteAll(refreshTokens);

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

    @Transactional
    @Override
    public TokenResponse changeEmail(String email, String newEmail) {
        if(userRepository.existsByEmail(newEmail)){
            throw new DuplicateResourceException("User with this email already exists.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found."));
        user.setEmail(newEmail);
        userRepository.save(user);

        String newAccessToken = jwtService.generateToken(user);

        return new TokenResponse(newAccessToken);
    }


    @Transactional
    @Override
    public void changePassword(String newPassword, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toDto);
    }

    @Transactional
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

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshTokenValue)
                .httpOnly(true)
//                .secure(true)
//                .sameSite("Lax")
                .maxAge(Duration.ofHours(24))
                .path("/api")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new TokenResponse(accessToken);
    }

    @Transactional
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

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
//                .secure(true)
//                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .path("/api")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}