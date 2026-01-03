package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.LoginRequest;
import com.jci.zodiac.dto.response.LoginResponse;
import com.jci.zodiac.dto.response.UserResponse;
import com.jci.zodiac.entity.User;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Simple login (no password encryption for now)
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        // Simple password check (in production, use BCrypt)
        // For now, just check if password matches
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Invalid username or password");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate simple token (in production, use JWT)
        String token = "Bearer_" + UUID.randomUUID().toString();

        // Map to response
        UserResponse userResponse = mapToUserResponse(user);

        return LoginResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    /**
     * Get current user (placeholder - in real app, get from JWT token)
     */
    public UserResponse getCurrentUser() {
        // For now, return the default user (id = 1)
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    /**
     * Map User entity to UserResponse
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .zodiacSign(user.getZodiacSign())
                .avatarUrl(user.getAvatarUrl())
                .organization(user.getOrganization())
                .position(user.getPosition())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}