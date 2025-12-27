package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HiringTrendsResponse {
    private Integer months;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MonthlyHiring> monthlyData;
    private Integer totalHires;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyHiring {
        private String month; // Format: yyyy-MM
        private Integer totalHires;
        private Map<String, Long> zodiacBreakdown;
    }
}