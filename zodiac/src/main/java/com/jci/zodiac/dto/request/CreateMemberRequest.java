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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberRequest {

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

    // JCI Specific
    private String position;

    private Long departmentId;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    private Member.MembershipStatus membershipStatus;

    private Member.MembershipType membershipType;

    // Contact & Personal
    private String avatarUrl;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private String city;

    private String emergencyContact;

    private String emergencyPhone;

    // Professional
    private String occupation;

    private String company;

    @Pattern(regexp = "^(https?://)?([\\w]+\\.)?linkedin\\.com/.*$",
            message = "LinkedIn URL must be valid")
    private String linkedinUrl;

    // Metadata
    private String notes;

    private List<String> tags;
}
