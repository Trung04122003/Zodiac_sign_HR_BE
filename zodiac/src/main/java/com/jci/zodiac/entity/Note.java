package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Note Entity - Personal notes and observations
 */
@Entity
@Table(name = "notes", indexes = {
        @Index(name = "idx_note_type", columnList = "note_type"),
        @Index(name = "idx_member", columnList = "member_id"),
        @Index(name = "idx_important", columnList = "is_important")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false, length = 20)
    private NoteType noteType;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private List<String> tags;

    @Column(name = "is_important")
    @Builder.Default
    private Boolean isImportant = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @Builder.Default
    private Long createdBy = 1L;

    public enum NoteType {
        Member, Team, Department, General
    }
}