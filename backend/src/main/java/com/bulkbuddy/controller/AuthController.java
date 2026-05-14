package com.bulkbuddy.controller;

import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.request.LoginRequest;
import com.bulkbuddy.dto.response.AuthResponse;
import com.bulkbuddy.dto.response.UserResponse;
import com.bulkbuddy.security.JwtUtil;
import com.bulkbuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Authentication controller.
 * <p>
 * Provides login and a "role-select" endpoint that lets
 * the user pick a role without real login (for development).
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper dtoMapper;

    /**
     * POST /api/auth/login — Authenticate with username + password.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        log.info("User '{}' logged in successfully", user.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/select/{userId} — Quick login by selecting a user (dev mode).
     * Generates a JWT without password verification.
     */
    @PostMapping("/select/{userId}")
    public ResponseEntity<?> selectUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();

            log.info("Dev login: selected user '{}'", user.getUsername());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/auth/users — List all available users (for the role-select screen).
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(dtoMapper::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
