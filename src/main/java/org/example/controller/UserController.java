package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.dto.user.TokenResponse;
import org.example.dto.user.UserDto;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response){
        return ResponseEntity.ok(userService.register(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
        return ResponseEntity.ok(userService.login(request, response));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        userService.deleteUser(email);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(size = 10, sort = "id") Pageable pageable){
        Page<UserDto> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getMySelf(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        return ResponseEntity.ok(Map.of("email", email));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse response){

        return ResponseEntity.ok(userService.refresh(refreshToken, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse response){
        userService.logout(refreshToken, response);

        return ResponseEntity.noContent().build();
    }
}
