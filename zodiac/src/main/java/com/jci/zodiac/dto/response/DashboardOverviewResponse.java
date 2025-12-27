package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for main dashboard overview
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardOverviewResponse {

    // Quick Stats
    private QuickStats quickStats;

    // Zodiac Insights
    private ZodiacInsights zodiacInsights;

    // Recent Activity
    private List<RecentActivity> recentActivities;

    // Upcoming Events
    private List<UpcomingEvent> upcomingEvents;

    // Alerts
    private List<String> alerts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuickStats {
        private Long totalMembers;
        private Long activeMembers;
        private Long totalDepartments;
        private Long activeTeams;
        private BigDecimal averageOrganizationCompatibility;
        private String compatibilityTrend; // UP, DOWN, STABLE
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ZodiacInsights {
        private String mostCommonSign;
        private String mostCommonElement;
        private Integer totalConflicts;
        private Integer excellentPairs;
        private String organizationVibe;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentActivity {
        private String type; // MEMBER_JOINED, TEAM_CREATED, etc.
        private String description;
        private LocalDate date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpcomingEvent {
        private String type; // BIRTHDAY, TEAM_DEADLINE, etc.
        private String description;
        private LocalDate date;
        private Integer daysUntil;
    }
}