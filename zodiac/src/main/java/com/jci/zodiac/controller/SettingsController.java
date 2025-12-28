package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.BulkUpdateSettingsRequest;
import com.jci.zodiac.dto.request.CreateSettingRequest;
import com.jci.zodiac.dto.request.UpdateSettingRequest;
import com.jci.zodiac.dto.response.SettingResponse;
import com.jci.zodiac.dto.response.SettingsByCategoryResponse;
import com.jci.zodiac.entity.Setting;
import com.jci.zodiac.service.DataExportService;
import com.jci.zodiac.service.DataImportService;
import com.jci.zodiac.service.SettingsService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * SettingsController - REST APIs for system settings management
 * Base URL: /api/settings
 */
@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Settings Management", description = "APIs for managing system settings, export/import data")
public class SettingsController {

    private final SettingsService settingsService;
    private final DataExportService dataExportService;
    private final DataImportService dataImportService;

    // ==================== Basic CRUD Operations ====================

    /**
     * Create a new setting
     * POST /api/settings
     */
    @PostMapping
    @Operation(summary = "Create setting", description = "Create a new system setting")
    public ResponseEntity<ApiResponse<SettingResponse>> createSetting(
            @Valid @RequestBody CreateSettingRequest request) {

        log.info("REST request to create setting: {}", request.getKey());

        SettingResponse response = settingsService.createSetting(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Setting created successfully", response));
    }

    /**
     * Get setting by key
     * GET /api/settings/{key}
     */
    @GetMapping("/{key}")
    @Operation(summary = "Get setting by key", description = "Retrieve setting by its key")
    public ResponseEntity<ApiResponse<SettingResponse>> getSettingByKey(
            @Parameter(description = "Setting key") @PathVariable String key) {

        log.info("REST request to get setting: {}", key);

        SettingResponse response = settingsService.getSettingByKey(key);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get setting value by key (direct value only)
     * GET /api/settings/{key}/value
     */
    @GetMapping("/{key}/value")
    @Operation(summary = "Get setting value", description = "Retrieve only the value of a setting")
    public ResponseEntity<ApiResponse<String>> getSettingValue(
            @Parameter(description = "Setting key") @PathVariable String key) {

        log.info("REST request to get setting value: {}", key);

        String value = settingsService.getSettingValue(key);

        return ResponseEntity.ok(ApiResponse.success("Setting value retrieved", value));
    }

    /**
     * Get all settings
     * GET /api/settings
     */
    @GetMapping
    @Operation(summary = "Get all settings", description = "Retrieve all system settings")
    public ResponseEntity<ApiResponse<List<SettingResponse>>> getAllSettings() {
        log.info("REST request to get all settings");

        List<SettingResponse> response = settingsService.getAllSettings();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get public settings only
     * GET /api/settings/public
     */
    @GetMapping("/public")
    @Operation(summary = "Get public settings", description = "Retrieve only publicly visible settings")
    public ResponseEntity<ApiResponse<List<SettingResponse>>> getPublicSettings() {
        log.info("REST request to get public settings");

        List<SettingResponse> response = settingsService.getPublicSettings();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get settings by category
     * GET /api/settings/category/{category}
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get settings by category", description = "Retrieve settings filtered by category")
    public ResponseEntity<ApiResponse<List<SettingResponse>>> getSettingsByCategory(
            @Parameter(description = "Setting category") @PathVariable Setting.Category category) {

        log.info("REST request to get settings by category: {}", category);

        List<SettingResponse> response = settingsService.getSettingsByCategory(category);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all settings grouped by category
     * GET /api/settings/grouped
     */
    @GetMapping("/grouped")
    @Operation(summary = "Get settings grouped by category", description = "Retrieve all settings organized by category")
    public ResponseEntity<ApiResponse<SettingsByCategoryResponse>> getAllSettingsByCategory() {
        log.info("REST request to get settings grouped by category");

        SettingsByCategoryResponse response = settingsService.getAllSettingsByCategory();

        return ResponseEntity.ok(ApiResponse.success("Settings grouped successfully", response));
    }

    /**
     * Update setting by key
     * PUT /api/settings/{key}
     */
    @PutMapping("/{key}")
    @Operation(summary = "Update setting", description = "Update an existing setting")
    public ResponseEntity<ApiResponse<SettingResponse>> updateSetting(
            @Parameter(description = "Setting key") @PathVariable String key,
            @Valid @RequestBody UpdateSettingRequest request) {

        log.info("REST request to update setting: {}", key);

        SettingResponse response = settingsService.updateSetting(key, request);

        return ResponseEntity.ok(ApiResponse.success("Setting updated successfully", response));
    }

    /**
     * Bulk update settings
     * PUT /api/settings/bulk
     */
    @PutMapping("/bulk")
    @Operation(summary = "Bulk update settings", description = "Update multiple settings at once")
    public ResponseEntity<ApiResponse<Map<String, String>>> bulkUpdateSettings(
            @Valid @RequestBody BulkUpdateSettingsRequest request) {

        log.info("REST request to bulk update {} settings", request.getSettings().size());

        Map<String, String> results = settingsService.bulkUpdateSettings(request);

        return ResponseEntity.ok(ApiResponse.success("Bulk update completed", results));
    }

    /**
     * Delete setting by key
     * DELETE /api/settings/{key}
     */
    @DeleteMapping("/{key}")
    @Operation(summary = "Delete setting", description = "Delete a setting (only if editable)")
    public ResponseEntity<ApiResponse<Void>> deleteSetting(
            @Parameter(description = "Setting key") @PathVariable String key) {

        log.info("REST request to delete setting: {}", key);

        settingsService.deleteSetting(key);

        return ResponseEntity.ok(ApiResponse.success("Setting deleted successfully"));
    }

    /**
     * Search settings
     * GET /api/settings/search?keyword={keyword}
     */
    @GetMapping("/search")
    @Operation(summary = "Search settings", description = "Search settings by keyword in key or description")
    public ResponseEntity<ApiResponse<List<SettingResponse>>> searchSettings(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {

        log.info("REST request to search settings: {}", keyword);

        List<SettingResponse> response = settingsService.searchSettings(keyword);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Reset settings to defaults
     * POST /api/settings/reset
     */
    @PostMapping("/reset")
    @Operation(summary = "Reset to defaults", description = "Reset all editable settings to default values")
    public ResponseEntity<ApiResponse<Void>> resetToDefaults() {
        log.warn("REST request to reset settings to defaults");

        settingsService.resetToDefaults();

        return ResponseEntity.ok(ApiResponse.success("Settings reset to defaults"));
    }

    // ==================== Data Export/Import Operations ====================

    /**
     * Export all data to JSON
     * GET /api/settings/export/json
     */
    @GetMapping("/export/json")
    @Operation(summary = "Export data to JSON", description = "Export all system data as JSON file")
    public ResponseEntity<byte[]> exportDataToJson() {
        log.info("REST request to export data to JSON");

        byte[] jsonData = dataExportService.exportAllDataToJson();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"zodiac_hr_backup_" + System.currentTimeMillis() + ".json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonData);
    }

    /**
     * Export all data to Excel
     * GET /api/settings/export/excel
     */
    @GetMapping("/export/excel")
    @Operation(summary = "Export data to Excel", description = "Export all system data as Excel file")
    public ResponseEntity<byte[]> exportDataToExcel() {
        log.info("REST request to export data to Excel");

        byte[] excelData = dataExportService.exportAllDataToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"zodiac_hr_backup_" + System.currentTimeMillis() + ".xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
    }

    /**
     * Export settings only to JSON
     * GET /api/settings/export/settings-only
     */
    @GetMapping("/export/settings-only")
    @Operation(summary = "Export settings to JSON", description = "Export only settings data as JSON")
    public ResponseEntity<byte[]> exportSettingsOnly() {
        log.info("REST request to export settings only");

        byte[] jsonData = dataExportService.exportSettingsToJson();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"settings_backup_" + System.currentTimeMillis() + ".json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonData);
    }

    /**
     * Import data from JSON
     * POST /api/settings/import/json
     */
    @PostMapping(value = "/import/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import data from JSON", description = "Import system data from JSON file")
    public ResponseEntity<ApiResponse<Map<String, Object>>> importDataFromJson(
            @Parameter(description = "JSON backup file") @RequestParam("file") MultipartFile file) {

        log.info("REST request to import data from JSON: {}", file.getOriginalFilename());

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("File is empty"));
        }

        if (!file.getOriginalFilename().endsWith(".json")) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Only JSON files are supported"));
        }

        Map<String, Object> result = dataImportService.importDataFromJson(file);

        return ResponseEntity.ok(ApiResponse.success("Data imported successfully", result));
    }

    /**
     * Import settings only from JSON
     * POST /api/settings/import/settings-only
     */
    @PostMapping(value = "/import/settings-only", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import settings from JSON", description = "Import only settings from JSON file")
    public ResponseEntity<ApiResponse<Map<String, Object>>> importSettingsOnly(
            @Parameter(description = "Settings JSON file") @RequestParam("file") MultipartFile file) {

        log.info("REST request to import settings from JSON: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("File is empty"));
        }

        Map<String, Object> result = dataImportService.importSettingsFromJson(file);

        return ResponseEntity.ok(ApiResponse.success("Settings imported successfully", result));
    }

    /**
     * Get export history
     * GET /api/settings/export/history
     */
    @GetMapping("/export/history")
    @Operation(summary = "Get export history", description = "Retrieve history of data exports")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getExportHistory() {
        log.info("REST request to get export history");

        List<Map<String, Object>> history = dataExportService.getExportHistory();

        return ResponseEntity.ok(ApiResponse.success("Export history retrieved", history));
    }

    // ==================== System Information ====================

    /**
     * Get system information
     * GET /api/settings/system-info
     */
    @GetMapping("/system-info")
    @Operation(summary = "Get system info", description = "Retrieve system information and statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemInfo() {
        log.info("REST request to get system information");

        Map<String, Object> systemInfo = Map.of(
                "organization", Map.of(
                        "name", settingsService.getSettingValue("organization.name", "JCI Danang Junior Club"),
                        "code", settingsService.getSettingValue("organization.code", "JCI-DN"),
                        "motto", settingsService.getSettingValue("organization.motto", "Aim High, Lead with Optimism!")
                ),
                "zodiacTheme", Map.of(
                        "primarySign", settingsService.getSettingValue("zodiac.theme.primary_sign", "Sagittarius"),
                        "symbol", settingsService.getSettingValue("zodiac.theme.symbol", "♐"),
                        "colorPrimary", settingsService.getSettingValue("zodiac.theme.color_primary", "#9B59B6"),
                        "colorSecondary", settingsService.getSettingValue("zodiac.theme.color_secondary", "#3498DB")
                ),
                "system", Map.of(
                        "dateFormat", settingsService.getSettingValue("system.date_format", "yyyy-MM-dd"),
                        "language", settingsService.getSettingValue("system.language", "en"),
                        "timezone", settingsService.getSettingValue("system.timezone", "Asia/Ho_Chi_Minh")
                ),
                "features", Map.of(
                        "birthdayTracker", Boolean.parseBoolean(settingsService.getSettingValue("feature.birthday_tracker", "true")),
                        "dailyInsights", Boolean.parseBoolean(settingsService.getSettingValue("feature.daily_insights", "true")),
                        "teamBuilder", Boolean.parseBoolean(settingsService.getSettingValue("feature.team_builder", "true")),
                        "exportPdf", Boolean.parseBoolean(settingsService.getSettingValue("feature.export_pdf", "true")),
                        "exportExcel", Boolean.parseBoolean(settingsService.getSettingValue("feature.export_excel", "true"))
                )
        );

        return ResponseEntity.ok(ApiResponse.success("System information retrieved", systemInfo));
    }
}