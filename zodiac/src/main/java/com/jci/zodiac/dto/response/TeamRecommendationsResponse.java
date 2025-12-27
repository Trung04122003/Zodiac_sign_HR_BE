package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRecommendationsResponse {
    private Integer targetSize;
    private List<RecommendedTeam> recommendations;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecommendedTeam {
        private Integer teamNumber;
        private List<String> members;
        private BigDecimal compatibility;
        private Map<Member.ZodiacElement, Long> elementBalance;
        private String reasoning;
    }
}