package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTeamMemberRequest {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @Builder.Default
    private String role = "Member";

    @Builder.Default
    private LocalDate joinedDate = LocalDate.now();

    private String notes;
}