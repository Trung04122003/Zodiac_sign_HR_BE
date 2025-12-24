package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for member response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String memberCode;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    // Zodiac Info
    private String zodiacSign;
    private String zodiacElement;
    private String zodiacSymbol;

    // JCI Specific
    private String position;
    private Long departmentId;
    private String departmentName; // Will be populated if department exists
    private LocalDate joinDate;
    private String membershipStatus;
    private String membershipType;

    // Contact & Personal
    private String avatarUrl;
    private String address;
    private String city;
    private String emergencyContact;
    private String emergencyPhone;

    // Professional
    private String occupation;
    private String company;
    private String linkedinUrl;

    // Metadata
    private String notes;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed Fields
    private Integer age;
    private Long daysSinceJoined;
    private Boolean isActive;
}