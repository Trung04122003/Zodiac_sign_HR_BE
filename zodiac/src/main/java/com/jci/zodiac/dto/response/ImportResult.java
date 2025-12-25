package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for import operation results
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResult {

    private int totalRows;
    private int successfulImports;
    private int failedImports;
    private int skippedRows;

    @Builder.Default
    private List<MemberResponse> importedMembers = new ArrayList<>();

    @Builder.Default
    private List<ImportError> errors = new ArrayList<>();

    public boolean isFullySuccessful() {
        return failedImports == 0 && skippedRows == 0;
    }

    public double getSuccessRate() {
        if (totalRows == 0) return 0.0;
        return (double) successfulImports / totalRows * 100;
    }

    /**
     * Inner class for import errors
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImportError {
        private int rowNumber;
        private String field;
        private String value;
        private String errorMessage;
    }
}