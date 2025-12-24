package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ZodiacCompatibility Entity - Compatibility Matrix for 12 x 12 Zodiac Pairs
 * Contains precomputed compatibility scores and analysis
 */
@Entity
@Table(name = "zodiac_compatibility",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_pair", columnNames = {"zodiac_sign_1", "zodiac_sign_2"})
        },
        indexes = {
                @Index(name = "idx_sign1", columnList = "zodiac_sign_1"),
                @Index(name = "idx_sign2", columnList = "zodiac_sign_2"),
                @Index(name = "idx_compatibility", columnList = "compatibility_level")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZodiacCompatibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_sign_1", nullable = false, length = 20)
    private ZodiacSign zodiacSign1;

    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_sign_2", nullable = false, length = 20)
    private ZodiacSign zodiacSign2;

    // ==================== Compatibility Scores ====================

    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore; // 0-100

    @Column(name = "work_compatibility_score", precision = 5, scale = 2)
    private BigDecimal workCompatibilityScore; // How well they work together

    @Column(name = "communication_score", precision = 5, scale = 2)
    private BigDecimal communicationScore; // Communication effectiveness

    @Column(name = "conflict_potential", precision = 5, scale = 2)
    private BigDecimal conflictPotential; // 0 = no conflict, 100 = high conflict

    @Column(name = "synergy_score", precision = 5, scale = 2)
    private BigDecimal synergyScore; // Team synergy potential

    // ==================== Analysis ====================

    @Enumerated(EnumType.STRING)
    @Column(name = "compatibility_level", length = 20)
    private CompatibilityLevel compatibilityLevel;

    @Column(name = "strengths_together", columnDefinition = "TEXT")
    private String strengthsTogether;

    @Column(name = "challenges_together", columnDefinition = "TEXT")
    private String challengesTogether;

    @Column(name = "management_tips", columnDefinition = "TEXT")
    private String managementTips;

    @Column(name = "best_collaboration_type", length = 100)
    private String bestCollaborationType;

    // ==================== Element Interaction ====================

    @Enumerated(EnumType.STRING)
    @Column(name = "element_harmony", length = 20)
    private ElementHarmony elementHarmony;

    // ==================== Metadata ====================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==================== Enums ====================

    public enum ZodiacSign {
        Aries, Taurus, Gemini, Cancer, Leo, Virgo,
        Libra, Scorpio, Sagittarius, Capricorn, Aquarius, Pisces
    }

    public enum CompatibilityLevel {
        Excellent,   // 80-100%
        Good,        // 65-79%
        Moderate,    // 50-64%
        Challenging, // 35-49%
        Difficult    // 0-34%
    }

    public enum ElementHarmony {
        Harmonious,  // Same element or compatible (Fire-Air, Earth-Water)
        Neutral,     // Different but not conflicting
        Challenging  // Conflicting elements (Fire-Water, Earth-Air)
    }

    // ==================== Helper Methods ====================

    /**
     * Check if compatibility is high (>= 65%)
     */
    public boolean isHighCompatibility() {
        return this.overallScore.compareTo(BigDecimal.valueOf(65)) >= 0;
    }

    /**
     * Check if compatibility is low (< 40%)
     */
    public boolean isLowCompatibility() {
        return this.overallScore.compareTo(BigDecimal.valueOf(40)) < 0;
    }

    /**
     * Check if this is an excellent match
     */
    public boolean isExcellentMatch() {
        return this.compatibilityLevel == CompatibilityLevel.Excellent;
    }

    /**
     * Check if there's high conflict potential
     */
    public boolean hasHighConflictPotential() {
        return this.conflictPotential != null &&
                this.conflictPotential.compareTo(BigDecimal.valueOf(60)) >= 0;
    }

    /**
     * Get overall score as percentage string
     */
    public String getOverallScorePercentage() {
        return this.overallScore + "%";
    }

    /**
     * Check if signs are reversed (to avoid duplicate pairs)
     */
    public boolean isReversePair(ZodiacSign sign1, ZodiacSign sign2) {
        return (this.zodiacSign1 == sign2 && this.zodiacSign2 == sign1);
    }

    /**
     * Get compatibility level from score
     */
    public static CompatibilityLevel getCompatibilityLevelFromScore(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return CompatibilityLevel.Excellent;
        if (score.compareTo(BigDecimal.valueOf(65)) >= 0) return CompatibilityLevel.Good;
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return CompatibilityLevel.Moderate;
        if (score.compareTo(BigDecimal.valueOf(35)) >= 0) return CompatibilityLevel.Challenging;
        return CompatibilityLevel.Difficult;
    }

    @PrePersist
    protected void onCreate() {
        // Auto-set compatibility level based on overall score
        if (this.compatibilityLevel == null && this.overallScore != null) {
            this.compatibilityLevel = getCompatibilityLevelFromScore(this.overallScore);
        }
    }

    @Override
    public String toString() {
        return "ZodiacCompatibility{" +
                "zodiacSign1=" + zodiacSign1 +
                ", zodiacSign2=" + zodiacSign2 +
                ", overallScore=" + overallScore +
                ", compatibilityLevel=" + compatibilityLevel +
                '}';
    }
}