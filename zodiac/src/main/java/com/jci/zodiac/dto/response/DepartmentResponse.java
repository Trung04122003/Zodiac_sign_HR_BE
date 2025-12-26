package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for department response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private Long id;
    private String name;
    private String code;
    private String description;

    // Zodiac Theme
    private String zodiacTheme;
    private String zodiacSymbol;
    private String colorPrimary;
    private String colorSecondary;
    private String iconUrl;

    // Leadership
    private Long leadMemberId;
    private String leadMemberName;

    // Statistics
    private Integer memberCount;
    private Integer activeProjectsCount;

    // Status
    private Boolean isActive;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}