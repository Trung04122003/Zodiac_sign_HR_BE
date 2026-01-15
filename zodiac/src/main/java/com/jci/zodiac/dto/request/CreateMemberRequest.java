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
 * DTO for creating a new member
 * Auto-calculates zodiacSign and zodiacElement from dateOfBirth in service layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberRequest {

    // ==================== Basic Information ====================

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number must contain only numbers and +-()")
    private String phone;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // ==================== Zodiac Information (READ-ONLY) ====================
    // These fields are auto-calculated by backend from dateOfBirth
    // Frontend can send these for validation but backend will override

    private Member.ZodiacSign zodiacSign;
    private Member.ZodiacElement zodiacElement;

    // ==================== JCI Specific ====================

    private String position;

    private Long departmentId;

    @NotNull(message = "Join date is required")
    @Builder.Default
    private LocalDate joinDate = LocalDate.now();

    @Builder.Default
    private Member.MembershipStatus membershipStatus = Member.MembershipStatus.Active;

    @Builder.Default
    private Member.MembershipType membershipType = Member.MembershipType.FullMember;

    // ==================== Contact & Personal ====================

    private String avatarUrl;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Builder.Default
    private String city = "Da Nang";

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