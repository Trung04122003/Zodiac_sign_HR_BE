package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for team optimization suggestions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamOptimizationSuggestion {

    private String suggestionType; // ADD, REMOVE, SWAP
    private String description;
    private BigDecimal currentScore;
    private BigDecimal projectedScore;
    private BigDecimal improvement;

    // For ADD suggestions
    private MemberSummaryResponse memberToAdd;

    // For REMOVE suggestions
    private MemberSummaryResponse memberToRemove;

    private String reasoning;
    private List<String> benefits;
}