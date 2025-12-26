package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Department Entity - JCI Committees/Teams
 */
@Entity
@Table(name = "departments", indexes = {
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_is_active", columnList = "is_active"),
        @Index(name = "idx_zodiac_theme", columnList = "zodiac_theme")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Zodiac Theme
    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_theme", length = 20)
    private ZodiacSign zodiacTheme;

    @Column(name = "color_primary", length = 7)
    private String colorPrimary;

    @Column(name = "color_secondary", length = 7)
    private String colorSecondary;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    // Department Lead
    @Column(name = "lead_member_id")
    private Long leadMemberId;

    // Statistics
    @Column(name = "member_count")
    @Builder.Default
    private Integer memberCount = 0;

    @Column(name = "active_projects_count")
    @Builder.Default
    private Integer activeProjectsCount = 0;

    // Status
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ZodiacSign {
        Aries, Taurus, Gemini, Cancer, Leo, Virgo,
        Libra, Scorpio, Sagittarius, Capricorn, Aquarius, Pisces
    }

    // Helper methods
    public void incrementMemberCount() {
        this.memberCount = (this.memberCount == null ? 0 : this.memberCount) + 1;
    }

    public void decrementMemberCount() {
        this.memberCount = Math.max(0, (this.memberCount == null ? 0 : this.memberCount) - 1);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", memberCount=" + memberCount +
                ", isActive=" + isActive +
                '}';
    }
}