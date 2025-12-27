package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Member;
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
public class ZodiacAnalyticsResponse {
    private Integer totalMembers;
    private Map<Member.ZodiacSign, Long> zodiacDistribution;
    private Map<Member.ZodiacElement, Long> elementDistribution;
    private String mostCommonSign;
    private String leastCommonSign;
    private String preferredElement;
    private Map<String, Map<Member.ZodiacSign, Long>> hiringPatterns;
    private List<String> insights;
    private List<String> recommendations;
    private LocalDate generatedAt;
}