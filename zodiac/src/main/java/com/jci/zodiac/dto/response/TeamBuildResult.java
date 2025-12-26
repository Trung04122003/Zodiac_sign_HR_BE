package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO for team building result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamBuildResult {

    private String teamName;
    private List<MemberSummaryResponse> members;
    private int teamSize;

    // Compatibility Metrics
    private BigDecimal overallCompatibilityScore;
    private String compatibilityLevel; // Excellent/Good/Moderate/Challenging

    // Element Analysis
    private Map<Member.ZodiacElement, Long> elementBalance;
    private boolean isElementBalanced;
    private List<String> missingElements;

    // Conflict Analysis
    private List<ConflictPair> potentialConflicts;
    private int conflictCount;
    private boolean hasHighConflicts;

    // Best Pairs
    private List<BestPair> topCompatiblePairs;

    // Recommendations
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> recommendations;

    // Insights
    private String teamDynamicsSummary;
    private List<String> keyInsights;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConflictPair {
        private MemberSummaryResponse member1;
        private MemberSummaryResponse member2;
        private BigDecimal compatibilityScore;
        private String riskLevel; // High/Medium/Low
        private String suggestion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BestPair {
        private MemberSummaryResponse member1;
        private MemberSummaryResponse member2;
        private BigDecimal compatibilityScore;
        private String collaborationType;
    }
}