package com.jci.zodiac.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for bulk updating multiple settings at once
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUpdateSettingsRequest {

    @NotEmpty(message = "Settings map cannot be empty")
    private Map<String, String> settings; // key -> value

    private String updateReason; // Optional: reason for bulk update
}