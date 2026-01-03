package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private User.ZodiacSign zodiacSign;
    private String avatarUrl;
    private String organization;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}