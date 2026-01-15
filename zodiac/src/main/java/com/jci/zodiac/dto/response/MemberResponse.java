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
 * DTO for full member response (Member Detail Page)
 * Contains all member information including computed fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    // ==================== Basic Information ====================

    private Long id;
    private String memberCode;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    // ==================== Zodiac Information ====================

    private String zodiacSign;          // Enum name as String
    private String zodiacElement;       // Enum name as String
    private String zodiacSymbol;        // e.g., "♐"
    private String zodiacDateRange;     // e.g., "Nov 22 - Dec 21"
    private String elementSymbol;       // e.g., "🔥"

    // ==================== JCI Specific ====================

    private String position;
    private Long departmentId;
    private String departmentName;      // Populated from Department entity
    private String departmentCode;      // e.g., "TRAINING"
    private LocalDate joinDate;
    private String membershipStatus;    // Enum name as String
    private String membershipType;      // Enum name as String

    // ==================== Contact & Personal ====================

    private String avatarUrl;
    private String address;
    private String city;
    private String emergencyContact;
    private String emergencyPhone;

    // ==================== Social Media ====================

    private String facebookUrl;

    // ==================== Metadata ====================

    private String notes;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;

    // ==================== Computed Fields ====================

    private Integer age;
    private Long daysSinceJoined;
    private Boolean isActive;
    private Boolean isBirthdayToday;
    private Integer daysUntilBirthday;

    // ==================== Zodiac Profile Preview ====================
    // Basic personality traits for quick view

    private List<String> personalityTraits;  // Top 3-5 traits from ZodiacProfile
    private List<String> strengths;          // Top 3-5 strengths
    private String elementDescription;       // "Fire signs are passionate and dynamic"
}