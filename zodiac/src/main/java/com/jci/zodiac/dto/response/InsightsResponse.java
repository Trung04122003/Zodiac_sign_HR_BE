package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for AI-generated insights
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsightsResponse {

    private String insightType; // STRENGTH, WEAKNESS, OPPORTUNITY, THREAT
    private String title;
    private String description;
    private String priority; // HIGH, MEDIUM, LOW
    private List<String> recommendations;
    private List<String> affectedEntities; // Member IDs, Team IDs, etc.
}