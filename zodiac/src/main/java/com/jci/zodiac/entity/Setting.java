package com.jci.zodiac.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Setting Entity - System Configuration Key-Value Store
 * Flexible settings management for the application
 */
@Entity
@Table(name = "settings", indexes = {
        @Index(name = "idx_key", columnList = "setting_key"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_public", columnList = "is_public")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key", unique = true, nullable = false, length = 100)
    private String key;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String value;

    // ==================== Metadata ====================

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @Builder.Default
    private Category category = Category.GENERAL;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", length = 20)
    @Builder.Default
    private DataType dataType = DataType.STRING;

    // ==================== Access Control ====================

    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_editable")
    @Builder.Default
    private Boolean isEditable = true;

    // ==================== Timestamps ====================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    @Builder.Default
    private Long updatedBy = 1L;

    // ==================== Enums ====================

    public enum Category {
        GENERAL,
        ORGANIZATION,
        ZODIAC,
        COMPATIBILITY,
        SYSTEM,
        FEATURE,
        NOTIFICATION,
        BACKUP
    }

    public enum DataType {
        STRING,
        NUMBER,
        BOOLEAN,
        JSON
    }

    // ==================== Helper Methods ====================

    /**
     * Get value as String (default behavior)
     */
    public String getStringValue() {
        return this.value;
    }

    /**
     * Get value as Integer
     */
    public Integer getIntValue() {
        if (this.value == null) return null;
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get value as Double
     */
    public Double getDoubleValue() {
        if (this.value == null) return null;
        try {
            return Double.parseDouble(this.value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get value as Boolean
     */
    public Boolean getBooleanValue() {
        if (this.value == null) return null;
        return Boolean.parseBoolean(this.value);
    }

    /**
     * Check if this setting can be edited
     */
    public boolean canEdit() {
        return Boolean.TRUE.equals(this.isEditable);
    }

    /**
     * Check if this setting is public (visible to all users)
     */
    public boolean isPublicSetting() {
        return Boolean.TRUE.equals(this.isPublic);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", category=" + category +
                ", dataType=" + dataType +
                ", isPublic=" + isPublic +
                ", isEditable=" + isEditable +
                '}';
    }
}