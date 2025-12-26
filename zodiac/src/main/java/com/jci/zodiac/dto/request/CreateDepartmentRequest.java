package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating department
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Department code is required")
    @Pattern(regexp = "^[A-Z0-9-]{2,20}$", message = "Code must be uppercase alphanumeric with hyphens, 2-20 characters")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Department.ZodiacSign zodiacTheme;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be valid hex format (#RRGGBB)")
    private String colorPrimary;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be valid hex format (#RRGGBB)")
    private String colorSecondary;

    private String iconUrl;

    private Long leadMemberId;

    @Builder.Default
    private Integer activeProjectsCount = 0;
}