package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.LoginRequest;
import com.jci.zodiac.dto.response.LoginResponse;
import com.jci.zodiac.dto.response.UserResponse;
import com.jci.zodiac.entity.User;
import com.jci.zodiac.service.AuthService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - REST APIs for authentication
 * Base URL: /api/auth
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    private final AuthService authService;

    /**
     * Login
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("Login attempt for username: {}", request.getUsername());

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Logout
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout current user")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("Logout request");

        // In a real app, you might invalidate the JWT token here
        // For now, just return success

        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    /**
     * Get current user
     * GET /api/auth/me
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get currently authenticated user info")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        log.info("Get current user request");

        UserResponse response = authService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}