package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Member Entity - JCI Danang Junior Club Members
 * Core entity for managing 50-100 members
 */
@Entity
@Table(name = "members", indexes = {
        @Index(name = "idx_member_code", columnList = "member_code"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_zodiac_sign", columnList = "zodiac_sign"),
        @Index(name = "idx_zodiac_element", columnList = "zodiac_element"),
        @Index(name = "idx_status", columnList = "membership_status"),
        @Index(name = "idx_join_date", columnList = "join_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_code", unique = true, nullable = false, length = 20)
    private String memberCode;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_sign", nullable = false, length = 20)
    private ZodiacSign zodiacSign;

    @Enumerated(EnumType.STRING)
    @Column(name = "zodiac_element", nullable = false, length = 10)
    private ZodiacElement zodiacElement;

    // ==================== JCI Specific Fields ====================

    @Column(length = 100)
    private String position;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_status", length = 20)
    @Builder.Default
    private MembershipStatus membershipStatus = MembershipStatus.Active;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_type", length = 20)
    @Builder.Default
    private MembershipType membershipType = MembershipType.FullMember;

    // ==================== Contact & Personal ====================

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 50)
    @Builder.Default
    private String city = "Da Nang";

    @Column(name = "emergency_contact", length = 100)
    private String emergencyContact;

    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;

// ==================== Social Media & Organization ====================

    @Column(name = "facebook_url", length = 200)
    private String facebookUrl;

    @Column(length = 100)
    private String company;

// ==================== Metadata ====================

    @Column(columnDefinition = "TEXT")
    private String notes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private List<String> tags;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @Builder.Default
    private Long createdBy = 1L; // Always created by VP (user id = 1)

    // ==================== Enums ====================

    public enum ZodiacSign {
        Aries, Taurus, Gemini, Cancer, Leo, Virgo,
        Libra, Scorpio, Sagittarius, Capricorn, Aquarius, Pisces
    }

    public enum ZodiacElement {
        Fire, Earth, Air, Water
    }

    public enum MembershipStatus {
        Active, Inactive, OnLeave, Alumni
    }

    public enum MembershipType {
        FullMember, Associate, Honorary
    }

    // ==================== Helper Methods ====================

    /**
     * Check if member is active
     */
    public boolean isActive() {
        return this.membershipStatus == MembershipStatus.Active;
    }

    /**
     * Check if member shares same zodiac element with another member
     */
    public boolean hasSameElementAs(Member other) {
        return this.zodiacElement == other.getZodiacElement();
    }

    /**
     * Check if member is same zodiac sign with another member
     */
    public boolean hasSameSignAs(Member other) {
        return this.zodiacSign == other.getZodiacSign();
    }

    /**
     * Get member's age
     */
    public int getAge() {
        return LocalDate.now().getYear() - this.dateOfBirth.getYear();
    }

    /**
     * Get days since joined
     */
    public long getDaysSinceJoined() {
        return java.time.temporal.ChronoUnit.DAYS.between(this.joinDate, LocalDate.now());
    }

    @PrePersist
    protected void onCreate() {
        if (this.membershipStatus == null) {
            this.membershipStatus = MembershipStatus.Active;
        }
        if (this.membershipType == null) {
            this.membershipType = MembershipType.FullMember;
        }
        if (this.city == null) {
            this.city = "Da Nang";
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberCode='" + memberCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", zodiacSign=" + zodiacSign +
                ", zodiacElement=" + zodiacElement +
                ", position='" + position + '\'' +
                ", membershipStatus=" + membershipStatus +
                '}';
    }
}