package com.jci.zodiac.service;

import com.jci.zodiac.entity.ZodiacCompatibility;
import com.jci.zodiac.repository.ZodiacCompatibilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CompatibilitySeedService - Seed zodiac compatibility matrix
 * Creates 144 compatibility pairs (12 x 12)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after ZodiacSeedService
public class CompatibilitySeedService implements CommandLineRunner {

    private final ZodiacCompatibilityRepository compatibilityRepository;

    @Override
    public void run(String... args) {
        if (compatibilityRepository.count() == 0) {
            log.info("🌟 Seeding Zodiac Compatibility Matrix...");
            seedCompatibilityMatrix();
            log.info("✅ Compatibility Matrix seeding completed!");
        } else {
            log.info("✓ Compatibility Matrix already seeded");
        }
    }

    @Transactional
    public void seedCompatibilityMatrix() {
        List<ZodiacCompatibility> compatibilities = new ArrayList<>();

        ZodiacCompatibility.ZodiacSign[] signs = ZodiacCompatibility.ZodiacSign.values();

        // Only create one direction for each pair to avoid duplicates
        for (int i = 0; i < signs.length; i++) {
            for (int j = i; j < signs.length; j++) {
                ZodiacCompatibility.ZodiacSign sign1 = signs[i];
                ZodiacCompatibility.ZodiacSign sign2 = signs[j];
                compatibilities.add(createCompatibility(sign1, sign2));
            }
        }

        compatibilityRepository.saveAll(compatibilities);
        log.info("Saved {} compatibility pairs", compatibilities.size());
    }

    private ZodiacCompatibility createCompatibility(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {

        // Calculate scores based on element compatibility
        BigDecimal overallScore = calculateOverallScore(sign1, sign2);
        BigDecimal workScore = calculateWorkScore(sign1, sign2);
        BigDecimal commScore = calculateCommunicationScore(sign1, sign2);
        BigDecimal conflictScore = calculateConflictPotential(sign1, sign2);
        BigDecimal synergyScore = calculateSynergyScore(sign1, sign2);

        ZodiacCompatibility.CompatibilityLevel level = determineLevel(overallScore);
        ZodiacCompatibility.ElementHarmony harmony = determineElementHarmony(sign1, sign2);

        return ZodiacCompatibility.builder()
                .zodiacSign1(sign1)
                .zodiacSign2(sign2)
                .overallScore(overallScore)
                .workCompatibilityScore(workScore)
                .communicationScore(commScore)
                .conflictPotential(conflictScore)
                .synergyScore(synergyScore)
                .compatibilityLevel(level)
                .elementHarmony(harmony)
                .strengthsTogether(generateStrengths(sign1, sign2, level))
                .challengesTogether(generateChallenges(sign1, sign2, level))
                .managementTips(generateManagementTips(sign1, sign2, level))
                .bestCollaborationType(generateBestCollaboration(sign1, sign2))
                .build();
    }

    // Compatibility calculation logic
    private BigDecimal calculateOverallScore(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {

        // Same sign = High compatibility
        if (sign1 == sign2) {
            return BigDecimal.valueOf(85);
        }

        String element1 = getElement(sign1);
        String element2 = getElement(sign2);

        // Fire + Air = Excellent (90-95)
        if ((element1.equals("Fire") && element2.equals("Air")) ||
                (element1.equals("Air") && element2.equals("Fire"))) {
            return BigDecimal.valueOf(92);
        }

        // Earth + Water = Excellent (88-93)
        if ((element1.equals("Earth") && element2.equals("Water")) ||
                (element1.equals("Water") && element2.equals("Earth"))) {
            return BigDecimal.valueOf(90);
        }

        // Same element (not same sign) = Good (75-80)
        if (element1.equals(element2)) {
            return BigDecimal.valueOf(78);
        }

        // Fire + Water = Challenging (40-50)
        if ((element1.equals("Fire") && element2.equals("Water")) ||
                (element1.equals("Water") && element2.equals("Fire"))) {
            return BigDecimal.valueOf(45);
        }

        // Earth + Air = Moderate (50-60)
        if ((element1.equals("Earth") && element2.equals("Air")) ||
                (element1.equals("Air") && element2.equals("Earth"))) {
            return BigDecimal.valueOf(55);
        }

        // Default moderate
        return BigDecimal.valueOf(60);
    }

    private BigDecimal calculateWorkScore(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {
        return calculateOverallScore(sign1, sign2).add(BigDecimal.valueOf(5));
    }

    private BigDecimal calculateCommunicationScore(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {
        String element1 = getElement(sign1);
        String element2 = getElement(sign2);

        // Air signs communicate well with everyone
        if (element1.equals("Air") || element2.equals("Air")) {
            return BigDecimal.valueOf(85);
        }

        return calculateOverallScore(sign1, sign2);
    }

    private BigDecimal calculateConflictPotential(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {
        BigDecimal overall = calculateOverallScore(sign1, sign2);
        // Inverse relationship: high compatibility = low conflict
        return BigDecimal.valueOf(100).subtract(overall);
    }

    private BigDecimal calculateSynergyScore(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {
        return calculateOverallScore(sign1, sign2).subtract(BigDecimal.valueOf(5));
    }

    private ZodiacCompatibility.CompatibilityLevel determineLevel(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return ZodiacCompatibility.CompatibilityLevel.Excellent;
        if (score.compareTo(BigDecimal.valueOf(65)) >= 0) return ZodiacCompatibility.CompatibilityLevel.Good;
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return ZodiacCompatibility.CompatibilityLevel.Moderate;
        if (score.compareTo(BigDecimal.valueOf(35)) >= 0) return ZodiacCompatibility.CompatibilityLevel.Challenging;
        return ZodiacCompatibility.CompatibilityLevel.Difficult;
    }

    private ZodiacCompatibility.ElementHarmony determineElementHarmony(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {

        String element1 = getElement(sign1);
        String element2 = getElement(sign2);

        if (element1.equals(element2)) return ZodiacCompatibility.ElementHarmony.Harmonious;

        if ((element1.equals("Fire") && element2.equals("Air")) ||
                (element1.equals("Air") && element2.equals("Fire")) ||
                (element1.equals("Earth") && element2.equals("Water")) ||
                (element1.equals("Water") && element2.equals("Earth"))) {
            return ZodiacCompatibility.ElementHarmony.Harmonious;
        }

        if ((element1.equals("Fire") && element2.equals("Water")) ||
                (element1.equals("Water") && element2.equals("Fire")) ||
                (element1.equals("Earth") && element2.equals("Air")) ||
                (element1.equals("Air") && element2.equals("Earth"))) {
            return ZodiacCompatibility.ElementHarmony.Challenging;
        }

        return ZodiacCompatibility.ElementHarmony.Neutral;
    }

    private String getElement(ZodiacCompatibility.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Leo, Sagittarius -> "Fire";
            case Taurus, Virgo, Capricorn -> "Earth";
            case Gemini, Libra, Aquarius -> "Air";
            case Cancer, Scorpio, Pisces -> "Water";
        };
    }

    private String generateStrengths(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2,
            ZodiacCompatibility.CompatibilityLevel level) {

        if (level == ZodiacCompatibility.CompatibilityLevel.Excellent) {
            return String.format("%s and %s work exceptionally well together. " +
                    "They share similar energy and complement each other's strengths. " +
                    "Great communication and mutual understanding.", sign1, sign2);
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Good) {
            return String.format("%s and %s have good synergy. " +
                    "They can collaborate effectively with some adjustments. " +
                    "Respect for differences enhances teamwork.", sign1, sign2);
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Moderate) {
            return String.format("%s and %s can work together with effort. " +
                    "Finding common ground and clear communication is key.", sign1, sign2);
        } else {
            return String.format("%s and %s require careful management. " +
                    "Focus on complementary skills and clear role definitions.", sign1, sign2);
        }
    }

    private String generateChallenges(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2,
            ZodiacCompatibility.CompatibilityLevel level) {

        if (level == ZodiacCompatibility.CompatibilityLevel.Excellent) {
            return "May become too similar in approach. Need to ensure diverse perspectives.";
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Good) {
            return "Minor differences in work style. Occasional miscommunication possible.";
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Moderate) {
            return "Different approaches to work. May clash on methods or priorities.";
        } else {
            return String.format("Significant differences between %s and %s. " +
                    "Potential for conflict if not managed properly. " +
                    "Different values and work styles may cause friction.", sign1, sign2);
        }
    }

    private String generateManagementTips(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2,
            ZodiacCompatibility.CompatibilityLevel level) {

        if (level == ZodiacCompatibility.CompatibilityLevel.Excellent) {
            return "Leverage their natural synergy. Assign collaborative projects. " +
                    "Encourage them to mentor others together.";
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Good) {
            return "Provide clear communication channels. Acknowledge both their strengths. " +
                    "Use their differences as complementary assets.";
        } else if (level == ZodiacCompatibility.CompatibilityLevel.Moderate) {
            return "Set clear expectations and boundaries. Facilitate regular check-ins. " +
                    "Focus on shared goals rather than methods.";
        } else {
            return String.format("Carefully manage %s and %s interactions. " +
                    "Assign them to different aspects of projects. " +
                    "Have a mediator available. Emphasize respect and professionalism. " +
                    "Focus on their complementary skills.", sign1, sign2);
        }
    }

    private String generateBestCollaboration(
            ZodiacCompatibility.ZodiacSign sign1,
            ZodiacCompatibility.ZodiacSign sign2) {

        String element1 = getElement(sign1);
        String element2 = getElement(sign2);

        if (element1.equals("Fire") && element2.equals("Air")) {
            return "Creative projects, brainstorming sessions, innovation";
        } else if (element1.equals("Earth") && element2.equals("Water")) {
            return "Strategic planning, detailed execution, long-term projects";
        } else if (element1.equals(element2)) {
            return "Projects requiring similar energy and approach";
        } else {
            return "Tasks requiring diverse perspectives and complementary skills";
        }
    }
}