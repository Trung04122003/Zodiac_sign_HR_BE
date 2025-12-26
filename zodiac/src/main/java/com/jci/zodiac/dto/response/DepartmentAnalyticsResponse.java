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
 * DTO for department analytics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAnalyticsResponse {

    private Long departmentId;
    private String departmentName;
    private String departmentCode;

    // Member Statistics
    private Integer totalMembers;
    private Integer activeMembers;
    private Integer inactiveMembers;

    // Zodiac Distribution
    private Map<Member.ZodiacSign, Long> zodiacDistribution;
    private String mostCommonZodiacSign;
    private String leastCommonZodiacSign;

    // Element Balance
    private Map<Member.ZodiacElement, Long> elementBalance;
    private boolean isElementBalanced;
    private List<String> missingElements;
    private String dominantElement;

    // Compatibility Analysis
    private BigDecimal averageTeamCompatibility;
    private String teamCompatibilityLevel;
    private Integer potentialConflictCount;

    // Team Insights
    private List<String> teamStrengths;
    private List<String> teamChallenges;
    private List<String> recommendations;

    // Top Performers
    private List<MemberSummaryResponse> topCompatibleMembers;
}