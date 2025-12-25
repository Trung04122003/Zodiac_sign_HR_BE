package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for bulk operation results
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkOperationResponse {

    private int totalRequested;
    private int successCount;
    private int failureCount;
    private List<String> successMessages;
    private List<String> errorMessages;
    private Map<String, Object> details; // Additional info

    public boolean isFullySuccessful() {
        return failureCount == 0;
    }

    public boolean isPartiallySuccessful() {
        return successCount > 0 && failureCount > 0;
    }

    public boolean isFullyFailed() {
        return successCount == 0;
    }
}