package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for member list view (simplified)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSummaryResponse {

    private Long id;
    private String memberCode;
    private String fullName;
    private String email;
    private String position;
    private String zodiacSign;
    private String zodiacSymbol;
    private String zodiacElement;
    private String avatarUrl;
    private String membershipStatus;
    private LocalDate joinDate;
    private Boolean isActive;
}