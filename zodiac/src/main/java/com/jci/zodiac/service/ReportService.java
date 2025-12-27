package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.GenerateReportRequest;
import com.jci.zodiac.dto.request.TeamPredictionRequest;
import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.*;
import com.jci.zodiac.exception.BadRequestException;
import com.jci.zodiac.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ReportService - Business logic for reports and analytics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;
    private final CompatibilityService compatibilityService;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Get zodiac analytics report
     */
    @Transactional(readOnly = true)
    public ZodiacAnalyticsResponse getZodiacAnalytics() {
        log.info("Generating zodiac analytics report");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        List<Member> allMembers = memberRepository.findAll();

        // Zodiac distribution
        Map<Member.ZodiacSign, Long> zodiacDistribution = zodiacUtilityService.calculateZodiacDistribution(activeMembers);

        // Element distribution
        Map<Member.ZodiacElement, Long> elementDistribution = zodiacUtilityService.calculateElementBalance(activeMembers);

        // Find most/least common
        Member.ZodiacSign mostCommon = zodiacUtilityService.getMostCommonZodiacSign(activeMembers);
        Member.ZodiacSign leastCommon = zodiacUtilityService.getLeastCommonZodiacSign(activeMembers);

        // Hiring patterns by month
        Map<String, Map<Member.ZodiacSign, Long>> hiringPatterns = calculateHiringPatternsByMonth(allMembers);

        // Insights
        List<String> insights = generateZodiacInsights(activeMembers, zodiacDistribution, elementDistribution);

        // Recommendations
        List<String> recommendations = generateZodiacRecommendations(activeMembers, elementDistribution);

        // Preferences analysis
        String preferredElement = elementDistribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse("N/A");

        return ZodiacAnalyticsResponse.builder()
                .totalMembers(activeMembers.size())
                .zodiacDistribution(zodiacDistribution)
                .elementDistribution(elementDistribution)
                .mostCommonSign(mostCommon != null ? mostCommon.name() : "N/A")
                .leastCommonSign(leastCommon != null ? leastCommon.name() : "N/A")
                .preferredElement(preferredElement)
                .hiringPatterns(hiringPatterns)
                .insights(insights)
                .recommendations(recommendations)
                .generatedAt(LocalDate.now())
                .build();
    }

    /**
     * Get compatibility matrix for report
     */
    @Transactional(readOnly = true)
    public CompatibilityMatrixResponse getCompatibilityMatrixForReport(Long departmentId, int maxMembers) {
        log.info("Generating compatibility matrix: department={}, maxMembers={}", departmentId, maxMembers);

        List<Member> members;
        if (departmentId != null) {
            members = memberRepository.findByDepartmentId(departmentId);
        } else {
            members = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        }

        // Limit members for performance
        if (members.size() > maxMembers) {
            members = members.subList(0, maxMembers);
        }

        List<String> labels = members.stream()
                .map(m -> String.format("%s (%s)", m.getFullName(), m.getZodiacSign().name()))
                .collect(Collectors.toList());

        List<List<CompatibilityMatrixResponse.MatrixCell>> matrix = new ArrayList<>();

        for (Member member1 : members) {
            List<CompatibilityMatrixResponse.MatrixCell> row = new ArrayList<>();

            for (Member member2 : members) {
                if (member1.getId().equals(member2.getId())) {
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
     * Get department composition
     */
    @Transactional(readOnly = true)
    public DepartmentCompositionResponse getDepartmentComposition(Long departmentId) {
        log.info("Generating department composition for department: {}", departmentId);

        List<DepartmentCompositionResponse.DepartmentZodiacBreakdown> breakdowns = new ArrayList<>();

        List<Department> departments;
        if (departmentId != null) {
            departments = List.of(departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new BadRequestException("Department not found")));
        } else {
            departments = departmentRepository.findAll();
        }

        for (Department dept : departments) {
            List<Member> members = memberRepository.findByDepartmentId(dept.getId());

            Map<Member.ZodiacSign, Long> zodiacDist = zodiacUtilityService.calculateZodiacDistribution(members);
            Map<Member.ZodiacElement, Long> elementBalance = zodiacUtilityService.calculateElementBalance(members);

            boolean isBalanced = zodiacUtilityService.isTeamBalanced(members);
            List<String> missingElements = zodiacUtilityService.getMissingElements(members).stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());

            String dominantSign = zodiacDist.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(e -> e.getKey().name())
                    .orElse("N/A");

            breakdowns.add(DepartmentCompositionResponse.DepartmentZodiacBreakdown.builder()
                    .departmentId(dept.getId())
                    .departmentName(dept.getName())
                    .departmentCode(dept.getCode())
                    .totalMembers(members.size())
                    .zodiacDistribution(zodiacDist)
                    .elementBalance(elementBalance)
                    .isBalanced(isBalanced)
                    .missingElements(missingElements)
                    .dominantSign(dominantSign)
                    .teamVibe(generateDepartmentVibe(members, elementBalance))
                    .build());
        }

        return DepartmentCompositionResponse.builder()
                .departments(breakdowns)
                .totalDepartments(departments.size())
                .generatedAt(LocalDate.now())
                .build();
    }

    /**
     * Get fun statistics
     */
    @Transactional(readOnly = true)
    public FunStatsResponse getFunStats() {
        log.info("Generating fun statistics");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);

        // Find most compatible pair
        CompatibilityService.MemberPairCompatibility bestPair = findBestCompatiblePair(activeMembers);

        // Find least compatible pair
        CompatibilityService.MemberPairCompatibility worstPair = findLeastCompatiblePair(activeMembers);

        // Most balanced team
        Team mostBalancedTeam = findMostBalancedTeam();

        // Zodiac of the month
        String zodiacOfMonth = determineZodiacOfMonth();

        // Sagittarius count (special! ♐)
        long sagittariusCount = zodiacUtilityService.countSagittarius(activeMembers);
        double sagittariusPercentage = zodiacUtilityService.getSagittariusPercentage(activeMembers);

        // Fun facts
        List<String> funFacts = generateFunFacts(activeMembers, sagittariusCount);

        return FunStatsResponse.builder()
                .mostCompatiblePair(formatPairInfo(bestPair))
                .leastCompatiblePair(formatPairInfo(worstPair))
                .mostBalancedTeam(mostBalancedTeam != null ? mostBalancedTeam.getName() : "N/A")
                .zodiacOfTheMonth(zodiacOfMonth)
                .sagittariusCount(sagittariusCount)
                .sagittariusPercentage(sagittariusPercentage)
                .funFacts(funFacts)
                .build();
    }

    /**
     * Generate comprehensive compatibility report
     */
    @Transactional(readOnly = true)
    public CompatibilityReport generateCompatibilityReport(GenerateReportRequest request) {
        log.info("Generating compatibility report: {}", request.getReportType());

        switch (request.getReportType()) {
            case "PAIR":
                return generatePairReport(request);
            case "TEAM":
                return generateTeamReport(request);
            case "DEPARTMENT":
                return generateDepartmentReport(request);
            default:
                throw new BadRequestException("Invalid report type: " + request.getReportType());
        }
    }

    /**
     * Generate PDF report (placeholder - requires PDF library)
     */
    public byte[] generatePdfReport(GenerateReportRequest request) {
        log.info("Generating PDF report: {}", request.getReportType());

        // TODO: Implement PDF generation using iText or Apache PDFBox
        // For now, return placeholder
        String placeholder = "PDF Report Generation - Coming Soon!\n" +
                "Report Type: " + request.getReportType() + "\n" +
                "Generated: " + LocalDate.now();

        return placeholder.getBytes();
    }

    /**
     * Generate Excel report (placeholder - requires Apache POI)
     */
    public byte[] generateExcelReport(GenerateReportRequest request) {
        log.info("Generating Excel report: {}", request.getReportType());

        // TODO: Implement Excel generation using Apache POI
        // For now, return placeholder
        String placeholder = "Excel Report Generation - Coming Soon!\n" +
                "Report Type: " + request.getReportType() + "\n" +
                "Generated: " + LocalDate.now();

        return placeholder.getBytes();
    }

    /**
     * Get hiring trends
     */
    @Transactional(readOnly = true)
    public HiringTrendsResponse getHiringTrends(int months) {
        log.info("Generating hiring trends for last {} months", months);

        LocalDate startDate = LocalDate.now().minusMonths(months);
        List<Member> recentHires = memberRepository.findRecentJoins(startDate);

        Map<String, HiringTrendsResponse.MonthlyHiring> monthlyData = new LinkedHashMap<>();

        for (Member member : recentHires) {
            String monthKey = member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            monthlyData.computeIfAbsent(monthKey, k -> HiringTrendsResponse.MonthlyHiring.builder()
                    .month(monthKey)
                    .totalHires(0)
                    .zodiacBreakdown(new HashMap<>())
                    .build());

            HiringTrendsResponse.MonthlyHiring monthData = monthlyData.get(monthKey);
            monthData.setTotalHires(monthData.getTotalHires() + 1);

            String zodiacKey = member.getZodiacSign().name();
            monthData.getZodiacBreakdown().put(zodiacKey,
                    monthData.getZodiacBreakdown().getOrDefault(zodiacKey, 0L) + 1);
        }

        return HiringTrendsResponse.builder()
                .months(months)
                .startDate(startDate)
                .endDate(LocalDate.now())
                .monthlyData(new ArrayList<>(monthlyData.values()))
                .totalHires(recentHires.size())
                .build();
    }

    /**
     * Get team predictions
     */
    @Transactional(readOnly = true)
    public TeamPredictionsResponse getTeamPredictions(TeamPredictionRequest request) {
        log.info("Generating team predictions for {} members", request.getMemberIds().size());

        CompatibilityService.TeamCompatibilityResult compatResult =
                compatibilityService.calculateTeamCompatibility(request.getMemberIds());

        List<String> predictions = new ArrayList<>();
        predictions.add(generatePerformancePrediction(compatResult));
        predictions.add(generateCommunicationPrediction(compatResult));
        predictions.add(generateSynergyPrediction(compatResult));

        List<String> risks = generateTeamRisks(compatResult);
        List<String> opportunities = generateTeamOpportunities(compatResult);

        return TeamPredictionsResponse.builder()
                .teamSize(request.getMemberIds().size())
                .predictedCompatibility(compatResult.averageCompatibilityScore())
                .predictions(predictions)
                .risks(risks)
                .opportunities(opportunities)
                .confidence(calculatePredictionConfidence(compatResult))
                .build();
    }

    /**
     * Get team recommendations
     */
    @Transactional(readOnly = true)
    public TeamRecommendationsResponse getTeamRecommendations(int teamSize, Long departmentId) {
        log.info("Generating team recommendations: size={}, department={}", teamSize, departmentId);

        List<Member> availableMembers;
        if (departmentId != null) {
            availableMembers = memberRepository.findByDepartmentId(departmentId);
        } else {
            availableMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        }

        List<TeamRecommendationsResponse.RecommendedTeam> recommendations = new ArrayList<>();

        // Generate top 3 team combinations
        for (int i = 0; i < 3; i++) {
            List<Member> team = selectOptimalTeam(availableMembers, teamSize, i);

            if (team.size() >= 2) {
                List<Long> memberIds = team.stream().map(Member::getId).collect(Collectors.toList());
                CompatibilityService.TeamCompatibilityResult compatResult =
                        compatibilityService.calculateTeamCompatibility(memberIds);

                recommendations.add(TeamRecommendationsResponse.RecommendedTeam.builder()
                        .teamNumber(i + 1)
                        .members(team.stream().map(Member::getFullName).collect(Collectors.toList()))
                        .compatibility(compatResult.averageCompatibilityScore())
                        .elementBalance(compatResult.elementBalance())
                        .reasoning(generateTeamReasoning(compatResult))
                        .build());
            }
        }

        return TeamRecommendationsResponse.builder()
                .targetSize(teamSize)
                .recommendations(recommendations)
                .build();
    }

    /**
     * Get element recommendations
     */
    @Transactional(readOnly = true)
    public ElementRecommendationsResponse getElementRecommendations() {
        log.info("Generating element recommendations");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        Map<Member.ZodiacElement, Long> currentBalance = zodiacUtilityService.calculateElementBalance(activeMembers);

        boolean isBalanced = zodiacUtilityService.isTeamBalanced(activeMembers);
        List<Member.ZodiacElement> missingElements = zodiacUtilityService.getMissingElements(activeMembers);

        List<String> recommendations = new ArrayList<>();

        if (!isBalanced) {
            for (Member.ZodiacElement element : missingElements) {
                recommendations.add(String.format("Consider hiring %s signs (%s) to achieve balance",
                        element.name(), getSignsForElement(element)));
            }
        } else {
            recommendations.add("✅ Perfect element balance achieved! All 4 elements are represented.");
        }

        // Add specific zodiac sign recommendations
        List<String> suggestedSigns = missingElements.stream()
                .flatMap(element -> getZodiacSignsForElement(element).stream())
                .map(sign -> sign.name() + " " + zodiacUtilityService.getZodiacSymbol(sign))
                .collect(Collectors.toList());

        return ElementRecommendationsResponse.builder()
                .currentBalance(currentBalance)
                .isBalanced(isBalanced)
                .missingElements(missingElements.stream().map(Enum::name).collect(Collectors.toList()))
                .recommendations(recommendations)
                .suggestedZodiacSigns(suggestedSigns)
                .build();
    }

    /**
     * Preview report
     */
    @Transactional(readOnly = true)
    public ReportPreviewResponse previewReport(GenerateReportRequest request) {
        log.info("Generating report preview: {}", request.getReportType());

        CompatibilityReport report = generateCompatibilityReport(request);

        return ReportPreviewResponse.builder()
                .reportType(request.getReportType())
                .title(report.getTitle())
                .summary(report.getSummary())
                .pageCount(calculatePageCount(report))
                .estimatedSize("~500KB")
                .availableFormats(List.of("PDF", "Excel", "JSON"))
                .preview(report)
                .build();
    }

    // ==================== Helper Methods ====================

    private Map<String, Map<Member.ZodiacSign, Long>> calculateHiringPatternsByMonth(List<Member> members) {
        Map<String, Map<Member.ZodiacSign, Long>> patterns = new LinkedHashMap<>();

        for (Member member : members) {
            String monthKey = member.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            patterns.computeIfAbsent(monthKey, k -> new HashMap<>());

            Map<Member.ZodiacSign, Long> monthData = patterns.get(monthKey);
            monthData.put(member.getZodiacSign(), monthData.getOrDefault(member.getZodiacSign(), 0L) + 1);
        }

        return patterns;
    }

    private List<String> generateZodiacInsights(List<Member> members,
                                                Map<Member.ZodiacSign, Long> zodiacDist,
                                                Map<Member.ZodiacElement, Long> elementDist) {

        List<String> insights = new ArrayList<>();

        Member.ZodiacElement dominantElement = elementDist.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (dominantElement != null) {
            insights.add(String.format("Your organization favors %s signs, bringing %s energy to the team",
                    dominantElement.name(), getElementCharacteristic(dominantElement)));
        }

        long sagittariusCount = zodiacUtilityService.countSagittarius(members);
        if (sagittariusCount > 0) {
            insights.add(String.format("♐ Sagittarius power: %d adventurous leaders in your team!", sagittariusCount));
        }

        return insights;
    }

    private List<String> generateZodiacRecommendations(List<Member> members,
                                                       Map<Member.ZodiacElement, Long> elementDist) {

        List<String> recommendations = new ArrayList<>();

        List<Member.ZodiacElement> missingElements = zodiacUtilityService.getMissingElements(members);

        if (!missingElements.isEmpty()) {
            recommendations.add("Consider diversifying with " +
                    missingElements.stream().map(Enum::name).collect(Collectors.joining(", ")) +
                    " signs for better team balance");
        }

        return recommendations;
    }

    private String generateDepartmentVibe(List<Member> members, Map<Member.ZodiacElement, Long> elementBalance) {
        if (members.isEmpty()) return "No members";

        Member.ZodiacElement dominant = elementBalance.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (dominant == null) return "Balanced";

        return switch (dominant) {
            case Fire -> "Dynamic and Action-Oriented 🔥";
            case Earth -> "Practical and Reliable 🌍";
            case Air -> "Innovative and Communicative 💨";
            case Water -> "Intuitive and Collaborative 💧";
        };
    }

    private CompatibilityService.MemberPairCompatibility findBestCompatiblePair(List<Member> members) {
        if (members.size() < 2) return null;

        CompatibilityService.MemberPairCompatibility best = null;
        BigDecimal bestScore = BigDecimal.ZERO;

        for (int i = 0; i < Math.min(members.size(), 10); i++) {
            for (int j = i + 1; j < Math.min(members.size(), 10); j++) {
                try {
                    ZodiacCompatibility compat = compatibilityService.getCompatibilityByMembers(
                            members.get(i).getId(), members.get(j).getId()
                    );

                    if (compat.getOverallScore().compareTo(bestScore) > 0) {
                        bestScore = compat.getOverallScore();
                        Member m1 = members.get(i);
                        Member m2 = members.get(j);

                        best = new CompatibilityService.MemberPairCompatibility(
                                m1.getId(), m1.getFullName(), m1.getMemberCode(), m1.getZodiacSign().name(),
                                m2.getId(), m2.getFullName(), m2.getMemberCode(), m2.getZodiacSign().name(),
                                compat.getOverallScore(),
                                compat.getCompatibilityLevel().name(),
                                compat.getBestCollaborationType()
                        );
                    }
                } catch (Exception e) {
                    log.warn("Error calculating compatibility: {}", e.getMessage());
                }
            }
        }

        return best;
    }

    private CompatibilityService.MemberPairCompatibility findLeastCompatiblePair(List<Member> members) {
        if (members.size() < 2) return null;

        CompatibilityService.MemberPairCompatibility worst = null;
        BigDecimal worstScore = BigDecimal.valueOf(100);

        for (int i = 0; i < Math.min(members.size(), 10); i++) {
            for (int j = i + 1; j < Math.min(members.size(), 10); j++) {
                try {
                    ZodiacCompatibility compat = compatibilityService.getCompatibilityByMembers(
                            members.get(i).getId(), members.get(j).getId()
                    );

                    if (compat.getOverallScore().compareTo(worstScore) < 0) {
                        worstScore = compat.getOverallScore();
                        Member m1 = members.get(i);
                        Member m2 = members.get(j);

                        worst = new CompatibilityService.MemberPairCompatibility(
                                m1.getId(), m1.getFullName(), m1.getMemberCode(), m1.getZodiacSign().name(),
                                m2.getId(), m2.getFullName(), m2.getMemberCode(), m2.getZodiacSign().name(),
                                compat.getOverallScore(),
                                compat.getCompatibilityLevel().name(),
                                compat.getBestCollaborationType()
                        );
                    }
                } catch (Exception e) {
                    log.warn("Error calculating compatibility: {}", e.getMessage());
                }
            }
        }

        return worst;
    }

    private Team findMostBalancedTeam() {
        List<Team> activeTeams = teamRepository.findByStatus(Team.Status.Active);

        return activeTeams.stream()
                .filter(team -> Boolean.TRUE.equals(team.getElementBalance() != null))
                .max(Comparator.comparing(team -> {
                    Map<String, Integer> balance = team.getElementBalance();
                    return balance != null ? balance.values().stream().filter(v -> v > 0).count() : 0;
                }))
                .orElse(null);
    }

    private String determineZodiacOfMonth() {
        LocalDate now = LocalDate.now();
        return zodiacUtilityService.calculateZodiacSign(now).name();
    }

    private List<String> generateFunFacts(List<Member> members, long sagittariusCount) {
        List<String> facts = new ArrayList<>();

        facts.add(String.format("🎯 %d out of %d members are Sagittarius - the adventurer's spirit is strong! ♐",
                sagittariusCount, members.size()));

        Map<Member.ZodiacSign, Long> distribution = zodiacUtilityService.calculateZodiacDistribution(members);
        long maxCount = distribution.values().stream().max(Long::compareTo).orElse(0L);

        if (maxCount > members.size() * 0.3) {
            facts.add("🌟 You have a dominant zodiac culture in your organization!");
        }

        return facts;
    }

    private String formatPairInfo(CompatibilityService.MemberPairCompatibility pair) {
        if (pair == null) return "N/A";
        return String.format("%s (%s) & %s (%s) - %.1f%%",
                pair.member1Name(), pair.member1Sign(),
                pair.member2Name(), pair.member2Sign(),
                pair.compatibilityScore());
    }

    private CompatibilityReport generatePairReport(GenerateReportRequest request) {
        // Implementation for pair compatibility report
        return CompatibilityReport.builder()
                .reportType("PAIR")
                .title("Pair Compatibility Report")
                .summary("Detailed compatibility analysis for two members")
                .build();
    }

    private CompatibilityReport generateTeamReport(GenerateReportRequest request) {
        // Implementation for team compatibility report
        return CompatibilityReport.builder()
                .reportType("TEAM")
                .title("Team Compatibility Report")
                .summary("Comprehensive team dynamics analysis")
                .build();
    }

    private CompatibilityReport generateDepartmentReport(GenerateReportRequest request) {
        // Implementation for department compatibility report
        return CompatibilityReport.builder()
                .reportType("DEPARTMENT")
                .title("Department Compatibility Report")
                .summary("Department-wide zodiac composition and compatibility")
                .build();
    }

    private String getCompatibilityColor(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return "#27AE60";
        if (score.compareTo(BigDecimal.valueOf(65)) >= 0) return "#52C41A";
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return "#FAAD14";
        if (score.compareTo(BigDecimal.valueOf(35)) >= 0) return "#FA8C16";
        return "#E74C3C";
    }

    private String getElementCharacteristic(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "passionate and dynamic";
            case Earth -> "practical and grounded";
            case Air -> "intellectual and communicative";
            case Water -> "emotional and intuitive";
        };
    }

    private String getSignsForElement(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "Aries, Leo, Sagittarius";
            case Earth -> "Taurus, Virgo, Capricorn";
            case Air -> "Gemini, Libra, Aquarius";
            case Water -> "Cancer, Scorpio, Pisces";
        };
    }

    private List<Member.ZodiacSign> getZodiacSignsForElement(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> List.of(Member.ZodiacSign.Aries, Member.ZodiacSign.Leo, Member.ZodiacSign.Sagittarius);
            case Earth -> List.of(Member.ZodiacSign.Taurus, Member.ZodiacSign.Virgo, Member.ZodiacSign.Capricorn);
            case Air -> List.of(Member.ZodiacSign.Gemini, Member.ZodiacSign.Libra, Member.ZodiacSign.Aquarius);
            case Water -> List.of(Member.ZodiacSign.Cancer, Member.ZodiacSign.Scorpio, Member.ZodiacSign.Pisces);
        };
    }

    private String generatePerformancePrediction(CompatibilityService.TeamCompatibilityResult result) {
        BigDecimal score = result.averageCompatibilityScore();
        if (score.compareTo(BigDecimal.valueOf(75)) >= 0) {
            return "🚀 High performance expected - team members will naturally sync and excel together";
        } else if (score.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return "📈 Good performance potential - minor adjustments will help optimize results";
        } else {
            return "⚠️ Moderate performance - requires active management and clear communication";
        }
    }

    private String generateCommunicationPrediction(CompatibilityService.TeamCompatibilityResult result) {
        long airCount = result.elementBalance().getOrDefault(Member.ZodiacElement.Air, 0L);
        if (airCount >= 2) {
            return "💬 Excellent communication expected - Air signs will facilitate smooth information flow";
        }
        return "📢 Communication requires attention - establish clear channels and regular check-ins";
    }

    private String generateSynergyPrediction(CompatibilityService.TeamCompatibilityResult result) {
        boolean balanced = result.elementBalance().size() >= 3;
        if (balanced) {
            return "⚡ Strong synergy potential - diverse elements will bring complementary strengths";
        }
        return "🔄 Synergy needs cultivation - focus on leveraging individual strengths";
    }

    private List<String> generateTeamRisks(CompatibilityService.TeamCompatibilityResult result) {
        List<String> risks = new ArrayList<>();

        if (!result.potentialConflicts().isEmpty()) {
            risks.add(String.format("⚠️ %d potential conflict pair(s) detected", result.potentialConflicts().size()));
        }

        if (result.elementBalance().size() < 3) {
            risks.add("📊 Limited element diversity may lead to one-sided approaches");
        }

        if (risks.isEmpty()) {
            risks.add("✅ No major risks identified");
        }

        return risks;
    }

    private List<String> generateTeamOpportunities(CompatibilityService.TeamCompatibilityResult result) {
        List<String> opportunities = new ArrayList<>();

        if (result.averageCompatibilityScore().compareTo(BigDecimal.valueOf(70)) >= 0) {
            opportunities.add("🌟 High compatibility creates strong foundation for collaboration");
        }

        if (result.elementBalance().size() >= 3) {
            opportunities.add("🎨 Diverse perspectives enable creative problem-solving");
        }

        if (!result.bestPairs().isEmpty()) {
            opportunities.add(String.format("💎 %d excellent pair(s) can drive team success", result.bestPairs().size()));
        }

        return opportunities;
    }

    private BigDecimal calculatePredictionConfidence(CompatibilityService.TeamCompatibilityResult result) {
        // Base confidence on team size and compatibility score
        BigDecimal baseConfidence = result.averageCompatibilityScore();

        // Adjust for team size (larger teams have more data)
        if (result.teamSize() >= 5) {
            baseConfidence = baseConfidence.add(BigDecimal.valueOf(5));
        }

        // Cap at 95%
        return baseConfidence.min(BigDecimal.valueOf(95));
    }

    private List<Member> selectOptimalTeam(List<Member> availableMembers, int targetSize, int variant) {
        // Simple selection algorithm - can be enhanced
        Collections.shuffle(new ArrayList<>(availableMembers));
        return availableMembers.stream()
                .skip(variant * targetSize)
                .limit(targetSize)
                .collect(Collectors.toList());
    }

    private String generateTeamReasoning(CompatibilityService.TeamCompatibilityResult result) {
        return String.format("Compatibility: %.1f%% | Balance: %d/4 elements | Conflicts: %d",
                result.averageCompatibilityScore(),
                result.elementBalance().size(),
                result.potentialConflicts().size());
    }

    private int calculatePageCount(CompatibilityReport report) {
        // Simple estimation based on content
        return 3 + (report.getKeyStrengths().size() / 5) + (report.getKeyWeaknesses().size() / 5);
    }
}