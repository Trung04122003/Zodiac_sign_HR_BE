package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for comprehensive compatibility report
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilityReport {

    private String reportId;
    private String reportType; // PAIR, TEAM, DEPARTMENT

    @Builder.Default
    private LocalDateTime generatedAt = LocalDateTime.now();

    // Summary
    private String title;
    private String summary;
    private BigDecimal overallScore;
    private String overallLevel;

    // Detailed Analysis
    private Map<String, BigDecimal> scoreBreakdown; // Work, Communication, Synergy, etc.

    // Strengths & Weaknesses
    private List<String> keyStrengths;
    private List<String> keyWeaknesses;
    private List<String> opportunities;
    private List<String> threats;

    // Recommendations
    private List<String> actionItems;
    private List<String> bestPractices;
    private List<String> warningFlags;

    // Data
    private Object detailedData; // Can be TeamCompatibilityResult or other types

    // Export options
    private boolean exportable;
    private List<String> availableFormats; // PDF, EXCEL, JSON
}