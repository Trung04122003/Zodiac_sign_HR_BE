package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for setting response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingResponse {

    private Long id;
    private String key;
    private String value;
    private String category;
    private String description;
    private String dataType;
    private Boolean isPublic;
    private Boolean isEditable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}