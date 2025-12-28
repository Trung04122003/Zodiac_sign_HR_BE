package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for settings grouped by category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsByCategoryResponse {

    private Map<String, List<SettingResponse>> settingsByCategory;
    private Integer totalCategories;
    private Integer totalSettings;
}