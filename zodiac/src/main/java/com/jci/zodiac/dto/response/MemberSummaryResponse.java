package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for member list view (simplified for performance)
 * Used in: Member List Page, Search Results, Team Member Lists
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSummaryResponse {

    // ==================== Basic Information ====================

    private Long id;
    private String memberCode;
    private String fullName;
    private String email;
    private String position;

    // ==================== Zodiac Information ====================

    private String zodiacSign;          // Enum name as String
    private String zodiacElement;       // Enum name as String
    private String zodiacSymbol;        // e.g., "♐"
    private String elementSymbol;       // e.g., "🔥"

    // ==================== Display Information ====================

    private String avatarUrl;
    private String membershipStatus;    // Enum name as String
    private LocalDate joinDate;
    private Boolean isActive;

    // ==================== Department Info ====================

    private Long departmentId;
    private String departmentName;      // e.g., "Đào tạo"
    private String departmentCode;      // e.g., "TRAINING"

    // ==================== Computed Fields ====================

    private Integer age;
    private Long daysSinceJoined;
    private Boolean isBirthdayToday;
    private Boolean isBirthdayThisWeek;
}