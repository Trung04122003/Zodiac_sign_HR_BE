package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {

    private Long id;
    private Long memberId;
    private String memberName;
    private String memberCode;
    private String zodiacSign;
    private String zodiacSymbol;
    private String role;
    private LocalDate joinedDate;
    private LocalDate leftDate;
    private Boolean isActive;
    private String notes;
}