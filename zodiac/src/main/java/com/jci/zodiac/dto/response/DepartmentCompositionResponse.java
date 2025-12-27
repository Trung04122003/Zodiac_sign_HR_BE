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
public class DepartmentCompositionResponse {
    private List<DepartmentZodiacBreakdown> departments;
    private Integer totalDepartments;
    private LocalDate generatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DepartmentZodiacBreakdown {
        private Long departmentId;
        private String departmentName;
        private String departmentCode;
        private Integer totalMembers;
        private Map<Member.ZodiacSign, Long> zodiacDistribution;
        private Map<Member.ZodiacElement, Long> elementBalance;
        private Boolean isBalanced;
        private List<String> missingElements;
        private String dominantSign;
        private String teamVibe;
    }
}