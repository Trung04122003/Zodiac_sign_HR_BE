package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for conflict alerts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictAlert {

    private Long alertId;
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW

    private MemberSummaryResponse member1;
    private MemberSummaryResponse member2;

    private BigDecimal compatibilityScore;
    private BigDecimal conflictPotential;

    private String primaryIssue;
    private List<String> conflictAreas;

    private String recommendation;
    private List<String> preventionStrategies;

    @Builder.Default
    private LocalDateTime detectedAt = LocalDateTime.now();

    private boolean isResolved;
}