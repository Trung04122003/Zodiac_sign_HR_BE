package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTeamRequest {

    private String name;
    private String description;
    private Long departmentId;
    private Team.TeamType teamType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Team.Status status;
    private Integer targetMemberCount;
}