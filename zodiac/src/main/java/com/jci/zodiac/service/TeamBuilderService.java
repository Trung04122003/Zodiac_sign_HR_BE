package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.BuildTeamRequest;
import com.jci.zodiac.dto.request.OptimizeTeamRequest;
import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacCompatibility;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.mapper.MemberMapper;
import com.jci.zodiac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TeamBuilderService - Advanced team building and optimization
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeamBuilderService {

    private final MemberRepository memberRepository;
    private final CompatibilityService compatibilityService;
    private final MemberMapper memberMapper;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Build and analyze a team
     */
    @Transactional(readOnly = true)
    public TeamBuildResult buildTeam(BuildTeamRequest request) {
        log.info("Building team with {} members", request.getMemberIds().size());

        // Validate and fetch members
        List<Member> members = memberRepository.findAllById(request.getMemberIds());
        if (members.size() != request.getMemberIds().size()) {
            throw new ResourceNotFoundException("Some members not found");
        }

        // Calculate team compatibility
        CompatibilityService.TeamCompatibilityResult compatibilityResult =
                compatibilityService.calculateTeamCompatibility(request.getMemberIds());

        // Analyze element balance
        Map<Member.ZodiacElement, Long> elementBalance = compatibilityResult.elementBalance();
        boolean isBalanced = elementBalance.size() == 4;
        List<String> missingElements = Arrays.stream(Member.ZodiacElement.values())
                .filter(e -> !elementBalance.containsKey(e) || elementBalance.get(e) == 0)
                .map(Enum::name)
                .collect(Collectors.toList());

        // Identify conflicts
        List<TeamBuildResult.ConflictPair> conflicts = compatibilityResult.potentialConflicts().stream()
                .map(pc -> {
                    Member m1 = members.stream().filter(m -> m.getId().equals(pc.member1Id())).findFirst().orElse(null);
                    Member m2 = members.stream().filter(m -> m.getId().equals(pc.member2Id())).findFirst().orElse(null);

                    String riskLevel = determineRiskLevel(pc.compatibilityScore());
                    String suggestion = generateConflictSuggestion(m1, m2, pc.compatibilityScore());

                    return new TeamBuildResult.ConflictPair(
                            memberMapper.toSummaryResponse(m1),
                            memberMapper.toSummaryResponse(m2),
                            pc.compatibilityScore(),
                            riskLevel,
                            suggestion
                    );
                })
                .collect(Collectors.toList());

        // Identify best pairs
        List<TeamBuildResult.BestPair> bestPairs = compatibilityResult.bestPairs().stream()
                .map(bp -> {
                    Member m1 = members.stream().filter(m -> m.getId().equals(bp.member1Id())).findFirst().orElse(null);
                    Member m2 = members.stream().filter(m -> m.getId().equals(bp.member2Id())).findFirst().orElse(null);

                    ZodiacCompatibility comp = compatibilityService.getCompatibilityByMembers(m1.getId(), m2.getId());

                    return new TeamBuildResult.BestPair(
                            memberMapper.toSummaryResponse(m1),
                            memberMapper.toSummaryResponse(m2),
                            bp.compatibilityScore(),
                            comp.getBestCollaborationType()
                    );
                })
                .limit(3)
                .collect(Collectors.toList());

        // Generate recommendations
        List<String> strengths = generateTeamStrengths(members, compatibilityResult);
        List<String> weaknesses = generateTeamWeaknesses(members, compatibilityResult, conflicts);
        List<String> recommendations = generateTeamRecommendations(members, compatibilityResult, isBalanced, conflicts);

        // Generate summary
        String dynamicsSummary = generateTeamDynamicsSummary(members, compatibilityResult);

        return TeamBuildResult.builder()
                .teamName(request.getTeamName())
                .members(members.stream().map(memberMapper::toSummaryResponse).collect(Collectors.toList()))
                .teamSize(members.size())
                .overallCompatibilityScore(compatibilityResult.averageCompatibilityScore())
                .compatibilityLevel(compatibilityResult.overallLevel())
                .elementBalance(elementBalance)
                .isElementBalanced(isBalanced)
                .missingElements(missingElements)
                .potentialConflicts(conflicts)
                .conflictCount(conflicts.size())
                .hasHighConflicts(!conflicts.isEmpty())
                .topCompatiblePairs(bestPairs)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .recommendations(recommendations)
                .teamDynamicsSummary(dynamicsSummary)
                .keyInsights(compatibilityResult.insights())
                .build();
    }

    /**
     * Optimize existing team
     */
    @Transactional(readOnly = true)
    public List<TeamOptimizationSuggestion> optimizeTeam(OptimizeTeamRequest request) {
        log.info("Optimizing team with {} members", request.getCurrentTeamMemberIds().size());

        List<TeamOptimizationSuggestion> suggestions = new ArrayList<>();

        // Current team analysis
        CompatibilityService.TeamCompatibilityResult currentResult =
                compatibilityService.calculateTeamCompatibility(request.getCurrentTeamMemberIds());

        BigDecimal currentScore = currentResult.averageCompatibilityScore();

        // Suggestion 1: Add members to balance elements
        if (request.isPrioritizeElementBalance()) {
            suggestions.addAll(suggestMembersForElementBalance(
                    request.getCurrentTeamMemberIds(),
                    request.getAvailableMemberIds(),
                    currentScore
            ));
        }

        // Suggestion 2: Remove low-compatibility members
        if (request.isMinimizeConflicts() && !currentResult.potentialConflicts().isEmpty()) {
            suggestions.addAll(suggestMemberRemovals(
                    request.getCurrentTeamMemberIds(),
                    currentResult,
                    currentScore
            ));
        }

        // Suggestion 3: Swap members
        if (request.getAvailableMemberIds() != null && !request.getAvailableMemberIds().isEmpty()) {
            suggestions.addAll(suggestMemberSwaps(
                    request.getCurrentTeamMemberIds(),
                    request.getAvailableMemberIds(),
                    currentResult,
                    currentScore
            ));
        }

        return suggestions.stream()
                .sorted((s1, s2) -> s2.getImprovement().compareTo(s1.getImprovement()))
                .limit(request.getMaxSuggestions())
                .collect(Collectors.toList());
    }

    /**
     * Detect conflicts in organization
     */
    @Transactional(readOnly = true)
    public List<ConflictAlert> detectConflicts() {
        log.info("Detecting conflicts across organization");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        List<ConflictAlert> alerts = new ArrayList<>();

        for (int i = 0; i < activeMembers.size(); i++) {
            for (int j = i + 1; j < activeMembers.size(); j++) {
                Member m1 = activeMembers.get(i);
                Member m2 = activeMembers.get(j);

                ZodiacCompatibility comp = compatibilityService.getCompatibilityByMembers(m1.getId(), m2.getId());

                if (comp.getOverallScore().compareTo(BigDecimal.valueOf(40)) < 0) {
                    alerts.add(createConflictAlert(m1, m2, comp));
                }
            }
        }

        return alerts.stream()
                .sorted((a1, a2) -> {
                    int severityCompare = compareSeverity(a1.getSeverity(), a2.getSeverity());
                    if (severityCompare != 0) return severityCompare;
                    return a1.getCompatibilityScore().compareTo(a2.getCompatibilityScore());
                })
                .collect(Collectors.toList());
    }

    /**
     * Find optimal team composition
     */
    @Transactional(readOnly = true)
    public TeamBuildResult findOptimalTeam(int targetSize, List<Long> availableMemberIds) {
        log.info("Finding optimal team of size {} from {} available members",
                targetSize, availableMemberIds.size());

        if (availableMemberIds.size() < targetSize) {
            throw new IllegalArgumentException("Not enough members available");
        }

        List<Member> availableMembers = memberRepository.findAllById(availableMemberIds);

        // Greedy algorithm: Start with best compatible pair, then add members that maximize score
        List<Long> selectedIds = new ArrayList<>();

        // Find best initial pair
        BigDecimal bestPairScore = BigDecimal.ZERO;
        Long bestM1 = null, bestM2 = null;

        for (int i = 0; i < availableMembers.size(); i++) {
            for (int j = i + 1; j < availableMembers.size(); j++) {
                ZodiacCompatibility comp = compatibilityService.getCompatibilityByMembers(
                        availableMembers.get(i).getId(),
                        availableMembers.get(j).getId()
                );

                if (comp.getOverallScore().compareTo(bestPairScore) > 0) {
                    bestPairScore = comp.getOverallScore();
                    bestM1 = availableMembers.get(i).getId();
                    bestM2 = availableMembers.get(j).getId();
                }
            }
        }

        selectedIds.add(bestM1);
        selectedIds.add(bestM2);

        // Add remaining members
        while (selectedIds.size() < targetSize) {
            Long bestNext = findBestNextMember(selectedIds, availableMemberIds);
            if (bestNext == null) break;
            selectedIds.add(bestNext);
        }

        BuildTeamRequest request = BuildTeamRequest.builder()
                .memberIds(selectedIds)
                .teamName("Optimized Team")
                .build();

        return buildTeam(request);
    }

    // Helper methods
    private String determineRiskLevel(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(30)) < 0) return "CRITICAL";
        if (score.compareTo(BigDecimal.valueOf(40)) < 0) return "HIGH";
        if (score.compareTo(BigDecimal.valueOf(50)) < 0) return "MEDIUM";
        return "LOW";
    }

    private String generateConflictSuggestion(Member m1, Member m2, BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(30)) < 0) {
            return String.format("Avoid pairing %s and %s on same projects. Consider separate work streams.",
                    m1.getFullName(), m2.getFullName());
        }
        return String.format("Monitor interactions between %s and %s. Provide clear communication channels.",
                m1.getFullName(), m2.getFullName());
    }

    private List<String> generateTeamStrengths(List<Member> members,
                                               CompatibilityService.TeamCompatibilityResult result) {
        List<String> strengths = new ArrayList<>();

        if (result.averageCompatibilityScore().compareTo(BigDecimal.valueOf(75)) >= 0) {
            strengths.add("Strong natural synergy and compatibility");
        }

        if (result.elementBalance().size() >= 3) {
            strengths.add("Diverse perspectives with multiple elements represented");
        }

        if (result.potentialConflicts().isEmpty()) {
            strengths.add("No significant conflicts detected - smooth collaboration expected");
        }

        return strengths;
    }

    private List<String> generateTeamWeaknesses(List<Member> members,
                                                CompatibilityService.TeamCompatibilityResult result,
                                                List<TeamBuildResult.ConflictPair> conflicts) {
        List<String> weaknesses = new ArrayList<>();

        if (!conflicts.isEmpty()) {
            weaknesses.add(String.format("%d potential conflict pair(s) require management", conflicts.size()));
        }

        if (result.elementBalance().size() < 3) {
            weaknesses.add("Limited element diversity may result in one-sided approaches");
        }

        return weaknesses;
    }

    private List<String> generateTeamRecommendations(List<Member> members,
                                                     CompatibilityService.TeamCompatibilityResult result,
                                                     boolean isBalanced,
                                                     List<TeamBuildResult.ConflictPair> conflicts) {
        List<String> recommendations = new ArrayList<>();

        if (!isBalanced) {
            recommendations.add("Consider adding members from missing elements for better balance");
        }

        if (!conflicts.isEmpty()) {
            recommendations.add("Implement conflict management strategies for identified pairs");
        }

        if (result.averageCompatibilityScore().compareTo(BigDecimal.valueOf(60)) < 0) {
            recommendations.add("Team may benefit from team-building activities to improve cohesion");
        }

        return recommendations;
    }

    private String generateTeamDynamicsSummary(List<Member> members,
                                               CompatibilityService.TeamCompatibilityResult result) {
        return String.format("Team of %d members with %s compatibility (%.1f%%). %s element representation.",
                members.size(),
                result.overallLevel(),
                result.averageCompatibilityScore(),
                result.elementBalance().size() == 4 ? "Perfect" : "Partial"
        );
    }

    private List<TeamOptimizationSuggestion> suggestMembersForElementBalance(
            List<Long> currentIds, List<Long> availableIds, BigDecimal currentScore) {
        // Implementation for element balance suggestions
        return new ArrayList<>();
    }

    private List<TeamOptimizationSuggestion> suggestMemberRemovals(
            List<Long> currentIds,
            CompatibilityService.TeamCompatibilityResult currentResult,
            BigDecimal currentScore) {
        // Implementation for removal suggestions
        return new ArrayList<>();
    }

    private List<TeamOptimizationSuggestion> suggestMemberSwaps(
            List<Long> currentIds, List<Long> availableIds,
            CompatibilityService.TeamCompatibilityResult currentResult,
            BigDecimal currentScore) {
        // Implementation for swap suggestions
        return new ArrayList<>();
    }

    private ConflictAlert createConflictAlert(Member m1, Member m2, ZodiacCompatibility comp) {
        String severity = comp.getOverallScore().compareTo(BigDecimal.valueOf(30)) < 0 ? "CRITICAL" : "HIGH";

        return ConflictAlert.builder()
                .severity(severity)
                .member1(memberMapper.toSummaryResponse(m1))
                .member2(memberMapper.toSummaryResponse(m2))
                .compatibilityScore(comp.getOverallScore())
                .conflictPotential(comp.getConflictPotential())
                .primaryIssue(comp.getChallengesTogether())
                .conflictAreas(Arrays.asList("Work style differences", "Communication challenges"))
                .recommendation(comp.getManagementTips())
                .preventionStrategies(Arrays.asList(
                        "Regular check-ins",
                        "Clear communication channels",
                        "Defined roles and responsibilities"
                ))
                .isResolved(false)
                .build();
    }

    private int compareSeverity(String s1, String s2) {
        Map<String, Integer> severityOrder = Map.of(
                "CRITICAL", 1,
                "HIGH", 2,
                "MEDIUM", 3,
                "LOW", 4
        );
        return severityOrder.get(s1).compareTo(severityOrder.get(s2));
    }

    private Long findBestNextMember(List<Long> selectedIds, List<Long> availableIds) {
        Long bestNext = null;
        BigDecimal bestAvgScore = BigDecimal.ZERO;

        for (Long candidateId : availableIds) {
            if (selectedIds.contains(candidateId)) continue;

            List<Long> testTeam = new ArrayList<>(selectedIds);
            testTeam.add(candidateId);

            try {
                CompatibilityService.TeamCompatibilityResult result =
                        compatibilityService.calculateTeamCompatibility(testTeam);

                if (result.averageCompatibilityScore().compareTo(bestAvgScore) > 0) {
                    bestAvgScore = result.averageCompatibilityScore();
                    bestNext = candidateId;
                }
            } catch (Exception e) {
                log.warn("Error calculating compatibility for candidate {}", candidateId);
            }
        }

        return bestNext;
    }
}