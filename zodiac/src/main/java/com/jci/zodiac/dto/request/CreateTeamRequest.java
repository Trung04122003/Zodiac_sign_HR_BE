package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeamRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    private String description;

    private Long departmentId;

    @Builder.Default
    private Team.TeamType teamType = Team.TeamType.Project;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer targetMemberCount;

    private List<Long> initialMemberIds;
}