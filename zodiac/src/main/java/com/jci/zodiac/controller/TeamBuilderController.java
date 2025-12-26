package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.BuildTeamRequest;
import com.jci.zodiac.dto.request.OptimizeTeamRequest;
import com.jci.zodiac.dto.response.ConflictAlert;
import com.jci.zodiac.dto.response.TeamBuildResult;
import com.jci.zodiac.dto.response.TeamOptimizationSuggestion;
import com.jci.zodiac.service.TeamBuilderService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TeamBuilderController - REST APIs for team building and optimization
 * Base URL: /api/team-builder
 */
@RestController
@RequestMapping("/team-builder")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Team Builder", description = "APIs for building and optimizing teams based on zodiac compatibility")
public class TeamBuilderController {

    private final TeamBuilderService teamBuilderService;

    /**
     * Build and analyze a team
     * POST /api/team-builder/build
     */
    @PostMapping("/build")
    @Operation(summary = "Build team", description = "Build and analyze team compatibility")
    public ResponseEntity<ApiResponse<TeamBuildResult>> buildTeam(
            @Valid @RequestBody BuildTeamRequest request) {

        log.info("REST request to build team with {} members", request.getMemberIds().size());

        TeamBuildResult result = teamBuilderService.buildTeam(request);

        String message = String.format("Team built successfully! Overall compatibility: %.1f%% (%s)",
                result.getOverallCompatibilityScore(),
                result.getCompatibilityLevel()
        );

        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Optimize existing team
     * POST /api/team-builder/optimize
     */
    @PostMapping("/optimize")
    @Operation(summary = "Optimize team", description = "Get suggestions to improve team compatibility")
    public ResponseEntity<ApiResponse<List<TeamOptimizationSuggestion>>> optimizeTeam(
            @Valid @RequestBody OptimizeTeamRequest request) {

        log.info("REST request to optimize team with {} members", request.getCurrentTeamMemberIds().size());

        List<TeamOptimizationSuggestion> suggestions = teamBuilderService.optimizeTeam(request);

        String message = String.format("Found %d optimization suggestion(s)", suggestions.size());

        return ResponseEntity.ok(ApiResponse.success(message, suggestions));
    }

    /**
     * Detect conflicts in organization
     * GET /api/team-builder/conflicts
     */
    @GetMapping("/conflicts")
    @Operation(summary = "Detect conflicts", description = "Identify potential conflicts across organization")
    public ResponseEntity<ApiResponse<List<ConflictAlert>>> detectConflicts() {

        log.info("REST request to detect conflicts");

        List<ConflictAlert> alerts = teamBuilderService.detectConflicts();

        long criticalCount = alerts.stream().filter(a -> a.getSeverity().equals("CRITICAL")).count();
        long highCount = alerts.stream().filter(a -> a.getSeverity().equals("HIGH")).count();

        String message = String.format("Found %d potential conflicts (%d critical, %d high)",
                alerts.size(), criticalCount, highCount);

        return ResponseEntity.ok(ApiResponse.success(message, alerts));
    }

    /**
     * Find optimal team
     * POST /api/team-builder/optimal?targetSize={size}
     */
    @PostMapping("/optimal")
    @Operation(summary = "Find optimal team", description = "Find best team composition from available members")
    public ResponseEntity<ApiResponse<TeamBuildResult>> findOptimalTeam(
            @Parameter(description = "Target team size") @RequestParam int targetSize,
            @RequestBody List<Long> availableMemberIds) {

        log.info("REST request to find optimal team of size {} from {} members",
                targetSize, availableMemberIds.size());

        TeamBuildResult result = teamBuilderService.findOptimalTeam(targetSize, availableMemberIds);

        String message = String.format("Optimal team found! Compatibility: %.1f%%",
                result.getOverallCompatibilityScore()
        );

        return ResponseEntity.ok(ApiResponse.success(message, result));
    }

    /**
     * Quick team check
     * GET /api/team-builder/quick-check?members={id1},{id2},{id3}
     */
    @GetMapping("/quick-check")
    @Operation(summary = "Quick team check", description = "Quick compatibility check for a list of members")
    public ResponseEntity<ApiResponse<QuickTeamCheck>> quickTeamCheck(
            @Parameter(description = "Comma-separated member IDs") @RequestParam List<Long> members) {

        log.info("REST request for quick check of {} members", members.size());

        BuildTeamRequest request = BuildTeamRequest.builder()
                .memberIds(members)
                .teamName("Quick Check")
                .build();

        TeamBuildResult result = teamBuilderService.buildTeam(request);

        QuickTeamCheck quickCheck = new QuickTeamCheck(
                result.getTeamSize(),
                result.getOverallCompatibilityScore(),
                result.getCompatibilityLevel(),
                result.getConflictCount(),
                result.isElementBalanced(),
                result.getKeyInsights()
        );

        return ResponseEntity.ok(ApiResponse.success(quickCheck));
    }

    /**
     * DTO for quick check response
     */
    public record QuickTeamCheck(
            int teamSize,
            java.math.BigDecimal compatibilityScore,
            String level,
            int conflictCount,
            boolean isBalanced,
            List<String> insights
    ) {}
}