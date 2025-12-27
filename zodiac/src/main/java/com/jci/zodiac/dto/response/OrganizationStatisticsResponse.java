package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for organization-wide statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationStatisticsResponse {

    // Member Statistics
    private MemberStats memberStats;

    // Department Statistics
    private DepartmentStats departmentStats;

    // Team Statistics
    private TeamStats teamStats;

    // Zodiac Statistics
    private ZodiacStats zodiacStats;

    // Compatibility Statistics
    private CompatibilityStats compatibilityStats;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberStats {
        private Long total;
        private Long active;
        private Long inactive;
        private Long onLeave;
        private Long newThisMonth;
        private Map<String, Long> byDepartment;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DepartmentStats {
        private Long total;
        private Long active;
        private Long withLeaders;
        private Long withoutMembers;
        private String largestDepartment;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TeamStats {
        private Long total;
        private Long active;
        private Long planning;
        private Long completed;
        private Long withConflicts;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ZodiacStats {
        private Map<Member.ZodiacSign, Long> signDistribution;
        private Map<Member.ZodiacElement, Long> elementDistribution;
        private String mostCommonSign;
        private String leastCommonSign;
        private String dominantElement;
        private Boolean isBalanced;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CompatibilityStats {
        private BigDecimal averageOrgCompatibility;
        private Integer totalExcellentPairs;
        private Integer totalGoodPairs;
        private Integer totalConflictPairs;
        private Integer criticalConflicts;
    }
}