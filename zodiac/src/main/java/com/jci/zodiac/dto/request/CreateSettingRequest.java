package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Setting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new setting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSettingRequest {

    @NotBlank(message = "Setting key is required")
    @Size(min = 2, max = 100, message = "Key must be between 2 and 100 characters")
    private String key;

    private String value;

    private Setting.Category category;

    private String description;

    @Builder.Default
    private Setting.DataType dataType = Setting.DataType.STRING;

    @Builder.Default
    private Boolean isPublic = false;

    @Builder.Default
    private Boolean isEditable = true;
}