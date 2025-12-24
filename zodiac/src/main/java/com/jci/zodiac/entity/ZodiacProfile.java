package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ZodiacProfile Entity - Master Data for 12 Zodiac Signs
 * Contains detailed personality traits, strengths, weaknesses for each sign
 */
@Entity
@Table(name = "zodiac_profiles", indexes = {
        @Index(name = "idx_element", columnList = "element"),
        @Index(name = "idx_modality", columnList = "modality")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZodiacProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_sign", unique = true, nullable = false, length = 20)
    private ZodiacSign zodiacSign;

    // ==================== Basic Info ====================

    @Column(length = 10)
    private String symbol; // e.g., "♐" for Sagittarius

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Element element;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Modality modality;

    @Column(name = "ruling_planet", length = 20)
    private String rulingPlanet;

    // ==================== Date Range ====================

    @Column(name = "date_start", length = 10)
    private String dateStart; // Format: "MM-DD" e.g., "11-22"

    @Column(name = "date_end", length = 10)
    private String dateEnd; // Format: "MM-DD" e.g., "12-21"

    // ==================== Colors & Design ====================

    @Column(name = "color_primary", length = 7)
    private String colorPrimary; // HEX color

    @Column(name = "color_secondary", length = 7)
    private String colorSecondary;

    @Column(name = "color_gradient_start", length = 7)
    private String colorGradientStart;

    @Column(name = "color_gradient_end", length = 7)
    private String colorGradientEnd;

    // ==================== Personality & Traits (JSON) ====================

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "personality_traits", nullable = false, columnDefinition = "JSON")
    private List<String> personalityTraits;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "JSON")
    private List<String> strengths;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "JSON")
    private List<String> weaknesses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "work_style", nullable = false, columnDefinition = "JSON")
    private List<String> workStyle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "best_roles", columnDefinition = "JSON")
    private List<String> bestRoles;

    @Column(name = "communication_style", columnDefinition = "TEXT")
    private String communicationStyle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "motivation_factors", columnDefinition = "JSON")
    private List<String> motivationFactors;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stress_triggers", columnDefinition = "JSON")
    private List<String> stressTriggers;

    // ==================== Leadership & Teamwork ====================

    @Column(name = "leadership_style", columnDefinition = "TEXT")
    private String leadershipStyle;

    @Column(name = "team_contribution", columnDefinition = "TEXT")
    private String teamContribution;

    @Column(name = "conflict_resolution_style", columnDefinition = "TEXT")
    private String conflictResolutionStyle;

    // ==================== Additional Info ====================

    @Column(name = "description_long", columnDefinition = "TEXT")
    private String descriptionLong;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "famous_people", columnDefinition = "JSON")
    private List<String> famousPeople;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_attributes", columnDefinition = "JSON")
    private Map<String, Object> customAttributes;

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

    public enum Element {
        Fire, Earth, Air, Water
    }

    public enum Modality {
        Cardinal, Fixed, Mutable
    }

    // ==================== Helper Methods ====================

    /**
     * Check if this sign is compatible element with another
     */
    public boolean isElementCompatibleWith(Element otherElement) {
        // Fire ↔ Air: Harmonious
        // Earth ↔ Water: Harmonious
        // Same element: Harmonious
        if (this.element == otherElement) return true;
        if ((this.element == Element.Fire && otherElement == Element.Air) ||
                (this.element == Element.Air && otherElement == Element.Fire)) return true;
        if ((this.element == Element.Earth && otherElement == Element.Water) ||
                (this.element == Element.Water && otherElement == Element.Earth)) return true;
        return false;
    }

    /**
     * Check if this is a Fire sign
     */
    public boolean isFireSign() {
        return this.element == Element.Fire;
    }

    /**
     * Check if this is a Water sign
     */
    public boolean isWaterSign() {
        return this.element == Element.Water;
    }

    /**
     * Check if this is an Earth sign
     */
    public boolean isEarthSign() {
        return this.element == Element.Earth;
    }

    /**
     * Check if this is an Air sign
     */
    public boolean isAirSign() {
        return this.element == Element.Air;
    }

    /**
     * Get zodiac sign name
     */
    public String getSignName() {
        return this.zodiacSign.name();
    }

    @Override
    public String toString() {
        return "ZodiacProfile{" +
                "zodiacSign=" + zodiacSign +
                ", symbol='" + symbol + '\'' +
                ", element=" + element +
                ", modality=" + modality +
                ", rulingPlanet='" + rulingPlanet + '\'' +
                '}';
    }
}