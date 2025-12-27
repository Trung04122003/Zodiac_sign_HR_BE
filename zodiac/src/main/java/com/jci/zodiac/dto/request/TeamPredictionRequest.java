package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for team prediction request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPredictionRequest {

    @NotEmpty(message = "Member IDs list cannot be empty")
    @Size(min = 2, message = "At least 2 members required for predictions")
    private List<Long> memberIds;

    private String projectType; // Optional: type of project/goal
    private Integer durationMonths; // Optional: project duration
    private Boolean includeDetailedAnalysis;
}