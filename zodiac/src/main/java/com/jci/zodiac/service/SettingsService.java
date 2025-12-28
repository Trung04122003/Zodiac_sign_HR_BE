package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.BulkUpdateSettingsRequest;
import com.jci.zodiac.dto.request.CreateSettingRequest;
import com.jci.zodiac.dto.request.UpdateSettingRequest;
import com.jci.zodiac.dto.response.SettingResponse;
import com.jci.zodiac.dto.response.SettingsByCategoryResponse;
import com.jci.zodiac.entity.Setting;
import com.jci.zodiac.exception.DuplicateResourceException;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SettingsService - Business logic for system settings management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SettingsService {

    private final SettingRepository settingRepository;

    /**
     * Create a new setting
     */
    public SettingResponse createSetting(CreateSettingRequest request) {
        log.info("Creating setting: {}", request.getKey());

        // Validate uniqueness
        if (settingRepository.existsByKey(request.getKey())) {
            throw new DuplicateResourceException("Setting", "key", request.getKey());
        }

        Setting setting = Setting.builder()
                .key(request.getKey())
                .value(request.getValue())
                .category(request.getCategory() != null ? request.getCategory() : Setting.Category.GENERAL)
                .description(request.getDescription())
                .dataType(request.getDataType() != null ? request.getDataType() : Setting.DataType.STRING)
                .isPublic(request.getIsPublic())
                .isEditable(request.getIsEditable())
                .build();

        Setting saved = settingRepository.save(setting);
        log.info("Setting created: {}", saved.getKey());

        return toResponse(saved);
    }

    /**
     * Get setting by key
     */
    @Transactional(readOnly = true)
    public SettingResponse getSettingByKey(String key) {
        log.debug("Fetching setting with key: {}", key);

        Setting setting = settingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", key));

        return toResponse(setting);
    }

    /**
     * Get setting value by key (direct value)
     */
    @Transactional(readOnly = true)
    public String getSettingValue(String key) {
        return settingRepository.findValueByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", key));
    }

    /**
     * Get setting value with default fallback
     */
    @Transactional(readOnly = true)
    public String getSettingValue(String key, String defaultValue) {
        return settingRepository.findValueByKey(key).orElse(defaultValue);
    }

    /**
     * Get all settings
     */
    @Transactional(readOnly = true)
    public List<SettingResponse> getAllSettings() {
        log.debug("Fetching all settings");

        return settingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get public settings only
     */
    @Transactional(readOnly = true)
    public List<SettingResponse> getPublicSettings() {
        log.debug("Fetching public settings");

        return settingRepository.findAllPublicSettings().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get settings by category
     */
    @Transactional(readOnly = true)
    public List<SettingResponse> getSettingsByCategory(Setting.Category category) {
        log.debug("Fetching settings for category: {}", category);

        return settingRepository.findByCategory(category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all settings grouped by category
     */
    @Transactional(readOnly = true)
    public SettingsByCategoryResponse getAllSettingsByCategory() {
        log.debug("Fetching all settings grouped by category");

        List<Setting> allSettings = settingRepository.findAll();

        Map<String, List<SettingResponse>> grouped = allSettings.stream()
                .collect(Collectors.groupingBy(
                        setting -> setting.getCategory().name(),
                        Collectors.mapping(this::toResponse, Collectors.toList())
                ));

        return SettingsByCategoryResponse.builder()
                .settingsByCategory(grouped)
                .totalCategories(grouped.size())
                .totalSettings(allSettings.size())
                .build();
    }

    /**
     * Update setting by key
     */
    public SettingResponse updateSetting(String key, UpdateSettingRequest request) {
        log.info("Updating setting: {}", key);

        Setting setting = settingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", key));

        // Check if editable
        if (!setting.canEdit()) {
            throw new IllegalArgumentException("Setting '" + key + "' is not editable");
        }

        // Update fields
        if (request.getValue() != null) {
            setting.setValue(request.getValue());
        }
        if (request.getCategory() != null) {
            setting.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            setting.setDescription(request.getDescription());
        }
        if (request.getDataType() != null) {
            setting.setDataType(request.getDataType());
        }
        if (request.getIsPublic() != null) {
            setting.setIsPublic(request.getIsPublic());
        }
        if (request.getIsEditable() != null) {
            setting.setIsEditable(request.getIsEditable());
        }

        Setting updated = settingRepository.save(setting);
        log.info("Setting updated: {}", updated.getKey());

        return toResponse(updated);
    }

    /**
     * Bulk update settings
     */
    public Map<String, String> bulkUpdateSettings(BulkUpdateSettingsRequest request) {
        log.info("Bulk updating {} settings", request.getSettings().size());

        Map<String, String> results = new HashMap<>();

        for (Map.Entry<String, String> entry : request.getSettings().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            try {
                Optional<Setting> settingOpt = settingRepository.findByKey(key);

                if (settingOpt.isPresent()) {
                    Setting setting = settingOpt.get();

                    if (!setting.canEdit()) {
                        results.put(key, "SKIPPED - Not editable");
                        continue;
                    }

                    setting.setValue(value);
                    settingRepository.save(setting);
                    results.put(key, "UPDATED");
                } else {
                    results.put(key, "SKIPPED - Not found");
                }
            } catch (Exception e) {
                log.error("Error updating setting {}: {}", key, e.getMessage());
                results.put(key, "ERROR - " + e.getMessage());
            }
        }

        log.info("Bulk update completed. Results: {}", results);
        return results;
    }

    /**
     * Delete setting by key
     */
    public void deleteSetting(String key) {
        log.info("Deleting setting: {}", key);

        Setting setting = settingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", key));

        if (!setting.canEdit()) {
            throw new IllegalArgumentException("Setting '" + key + "' cannot be deleted");
        }

        settingRepository.delete(setting);
        log.info("Setting deleted: {}", key);
    }

    /**
     * Reset settings to default values
     */
    public void resetToDefaults() {
        log.warn("Resetting all editable settings to defaults");

        List<Setting> editableSettings = settingRepository.findAllEditableSettings();

        // This would need a default values map or re-run migration
        // For now, just log warning
        log.warn("Reset functionality requires default values configuration");
        throw new UnsupportedOperationException("Reset to defaults not yet implemented. Please re-run database migration.");
    }

    /**
     * Search settings by keyword
     */
    @Transactional(readOnly = true)
    public List<SettingResponse> searchSettings(String keyword) {
        log.debug("Searching settings with keyword: {}", keyword);

        return settingRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ==================== Helper Methods ====================

    private SettingResponse toResponse(Setting setting) {
        return SettingResponse.builder()
                .id(setting.getId())
                .key(setting.getKey())
                .value(setting.getValue())
                .category(setting.getCategory().name())
                .description(setting.getDescription())
                .dataType(setting.getDataType().name())
                .isPublic(setting.getIsPublic())
                .isEditable(setting.getIsEditable())
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .updatedBy(setting.getUpdatedBy())
                .build();
    }
}