package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Member;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for updating an existing member
 * All fields are optional (partial update support)
 * Auto-recalculates zodiacSign and zodiacElement if dateOfBirth changes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMemberRequest {

    // ==================== Basic Information ====================

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number must contain only numbers and +-()")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // ==================== Zodiac Information (READ-ONLY) ====================
    // Auto-recalculated if dateOfBirth is updated

    private Member.ZodiacSign zodiacSign;
    private Member.ZodiacElement zodiacElement;

    // ==================== JCI Specific ====================

    private String position;

    private Long departmentId;

    private LocalDate joinDate;

    private Member.MembershipStatus membershipStatus;

    private Member.MembershipType membershipType;

    // ==================== Contact & Personal ====================

    private String avatarUrl;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private String city;

    private String emergencyContact;

    private String emergencyPhone;

    // ==================== Social Media ====================

    @Pattern(regexp = "^(https?://)?(www\\.)?(facebook|fb)\\.com/.*$",
            message = "Facebook URL must be valid")
    private String facebookUrl;

    // ==================== Metadata ====================

    private String notes;

    private List<String> tags;
}