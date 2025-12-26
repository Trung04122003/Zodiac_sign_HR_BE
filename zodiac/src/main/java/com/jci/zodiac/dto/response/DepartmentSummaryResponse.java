package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for department list view
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSummaryResponse {

    private Long id;
    private String name;
    private String code;
    private String zodiacTheme;
    private String zodiacSymbol;
    private Integer memberCount;
    private String leadMemberName;
    private Boolean isActive;
}