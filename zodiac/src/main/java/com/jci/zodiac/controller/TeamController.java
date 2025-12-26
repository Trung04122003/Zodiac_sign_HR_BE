package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.AddTeamMemberRequest;
import com.jci.zodiac.dto.request.CreateTeamRequest;
import com.jci.zodiac.dto.request.UpdateTeamRequest;
import com.jci.zodiac.dto.response.TeamAnalyticsResponse;
import com.jci.zodiac.dto.response.TeamMemberResponse;
import com.jci.zodiac.dto.response.TeamResponse;
import com.jci.zodiac.dto.response.TeamSummaryResponse;
import com.jci.zodiac.entity.Team;
import com.jci.zodiac.service.TeamService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TeamController - REST APIs for team management
 * Base URL: /api/teams
 */
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Team Management", description = "APIs for managing project teams and working groups")
public class TeamController {

    private final TeamService teamService;

    /**
     * Create team
     * POST /api/teams
     */
    @PostMapping
    @Operation(summary = "Create team", description = "Create a new project team or working group")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody CreateTeamRequest request) {

        log.info("REST request to create team: {}", request.getName());

        TeamResponse response = teamService.createTeam(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Team created successfully", response));
    }

    /**
     * Get team by ID
     * GET /api/teams/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID", description = "Retrieve team details including members")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(
            @Parameter(description = "Team ID") @PathVariable Long id) {

        log.info("REST request to get team by id: {}", id);

        TeamResponse response = teamService.getTeamById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all teams
     * GET /api/teams
     */
    @GetMapping
    @Operation(summary = "Get all teams", description = "Retrieve all teams")
    public ResponseEntity<ApiResponse<List<TeamSummaryResponse>>> getAllTeams() {
        log.info("REST request to get all teams");

        List<TeamSummaryResponse> response = teamService.getAllTeams();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get active teams
     * GET /api/teams/active
     */
    @GetMapping("/active")
    @Operation(summary = "Get active teams", description = "Retrieve only active teams")
    public ResponseEntity<ApiResponse<List<TeamSummaryResponse>>> getActiveTeams() {
        log.info("REST request to get active teams");

        List<TeamSummaryResponse> response = teamService.getActiveTeams();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get teams by status
     * GET /api/teams/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get teams by status", description = "Retrieve teams filtered by status")
    public ResponseEntity<ApiResponse<List<TeamSummaryResponse>>> getTeamsByStatus(
            @Parameter(description = "Team status") @PathVariable Team.Status status) {

        log.info("REST request to get teams by status: {}", status);

        List<TeamSummaryResponse> response = teamService.getTeamsByStatus(status);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update team
     * PUT /api/teams/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update team", description = "Update team information")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @Parameter(description = "Team ID") @PathVariable Long id,
            @Valid @RequestBody UpdateTeamRequest request) {

        log.info("REST request to update team: {}", id);

        TeamResponse response = teamService.updateTeam(id, request);

        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", response));
    }

    /**
     * Delete team
     * DELETE /api/teams/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team", description = "Delete a team")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @Parameter(description = "Team ID") @PathVariable Long id) {

        log.info("REST request to delete team: {}", id);

        teamService.deleteTeam(id);

        return ResponseEntity.ok(ApiResponse.success("Team deleted successfully"));
    }

    /**
     * Add member to team
     * POST /api/teams/{teamId}/members
     */
    @PostMapping("/{teamId}/members")
    @Operation(summary = "Add member to team", description = "Add a member to this team")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> addMemberToTeam(
            @Parameter(description = "Team ID") @PathVariable Long teamId,
            @Valid @RequestBody AddTeamMemberRequest request) {

        log.info("REST request to add member {} to team {}", request.getMemberId(), teamId);

        TeamMemberResponse response = teamService.addMemberToTeam(teamId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member added to team successfully", response));
    }

    /**
     * Remove member from team
     * DELETE /api/teams/{teamId}/members/{memberId}
     */
    @DeleteMapping("/{teamId}/members/{memberId}")
    @Operation(summary = "Remove member from team", description = "Remove a member from this team")
    public ResponseEntity<ApiResponse<Void>> removeMemberFromTeam(
            @Parameter(description = "Team ID") @PathVariable Long teamId,
            @Parameter(description = "Member ID") @PathVariable Long memberId) {

        log.info("REST request to remove member {} from team {}", memberId, teamId);

        teamService.removeMemberFromTeam(teamId, memberId);

        return ResponseEntity.ok(ApiResponse.success("Member removed from team successfully"));
    }

    /**
     * Get team members
     * GET /api/teams/{id}/members
     */
    @GetMapping("/{id}/members")
    @Operation(summary = "Get team members", description = "Retrieve all active members in this team")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMembers(
            @Parameter(description = "Team ID") @PathVariable Long id) {

        log.info("REST request to get members for team: {}", id);

        List<TeamMemberResponse> response = teamService.getTeamMembers(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get team analytics
     * GET /api/teams/{id}/analytics
     */
    @GetMapping("/{id}/analytics")
    @Operation(summary = "Get team analytics", description = "Get zodiac compatibility analytics for team")
    public ResponseEntity<ApiResponse<TeamAnalyticsResponse>> getTeamAnalytics(
            @Parameter(description = "Team ID") @PathVariable Long id) {

        log.info("REST request to get analytics for team: {}", id);

        TeamAnalyticsResponse response = teamService.getTeamAnalytics(id);

        String message = String.format("Team compatibility: %.1f%% (%s)",
                response.getOverallCompatibilityScore(),
                response.getCompatibilityLevel()
        );

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}