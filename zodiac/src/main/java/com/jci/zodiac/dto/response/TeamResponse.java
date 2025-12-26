package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long id;
    private String name;
    private String description;

    private Long departmentId;
    private String departmentName;

    private String teamType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    private Integer memberCount;
    private Integer targetMemberCount;

    // Zodiac Analytics
    private BigDecimal compatibilityScore;
    private String compatibilityLevel;
    private Map<String, Integer> elementBalance;
    private Boolean hasZodiacConflicts;

    private List<TeamMemberResponse> members;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}