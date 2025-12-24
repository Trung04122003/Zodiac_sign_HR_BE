package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for member search and filter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSearchRequest {

    private String keyword; // Search in name, email, position

    private Member.ZodiacSign zodiacSign;

    private Member.ZodiacElement zodiacElement;

    private Member.MembershipStatus membershipStatus;

    private Long departmentId;

    private String position;

    // Pagination
    private Integer page = 0;

    private Integer size = 20;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC"; // ASC or DESC
}