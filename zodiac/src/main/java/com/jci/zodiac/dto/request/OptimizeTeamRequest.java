package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for team optimization request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizeTeamRequest {

    @NotEmpty(message = "Current team member IDs cannot be empty")
    private List<Long> currentTeamMemberIds;

    private List<Long> availableMemberIds; // Pool to suggest from

    @Builder.Default
    private int maxSuggestions = 5;

    @Builder.Default
    private boolean prioritizeElementBalance = true;

    @Builder.Default
    private boolean minimizeConflicts = true;
}