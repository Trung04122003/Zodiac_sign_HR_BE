package com.jci.zodiac.service;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacCompatibility;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.repository.MemberRepository;
import com.jci.zodiac.repository.ZodiacCompatibilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CompatibilityService - Calculate and analyze zodiac compatibility
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompatibilityService {

    private final ZodiacCompatibilityRepository compatibilityRepository;
    private final MemberRepository memberRepository;

    /**
     * Get compatibility between two zodiac signs
     */
    @Transactional(readOnly = true)
    public ZodiacCompatibility getCompatibilityBySign(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {

        log.debug("Fetching compatibility for {} and {}", sign1, sign2);

        return compatibilityRepository.findBySignPair(sign1, sign2)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compatibility not found for pair: " + sign1 + " - " + sign2
                ));
    }

    /**
     * Get compatibility between two members
     */
    @Transactional(readOnly = true)
    public ZodiacCompatibility getCompatibilityByMembers(Long memberId1, Long memberId2) {
        log.info("Calculating compatibility for members: {} and {}", memberId1, memberId2);

        Member member1 = memberRepository.findById(memberId1)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId1));

        Member member2 = memberRepository.findById(memberId2)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId2));

        ZodiacCompatibility.ZodiacSign sign1 = convertToCompatibilitySign(member1.getZodiacSign());
        ZodiacCompatibility.ZodiacSign sign2 = convertToCompatibilitySign(member2.getZodiacSign());

        return getCompatibilityBySign(sign1, sign2);
    }

    /**
     * Get all compatibility pairs for a specific sign
     */
    @Transactional(readOnly = true)
    public List<ZodiacCompatibility> getAllCompatibilitiesForSign(ZodiacCompatibility.ZodiacSign sign) {
        log.debug("Fetching all compatibilities for sign: {}", sign);
        return compatibilityRepository.findAllForSign(sign);
    }

    /**
     * Find best matches for a sign
     */
    @Transactional(readOnly = true)
    public List<ZodiacCompatibility> getBestMatchesForSign(ZodiacCompatibility.ZodiacSign sign) {
        log.debug("Finding best matches for sign: {}", sign);
        return compatibilityRepository.findBestMatchesForSign(sign).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Find best compatible pairs in entire database
     */
    @Transactional(readOnly = true)
    public List<ZodiacCompatibility> getBestCompatiblePairs(int limit) {
        log.debug("Finding top {} best compatible pairs", limit);
        return compatibilityRepository
                .findHighCompatibilityPairs(BigDecimal.valueOf(80))
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Find challenging pairs (low compatibility)
     */
    @Transactional(readOnly = true)
    public List<ZodiacCompatibility> getChallengingPairs() {
        log.debug("Finding challenging pairs");
        return compatibilityRepository.findLowCompatibilityPairs(BigDecimal.valueOf(50));
    }

    /**
     * Calculate team compatibility
     */
    @Transactional(readOnly = true)
    public TeamCompatibilityResult calculateTeamCompatibility(List<Long> memberIds) {
        log.info("Calculating team compatibility for {} members", memberIds.size());

        if (memberIds.size() < 2) {
            throw new IllegalArgumentException("Team must have at least 2 members");
        }

        List<Member> members = memberRepository.findAllById(memberIds);

        if (members.size() != memberIds.size()) {
            throw new ResourceNotFoundException("Some members not found");
        }

        // Calculate all pair compatibilities
        List<PairCompatibility> pairCompatibilities = new ArrayList<>();
        BigDecimal totalScore = BigDecimal.ZERO;
        int pairCount = 0;

        for (int i = 0; i < members.size(); i++) {
            for (int j = i + 1; j < members.size(); j++) {
                Member m1 = members.get(i);
                Member m2 = members.get(j);

                ZodiacCompatibility compatibility = getCompatibilityByMembers(m1.getId(), m2.getId());

                pairCompatibilities.add(new PairCompatibility(
                        m1.getId(), m1.getFullName(), m1.getZodiacSign().name(),
                        m2.getId(), m2.getFullName(), m2.getZodiacSign().name(),
                        compatibility.getOverallScore(),
                        compatibility.getCompatibilityLevel().name()
                ));

                totalScore = totalScore.add(compatibility.getOverallScore());
                pairCount++;
            }
        }

        // Calculate average score
        BigDecimal averageScore = totalScore.divide(
                BigDecimal.valueOf(pairCount),
                2,
                RoundingMode.HALF_UP
        );

        // Calculate element balance
        Map<Member.ZodiacElement, Long> elementBalance = members.stream()
                .collect(Collectors.groupingBy(
                        Member::getZodiacElement,
                        Collectors.counting()
                ));

        // Check for conflicts
        List<PairCompatibility> conflicts = pairCompatibilities.stream()
                .filter(pc -> pc.compatibilityScore().compareTo(BigDecimal.valueOf(40)) < 0)
                .collect(Collectors.toList());

        // Find best pairs
        List<PairCompatibility> bestPairs = pairCompatibilities.stream()
                .sorted((p1, p2) -> p2.compatibilityScore().compareTo(p1.compatibilityScore()))
                .limit(3)
                .collect(Collectors.toList());

        // Determine overall level
        String overallLevel = determineTeamLevel(averageScore);

        return new TeamCompatibilityResult(
                memberIds.size(),
                averageScore,
                overallLevel,
                elementBalance,
                pairCompatibilities,
                conflicts,
                bestPairs,
                generateTeamInsights(members, averageScore, elementBalance, conflicts)
        );
    }

    /**
     * Find best compatible pairs among actual members
     */
    @Transactional(readOnly = true)
    public List<MemberPairCompatibility> findBestMemberPairs(int limit) {
        log.info("Finding best member pairs (limit: {})", limit);

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);
        List<MemberPairCompatibility> pairs = new ArrayList<>();

        for (int i = 0; i < activeMembers.size(); i++) {
            for (int j = i + 1; j < activeMembers.size(); j++) {
                Member m1 = activeMembers.get(i);
                Member m2 = activeMembers.get(j);

                ZodiacCompatibility compatibility = getCompatibilityByMembers(m1.getId(), m2.getId());

                pairs.add(new MemberPairCompatibility(
                        m1.getId(), m1.getFullName(), m1.getMemberCode(), m1.getZodiacSign().name(),
                        m2.getId(), m2.getFullName(), m2.getMemberCode(), m2.getZodiacSign().name(),
                        compatibility.getOverallScore(),
                        compatibility.getCompatibilityLevel().name(),
                        compatibility.getBestCollaborationType()
                ));
            }
        }

        return pairs.stream()
                .sorted((p1, p2) -> p2.compatibilityScore().compareTo(p1.compatibilityScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Helper methods
    private ZodiacCompatibility.ZodiacSign convertToCompatibilitySign(Member.ZodiacSign memberSign) {
        return ZodiacCompatibility.ZodiacSign.valueOf(memberSign.name());
    }

    private String determineTeamLevel(BigDecimal averageScore) {
        if (averageScore.compareTo(BigDecimal.valueOf(80)) >= 0) return "Excellent";
        if (averageScore.compareTo(BigDecimal.valueOf(65)) >= 0) return "Good";
        if (averageScore.compareTo(BigDecimal.valueOf(50)) >= 0) return "Moderate";
        if (averageScore.compareTo(BigDecimal.valueOf(35)) >= 0) return "Challenging";
        return "Difficult";
    }

    private List<String> generateTeamInsights(
            List<Member> members,
            BigDecimal averageScore,
            Map<Member.ZodiacElement, Long> elementBalance,
            List<PairCompatibility> conflicts) {

        List<String> insights = new ArrayList<>();

        // Overall compatibility insight
        if (averageScore.compareTo(BigDecimal.valueOf(75)) >= 0) {
            insights.add("✅ Excellent team compatibility! This team has strong natural synergy.");
        } else if (averageScore.compareTo(BigDecimal.valueOf(60)) >= 0) {
            insights.add("👍 Good team compatibility. Minor adjustments may enhance collaboration.");
        } else {
            insights.add("⚠️ Team requires careful management. Focus on leveraging complementary strengths.");
        }

        // Element balance insight
        long missingElements = 4 - elementBalance.size();
        if (missingElements == 0) {
            insights.add("🌟 Perfect element balance! All 4 elements represented.");
        } else if (missingElements == 1) {
            insights.add("⚖️ Good element diversity. Consider adding one more element for perfect balance.");
        } else {
            insights.add("📊 Limited element diversity. Team may benefit from more varied perspectives.");
        }

        // Conflict insight
        if (conflicts.isEmpty()) {
            insights.add("✨ No significant conflicts detected. Team should work smoothly.");
        } else {
            insights.add(String.format("⚠️ %d potential conflict pair(s) detected. Monitor these relationships closely.",
                    conflicts.size()));
        }

        return insights;
    }

    // DTOs
    public record TeamCompatibilityResult(
            int teamSize,
            BigDecimal averageCompatibilityScore,
            String overallLevel,
            Map<Member.ZodiacElement, Long> elementBalance,
            List<PairCompatibility> allPairCompatibilities,
            List<PairCompatibility> potentialConflicts,
            List<PairCompatibility> bestPairs,
            List<String> insights
    ) {}

    public record PairCompatibility(
            Long member1Id, String member1Name, String member1Sign,
            Long member2Id, String member2Name, String member2Sign,
            BigDecimal compatibilityScore,
            String level
    ) {}

    public record MemberPairCompatibility(
            Long member1Id, String member1Name, String member1Code, String member1Sign,
            Long member2Id, String member2Name, String member2Code, String member2Sign,
            BigDecimal compatibilityScore,
            String level,
            String bestFor
    ) {}
}