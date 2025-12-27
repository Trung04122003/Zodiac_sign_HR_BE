package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for generating reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateReportRequest {

    @NotBlank(message = "Report type is required")
    private String reportType; // PAIR, TEAM, DEPARTMENT, ORGANIZATION

    // For PAIR reports
    private Long member1Id;
    private Long member2Id;

    // For TEAM reports
    private List<Long> teamMemberIds;
    private Long teamId;

    // For DEPARTMENT reports
    private Long departmentId;

    // Date range (optional)
    private LocalDate startDate;
    private LocalDate endDate;

    // Additional filters
    private Boolean includeInactive;
    private Boolean includeDetailedAnalysis;
    private Boolean includeRecommendations;

    // Export options
    private String format; // PDF, EXCEL, JSON
    private Boolean includeCharts;
    private Boolean includeRawData;
}