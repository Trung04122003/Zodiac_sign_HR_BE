package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAnalyticsResponse {

    private Long teamId;
    private String teamName;

    // Compatibility Metrics
    private BigDecimal overallCompatibilityScore;
    private String compatibilityLevel;

    private Map<Member.ZodiacSign, Long> zodiacDistribution;
    private Map<Member.ZodiacElement, Long> elementBalance;

    private boolean isElementBalanced;
    private List<String> missingElements;

    // Conflict Analysis
    private Integer potentialConflictCount;
    private List<ConflictPairInfo> conflictPairs;

    // Best Pairs
    private List<BestPairInfo> topCompatiblePairs;

    // Team Insights
    private List<String> teamStrengths;
    private List<String> teamChallenges;
    private List<String> recommendations;

    private String teamDynamicsSummary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConflictPairInfo {
        private String member1Name;
        private String member2Name;
        private BigDecimal compatibilityScore;
        private String suggestion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BestPairInfo {
        private String member1Name;
        private String member2Name;
        private BigDecimal compatibilityScore;
        private String collaborationType;
    }
}