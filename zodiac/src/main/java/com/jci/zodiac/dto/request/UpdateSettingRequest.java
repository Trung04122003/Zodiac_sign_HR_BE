package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing setting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSettingRequest {

    private String value;

    private Setting.Category category;

    private String description;

    private Setting.DataType dataType;

    private Boolean isPublic;

    private Boolean isEditable;
}