package com.jci.zodiac.service;

import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.Department;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.Team;
import com.jci.zodiac.entity.ZodiacCompatibility;
import com.jci.zodiac.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DashboardService - Business logic for dashboard and analytics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;
    private final CompatibilityService compatibilityService;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Get main dashboard overview
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardOverview", unless = "#result == null")
    public DashboardOverviewResponse getDashboardOverview() {
        log.info("Generating dashboard overview");

        // Quick Stats
        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.countActive();
        long totalDepartments = departmentRepository.count();
        long activeTeams = teamRepository.countByStatus(Team.Status.Active);

        // Average organization compatibility
        BigDecimal avgCompatibility = calculateAverageOrgCompatibility();
        String compatibilityTrend = determineCompatibilityTrend(avgCompatibility);

        DashboardOverviewResponse.QuickStats quickStats = DashboardOverviewResponse.QuickStats.builder()
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .totalDepartments(totalDepartments)
                .activeTeams(activeTeams)
                .averageOrganizationCompatibility(avgCompatibility)
                .compatibilityTrend(compatibilityTrend)
                .build();

        // Zodiac Insights
        List<Member> allActiveMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);

        Member.ZodiacSign mostCommonSign = zodiacUtilityService.getMostCommonZodiacSign(allActiveMembers);
        Map<Member.ZodiacElement, Long> elementBalance = zodiacUtilityService.calculateElementBalance(allActiveMembers);
        Member.ZodiacElement mostCommonElement = elementBalance.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        int totalConflicts = countTotalConflicts();
        int excellentPairs = countExcellentPairs();
        String organizationVibe = determineOrganizationVibe(mostCommonElement, avgCompatibility);

        DashboardOverviewResponse.ZodiacInsights zodiacInsights = DashboardOverviewResponse.ZodiacInsights.builder()
                .mostCommonSign(mostCommonSign != null ? mostCommonSign.name() : "N/A")
                .mostCommonElement(mostCommonElement != null ? mostCommonElement.name() : "N/A")
                .totalConflicts(totalConflicts)
                .excellentPairs(excellentPairs)
                .organizationVibe(organizationVibe)
                .build();

        // Recent Activities
        List<DashboardOverviewResponse.RecentActivity> recentActivities = getRecentActivities();

        // Upcoming Events
        List<DashboardOverviewResponse.UpcomingEvent> upcomingEvents = getUpcomingEvents();

        // Alerts
        List<String> alerts = generateAlerts(totalConflicts, elementBalance);

        return DashboardOverviewResponse.builder()
                .quickStats(quickStats)
                .zodiacInsights(zodiacInsights)
                .recentActivities(recentActivities)
                .upcomingEvents(upcomingEvents)
                .alerts(alerts)
                .build();
    }

    /**
     * Get zodiac distribution data for pie chart
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "zodiacDistribution", unless = "#result == null")
    public ChartDataResponse getZodiacDistribution() {
        log.info("Generating zodiac distribution chart data");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        Map<Member.ZodiacSign, Long> distribution = zodiacUtilityService.calculateZodiacDistribution(activeMembers);

        List<ChartDataResponse.ChartDataPoint> dataPoints = distribution.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> ChartDataResponse.ChartDataPoint.builder()
                        .label(entry.getKey().name())
                        .value(entry.getValue())
                        .color(getZodiacColor(entry.getKey()))
                        .additionalData(Map.of(
                                "symbol", zodiacUtilityService.getZodiacSymbol(entry.getKey()),
                                "percentage", calculatePercentage(entry.getValue(), activeMembers.size())
                        ))
                        .build())
                .sorted(Comparator.comparing(point -> point.getValue().longValue(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return ChartDataResponse.builder()
                .chartType("PIE")
                .title("Zodiac Sign Distribution")
                .data(dataPoints)
                .metadata(Map.of(
                        "totalMembers", activeMembers.size(),
                        "mostCommon", dataPoints.isEmpty() ? "N/A" : dataPoints.get(0).getLabel()
                ))
                .build();
    }

    /**
     * Get element balance data for bar chart
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "elementBalance", unless = "#result == null")
    public ChartDataResponse getElementBalance() {
        log.info("Generating element balance chart data");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        Map<Member.ZodiacElement, Long> balance = zodiacUtilityService.calculateElementBalance(activeMembers);

        List<ChartDataResponse.ChartDataPoint> dataPoints = Arrays.stream(Member.ZodiacElement.values())
                .map(element -> ChartDataResponse.ChartDataPoint.builder()
                        .label(element.name())
                        .value(balance.getOrDefault(element, 0L))
                        .color(getElementColor(element))
                        .additionalData(Map.of(
                                "emoji", getElementEmoji(element),
                                "percentage", calculatePercentage(balance.getOrDefault(element, 0L), activeMembers.size()),
                                "isBalanced", balance.getOrDefault(element, 0L) > 0
                        ))
                        .build())
                .collect(Collectors.toList());

        boolean isBalanced = zodiacUtilityService.isTeamBalanced(activeMembers);

        return ChartDataResponse.builder()
                .chartType("BAR")
                .title("Element Balance")
                .data(dataPoints)
                .metadata(Map.of(
                        "totalMembers", activeMembers.size(),
                        "isBalanced", isBalanced,
                        "missingElements", zodiacUtilityService.getMissingElements(activeMembers).stream()
                                .map(Enum::name)
                                .collect(Collectors.toList())
                ))
                .build();
    }

    /**
     * Get department breakdown
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "departmentBreakdown", unless = "#result == null")
    public ChartDataResponse getDepartmentBreakdown() {
        log.info("Generating department breakdown chart data");

        List<Department> departments = departmentRepository.findByIsActive(true);

        List<ChartDataResponse.ChartDataPoint> dataPoints = departments.stream()
                .map(dept -> ChartDataResponse.ChartDataPoint.builder()
                        .label(dept.getName())
                        .value(dept.getMemberCount())
                        .color(dept.getColorPrimary() != null ? dept.getColorPrimary() : "#3498DB")
                        .additionalData(Map.of(
                                "code", dept.getCode(),
                                "zodiacTheme", dept.getZodiacTheme() != null ? dept.getZodiacTheme().name() : "N/A",
                                "activeProjects", dept.getActiveProjectsCount() != null ? dept.getActiveProjectsCount() : 0
                        ))
                        .build())
                .sorted(Comparator.comparing(point -> point.getValue().longValue(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return ChartDataResponse.builder()
                .chartType("BAR")
                .title("Department Breakdown")
                .data(dataPoints)
                .metadata(Map.of(
                        "totalDepartments", departments.size(),
                        "largestDepartment", dataPoints.isEmpty() ? "N/A" : dataPoints.get(0).getLabel()
                ))
                .build();
    }

    /**
     * Get timeline data for new hires
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "timelineData", key = "#months", unless = "#result == null")
    public TimelineDataResponse getTimelineData(int months) {
        log.info("Generating timeline data for last {} months", months);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);

        List<Member> newHires = memberRepository.findRecentJoins(startDate);

        // Group by month
        Map<String, List<Member>> membersByMonth = newHires.stream()
                .collect(Collectors.groupingBy(member ->
                        member.getJoinDate().getMonth().name() + " " + member.getJoinDate().getYear()
                ));

        List<TimelineDataResponse.TimelineEvent> events = membersByMonth.entrySet().stream()
                .map(entry -> {
                    List<Member> monthMembers = entry.getValue();
                    Member.ZodiacSign mostCommonInMonth = zodiacUtilityService.getMostCommonZodiacSign(monthMembers);

                    return TimelineDataResponse.TimelineEvent.builder()
                            .date(monthMembers.get(0).getJoinDate())
                            .eventType("NEW_HIRES")
                            .description(String.format("%d new member(s) joined", monthMembers.size()))
                            .zodiacSign(mostCommonInMonth != null ? mostCommonInMonth.name() : "Mixed")
                            .zodiacSymbol(mostCommonInMonth != null ? zodiacUtilityService.getZodiacSymbol(mostCommonInMonth) : "🌟")
                            .count(monthMembers.size())
                            .build();
                })
                .sorted(Comparator.comparing(TimelineDataResponse.TimelineEvent::getDate))
                .collect(Collectors.toList());

        return TimelineDataResponse.builder()
                .timelineName("New Hires Timeline")
                .startDate(startDate)
                .endDate(endDate)
                .events(events)
                .build();
    }

    /**
     * Get organization-wide statistics
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "organizationStats", unless = "#result == null")
    public OrganizationStatisticsResponse getOrganizationStatistics() {
        log.info("Generating organization statistics");

        // Member Stats
        List<Member> allMembers = memberRepository.findAll();
        long activeCount = memberRepository.countActive();
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Member> newThisMonth = memberRepository.findRecentJoins(oneMonthAgo);

        Map<String, Long> membersByDepartment = allMembers.stream()
                .filter(m -> m.getDepartmentId() != null)
                .collect(Collectors.groupingBy(
                        m -> departmentRepository.findById(m.getDepartmentId())
                                .map(Department::getName)
                                .orElse("Unassigned"),
                        Collectors.counting()
                ));

        OrganizationStatisticsResponse.MemberStats memberStats = OrganizationStatisticsResponse.MemberStats.builder()
                .total((long) allMembers.size())
                .active(activeCount)
                .inactive(allMembers.stream().filter(m -> m.getMembershipStatus() == Member.MembershipStatus.Inactive).count())
                .onLeave(allMembers.stream().filter(m -> m.getMembershipStatus() == Member.MembershipStatus.OnLeave).count())
                .newThisMonth((long) newThisMonth.size())
                .byDepartment(membersByDepartment)
                .build();

        // Department Stats
        List<Department> allDepartments = departmentRepository.findAll();
        OrganizationStatisticsResponse.DepartmentStats departmentStats = OrganizationStatisticsResponse.DepartmentStats.builder()
                .total((long) allDepartments.size())
                .active(departmentRepository.countActive())
                .withLeaders(allDepartments.stream().filter(d -> d.getLeadMemberId() != null).count())
                .withoutMembers(allDepartments.stream().filter(d -> d.getMemberCount() == 0).count())
                .largestDepartment(allDepartments.stream()
                        .max(Comparator.comparing(Department::getMemberCount))
                        .map(Department::getName)
                        .orElse("N/A"))
                .build();

        // Team Stats
        List<Team> allTeams = teamRepository.findAll();
        OrganizationStatisticsResponse.TeamStats teamStats = OrganizationStatisticsResponse.TeamStats.builder()
                .total((long) allTeams.size())
                .active(teamRepository.countByStatus(Team.Status.Active))
                .planning(teamRepository.countByStatus(Team.Status.Planning))
                .completed(teamRepository.countByStatus(Team.Status.Completed))
                .withConflicts((long) teamRepository.findTeamsWithConflicts().size())
                .build();

        // Zodiac Stats
        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        Map<Member.ZodiacSign, Long> zodiacDist = zodiacUtilityService.calculateZodiacDistribution(activeMembers);
        Map<Member.ZodiacElement, Long> elementDist = zodiacUtilityService.calculateElementBalance(activeMembers);

        OrganizationStatisticsResponse.ZodiacStats zodiacStats = OrganizationStatisticsResponse.ZodiacStats.builder()
                .signDistribution(zodiacDist)
                .elementDistribution(elementDist)
                .mostCommonSign(zodiacUtilityService.getMostCommonZodiacSign(activeMembers) != null ?
                        zodiacUtilityService.getMostCommonZodiacSign(activeMembers).name() : "N/A")
                .leastCommonSign(zodiacUtilityService.getLeastCommonZodiacSign(activeMembers) != null ?
                        zodiacUtilityService.getLeastCommonZodiacSign(activeMembers).name() : "N/A")
                .dominantElement(elementDist.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(e -> e.getKey().name())
                        .orElse("N/A"))
                .isBalanced(zodiacUtilityService.isTeamBalanced(activeMembers))
                .build();

        // Compatibility Stats
        BigDecimal avgCompat = calculateAverageOrgCompatibility();
        int excellentPairs = countExcellentPairs();
        int goodPairs = countGoodPairs();
        int conflictPairs = countConflictPairs();
        int criticalConflicts = countCriticalConflicts();

        OrganizationStatisticsResponse.CompatibilityStats compatibilityStats =
                OrganizationStatisticsResponse.CompatibilityStats.builder()
                        .averageOrgCompatibility(avgCompat)
                        .totalExcellentPairs(excellentPairs)
                        .totalGoodPairs(goodPairs)
                        .totalConflictPairs(conflictPairs)
                        .criticalConflicts(criticalConflicts)
                        .build();

        return OrganizationStatisticsResponse.builder()
                .memberStats(memberStats)
                .departmentStats(departmentStats)
                .teamStats(teamStats)
                .zodiacStats(zodiacStats)
                .compatibilityStats(compatibilityStats)
                .build();
    }

    /**
     * Get compatibility matrix for heatmap
     */
    @Transactional(readOnly = true)
    public CompatibilityMatrixResponse getCompatibilityMatrix(Long departmentId) {
        log.info("Generating compatibility matrix for department: {}", departmentId);

        List<Member> members;
        if (departmentId != null) {
            members = memberRepository.findByDepartmentId(departmentId);
        } else {
            members = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        }

        // Limit to first 20 members for performance
        if (members.size() > 20) {
            members = members.subList(0, 20);
        }

        List<String> labels = members.stream()
                .map(m -> m.getFullName() + " (" + m.getZodiacSign().name() + ")")
                .collect(Collectors.toList());

        List<List<CompatibilityMatrixResponse.MatrixCell>> matrix = new ArrayList<>();

        for (Member member1 : members) {
            List<CompatibilityMatrixResponse.MatrixCell> row = new ArrayList<>();

            for (Member member2 : members) {
                if (member1.getId().equals(member2.getId())) {
                    // Diagonal cells
                    row.add(CompatibilityMatrixResponse.MatrixCell.builder()
                            .value(BigDecimal.valueOf(100))
                            .level("Self")
                            .color("#E8E8E8")
                            .build());
                } else {
                    ZodiacCompatibility compat = compatibilityService.getCompatibilityByMembers(
                            member1.getId(), member2.getId()
                    );

                    row.add(CompatibilityMatrixResponse.MatrixCell.builder()
                            .value(compat.getOverallScore())
                            .level(compat.getCompatibilityLevel().name())
                            .color(getCompatibilityColor(compat.getOverallScore()))
                            .build());
                }
            }

            matrix.add(row);
        }

        return CompatibilityMatrixResponse.builder()
                .rowLabels(labels)
                .columnLabels(labels)
                .matrix(matrix)
                .build();
    }

    /**
     * Refresh all dashboard caches
     */
    @CacheEvict(value = {"dashboardOverview", "zodiacDistribution", "elementBalance",
            "departmentBreakdown", "timelineData", "organizationStats"}, allEntries = true)
    public void refreshCache() {
        log.info("Dashboard cache refreshed");
    }

    // ==================== Helper Methods ====================

    private BigDecimal calculateAverageOrgCompatibility() {
        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);

        if (activeMembers.size() < 2) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalScore = BigDecimal.ZERO;
        int pairCount = 0;

        for (int i = 0; i < Math.min(activeMembers.size(), 10); i++) {
            for (int j = i + 1; j < Math.min(activeMembers.size(), 10); j++) {
                try {
                    ZodiacCompatibility compat = compatibilityService.getCompatibilityByMembers(
                            activeMembers.get(i).getId(),
                            activeMembers.get(j).getId()
                    );
                    totalScore = totalScore.add(compat.getOverallScore());
                    pairCount++;
                } catch (Exception e) {
                    log.warn("Error calculating compatibility: {}", e.getMessage());
                }
            }
        }

        if (pairCount == 0) {
            return BigDecimal.ZERO;
        }

        return totalScore.divide(BigDecimal.valueOf(pairCount), 2, RoundingMode.HALF_UP);
    }

    private String determineCompatibilityTrend(BigDecimal avgScore) {
        if (avgScore.compareTo(BigDecimal.valueOf(70)) >= 0) return "UP";
        if (avgScore.compareTo(BigDecimal.valueOf(50)) >= 0) return "STABLE";
        return "DOWN";
    }

    private int countTotalConflicts() {
        return compatibilityRepository.findLowCompatibilityPairs(BigDecimal.valueOf(40)).size();
    }

    private int countExcellentPairs() {
        return compatibilityRepository.findHighCompatibilityPairs(BigDecimal.valueOf(80)).size();
    }

    private int countGoodPairs() {
        return compatibilityRepository.findHighCompatibilityPairs(BigDecimal.valueOf(65)).size();
    }

    private int countConflictPairs() {
        return compatibilityRepository.findLowCompatibilityPairs(BigDecimal.valueOf(50)).size();
    }

    private int countCriticalConflicts() {
        return compatibilityRepository.findLowCompatibilityPairs(BigDecimal.valueOf(30)).size();
    }

    private String determineOrganizationVibe(Member.ZodiacElement dominantElement, BigDecimal avgCompat) {
        if (dominantElement == null) return "Balanced and Harmonious";

        String elementVibe = switch (dominantElement) {
            case Fire -> "Energetic and Dynamic 🔥";
            case Earth -> "Stable and Practical 🌍";
            case Air -> "Innovative and Communicative 💨";
            case Water -> "Collaborative and Intuitive 💧";
        };

        if (avgCompat.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return elementVibe + " with Excellent Team Chemistry";
        }

        return elementVibe;
    }

    private List<DashboardOverviewResponse.RecentActivity> getRecentActivities() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        List<Member> recentJoins = memberRepository.findRecentJoins(oneWeekAgo);

        return recentJoins.stream()
                .limit(5)
                .map(member -> new DashboardOverviewResponse.RecentActivity(
                        "MEMBER_JOINED",
                        member.getFullName() + " (" + member.getZodiacSign().name() + " " +
                                zodiacUtilityService.getZodiacSymbol(member.getZodiacSign()) + ") joined",
                        member.getJoinDate()
                ))
                .collect(Collectors.toList());
    }

    private List<DashboardOverviewResponse.UpcomingEvent> getUpcomingEvents() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);

        List<Member> upcomingBirthdays = memberRepository.findUpcomingBirthdays(today, nextMonth);

        return upcomingBirthdays.stream()
                .limit(5)
                .map(member -> {
                    LocalDate thisBirthday = LocalDate.of(
                            today.getYear(),
                            member.getDateOfBirth().getMonth(),
                            member.getDateOfBirth().getDayOfMonth()
                    );
                    if (thisBirthday.isBefore(today)) {
                        thisBirthday = thisBirthday.plusYears(1);
                    }

                    long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, thisBirthday);

                    return new DashboardOverviewResponse.UpcomingEvent(
                            "BIRTHDAY",
                            member.getFullName() + "'s birthday 🎂",
                            thisBirthday,
                            (int) daysUntil
                    );
                })
                .collect(Collectors.toList());
    }

    private List<String> generateAlerts(int conflicts, Map<Member.ZodiacElement, Long> elementBalance) {
        List<String> alerts = new ArrayList<>();

        if (conflicts > 5) {
            alerts.add("⚠️ High number of compatibility conflicts detected (" + conflicts + " pairs)");
        }

        List<Member.ZodiacElement> missingElements = elementBalance.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!missingElements.isEmpty()) {
            alerts.add("📊 Missing elements: " + missingElements.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));
        }

        if (alerts.isEmpty()) {
            alerts.add("✅ All systems looking good! No critical alerts.");
        }

        return alerts;
    }

    private double calculatePercentage(long count, int total) {
        if (total == 0) return 0.0;
        return Math.round((count * 100.0 / total) * 10.0) / 10.0;
    }

    private String getZodiacColor(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "#E74C3C";
            case Taurus -> "#27AE60";
            case Gemini -> "#F1C40F";
            case Cancer -> "#3498DB";
            case Leo -> "#E67E22";
            case Virgo -> "#95A5A6";
            case Libra -> "#E91E63";
            case Scorpio -> "#8E44AD";
            case Sagittarius -> "#9B59B6";
            case Capricorn -> "#34495E";
            case Aquarius -> "#1ABC9C";
            case Pisces -> "#16A085";
        };
    }

    private String getElementColor(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "#E74C3C";
            case Earth -> "#27AE60";
            case Air -> "#3498DB";
            case Water -> "#1ABC9C";
        };
    }

    private String getElementEmoji(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "🔥";
            case Earth -> "🌍";
            case Air -> "💨";
            case Water -> "💧";
        };
    }

    private String getCompatibilityColor(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return "#27AE60"; // Green
        if (score.compareTo(BigDecimal.valueOf(65)) >= 0) return "#52C41A"; // Light Green
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return "#FAAD14"; // Yellow
        if (score.compareTo(BigDecimal.valueOf(35)) >= 0) return "#FA8C16"; // Orange
        return "#E74C3C"; // Red
    }
}