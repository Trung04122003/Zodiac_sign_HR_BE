package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for compatibility heatmap matrix
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilityMatrixResponse {

    private List<String> rowLabels; // Member names or departments
    private List<String> columnLabels;
    private List<List<MatrixCell>> matrix;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MatrixCell {
        private BigDecimal value;
        private String level; // Excellent, Good, Moderate, etc.
        private String color; // HEX color for heatmap
    }
}