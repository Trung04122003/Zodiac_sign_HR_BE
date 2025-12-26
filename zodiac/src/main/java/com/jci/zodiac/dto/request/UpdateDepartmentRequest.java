package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Department;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating department
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDepartmentRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Department.ZodiacSign zodiacTheme;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be valid hex format (#RRGGBB)")
    private String colorPrimary;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be valid hex format (#RRGGBB)")
    private String colorSecondary;

    private String iconUrl;

    private Long leadMemberId;

    private Integer activeProjectsCount;

    private Boolean isActive;
}