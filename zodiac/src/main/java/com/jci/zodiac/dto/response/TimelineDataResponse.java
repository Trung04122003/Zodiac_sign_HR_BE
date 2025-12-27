package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for timeline data (new hires, team formations)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineDataResponse {

    private String timelineName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TimelineEvent> events;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TimelineEvent {
        private LocalDate date;
        private String eventType;
        private String description;
        private String zodiacSign;
        private String zodiacSymbol;
        private Integer count;
    }
}