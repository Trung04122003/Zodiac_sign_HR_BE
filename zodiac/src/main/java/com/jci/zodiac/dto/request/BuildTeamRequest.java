package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for team builder request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildTeamRequest {

    @NotEmpty(message = "Member IDs list cannot be empty")
    private List<Long> memberIds;

    private String teamName;

    private String teamDescription;

    @Min(value = 2, message = "Team must have at least 2 members")
    @Max(value = 20, message = "Team cannot exceed 20 members")
    private Integer targetSize;

    @Builder.Default
    private boolean requireElementBalance = false;

    @Builder.Default
    private boolean avoidConflicts = true;

    @Builder.Default
    private Double minCompatibilityScore = 60.0;
}