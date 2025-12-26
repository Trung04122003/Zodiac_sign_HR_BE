package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TeamMember Entity - Junction table for Team-Member relationship
 */
@Entity
@Table(name = "team_members",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_team_member", columnNames = {"team_id", "member_id"})
        },
        indexes = {
                @Index(name = "idx_team", columnList = "team_id"),
                @Index(name = "idx_member", columnList = "member_id"),
                @Index(name = "idx_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    // Role in Team
    @Column(length = 50)
    @Builder.Default
    private String role = "Member";

    // Timeline
    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    @Column(name = "left_date")
    private LocalDate leftDate;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // Notes
    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "TeamMember{" +
                "teamId=" + teamId +
                ", memberId=" + memberId +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}