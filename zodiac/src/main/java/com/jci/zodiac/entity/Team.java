package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Team Entity - Project teams and working groups
 */
@Entity
@Table(name = "teams", indexes = {
        @Index(name = "idx_department", columnList = "department_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_team_type", columnList = "team_type"),
        @Index(name = "idx_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "department_id")
    private Long departmentId;

    // Team Type
    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", length = 20)
    @Builder.Default
    private TeamType teamType = TeamType.Project;

    // Timeline
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Status status = Status.Planning;

    // Team Composition
    @Column(name = "member_count")
    @Builder.Default
    private Integer memberCount = 0;

    @Column(name = "target_member_count")
    private Integer targetMemberCount;

    // Zodiac Analytics
    @Column(name = "compatibility_score", precision = 5, scale = 2)
    private BigDecimal compatibilityScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "element_balance", columnDefinition = "JSON")
    private Map<String, Integer> elementBalance;

    @Column(name = "has_zodiac_conflicts")
    @Builder.Default
    private Boolean hasZodiacConflicts = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @Builder.Default
    private Long createdBy = 1L;

    // Enums
    public enum TeamType {
        Project, Committee, WorkingGroup, TaskForce
    }

    public enum Status {
        Planning, Active, OnHold, Completed, Cancelled
    }

    // Helper methods
    public void incrementMemberCount() {
        this.memberCount = (this.memberCount == null ? 0 : this.memberCount) + 1;
    }

    public void decrementMemberCount() {
        this.memberCount = Math.max(0, (this.memberCount == null ? 0 : this.memberCount) - 1);
    }

    public boolean isActive() {
        return this.status == Status.Active;
    }

    public boolean isCompleted() {
        return this.status == Status.Completed;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamType=" + teamType +
                ", status=" + status +
                ", memberCount=" + memberCount +
                '}';
    }
}