package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElementRecommendationsResponse {
    private Map<Member.ZodiacElement, Long> currentBalance;
    private Boolean isBalanced;
    private List<String> missingElements;
    private List<String> recommendations;
    private List<String> suggestedZodiacSigns;
}