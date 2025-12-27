package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for chart visualization data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartDataResponse {

    private String chartType; // PIE, BAR, LINE, HEATMAP
    private String title;
    private List<ChartDataPoint> data;
    private Map<String, Object> metadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ChartDataPoint {
        private String label;
        private Number value;
        private String color;
        private Map<String, Object> additionalData;
    }
}