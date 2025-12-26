package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSummaryResponse {

    private Long id;
    private String name;
    private String teamType;
    private String status;
    private Integer memberCount;
    private Integer targetMemberCount;
    private BigDecimal compatibilityScore;
    private LocalDate startDate;
    private LocalDate endDate;
    private String departmentName;
}