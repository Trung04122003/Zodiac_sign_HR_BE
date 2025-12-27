package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FunStatsResponse {
    private String mostCompatiblePair;
    private String leastCompatiblePair;
    private String mostBalancedTeam;
    private String zodiacOfTheMonth;
    private Long sagittariusCount;
    private Double sagittariusPercentage;
    private List<String> funFacts;
}