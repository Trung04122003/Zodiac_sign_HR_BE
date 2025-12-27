package com.jci.zodiac.controller;

import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.service.DashboardService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * DashboardController - REST APIs for dashboard and analytics
 * Base URL: /api/dashboard
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard & Analytics", description = "APIs for dashboard statistics and analytics")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get dashboard overview (main stats)
     * GET /api/dashboard/overview
     */
    @GetMapping("/overview")
    @Operation(summary = "Get dashboard overview", description = "Retrieve main dashboard statistics")
    public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getDashboardOverview() {
        log.info("REST request to get dashboard overview");

        DashboardOverviewResponse response = dashboardService.getDashboardOverview();

        return ResponseEntity.ok(ApiResponse.success("Dashboard overview retrieved successfully", response));
    }

    /**
     * Get zodiac distribution for pie chart
     * GET /api/dashboard/zodiac-distribution
     */
    @GetMapping("/zodiac-distribution")
    @Operation(summary = "Get zodiac distribution", description = "Retrieve zodiac sign distribution data for pie chart")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getZodiacDistribution() {
        log.info("REST request to get zodiac distribution");

        ChartDataResponse response = dashboardService.getZodiacDistribution();

        return ResponseEntity.ok(ApiResponse.success("Zodiac distribution retrieved successfully", response));
    }

    /**
     * Get element balance for bar chart
     * GET /api/dashboard/element-balance
     */
    @GetMapping("/element-balance")
    @Operation(summary = "Get element balance", description = "Retrieve zodiac element balance data for bar chart")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getElementBalance() {
        log.info("REST request to get element balance");

        ChartDataResponse response = dashboardService.getElementBalance();

        return ResponseEntity.ok(ApiResponse.success("Element balance retrieved successfully", response));
    }

    /**
     * Get department breakdown statistics
     * GET /api/dashboard/department-breakdown
     */
    @GetMapping("/department-breakdown")
    @Operation(summary = "Get department breakdown", description = "Retrieve department statistics and composition")
    public ResponseEntity<ApiResponse<ChartDataResponse>> getDepartmentBreakdown() {
        log.info("REST request to get department breakdown");

        ChartDataResponse response = dashboardService.getDepartmentBreakdown();

        return ResponseEntity.ok(ApiResponse.success("Department breakdown retrieved successfully", response));
    }

    /**
     * Get new hires timeline with zodiac markers
     * GET /api/dashboard/timeline
     */
    @GetMapping("/timeline")
    @Operation(summary = "Get timeline data", description = "Retrieve new hires timeline with zodiac markers")
    public ResponseEntity<ApiResponse<TimelineDataResponse>> getTimeline(
            @Parameter(description = "Number of months to look back (default: 12)")
            @RequestParam(defaultValue = "12") int months) {

        log.info("REST request to get timeline data for last {} months", months);

        TimelineDataResponse response = dashboardService.getTimelineData(months);

        return ResponseEntity.ok(ApiResponse.success("Timeline data retrieved successfully", response));
    }

    /**
     * Get organization-wide statistics
     * GET /api/dashboard/organization-stats
     */
    @GetMapping("/organization-stats")
    @Operation(summary = "Get organization statistics", description = "Retrieve comprehensive organization statistics")
    public ResponseEntity<ApiResponse<OrganizationStatisticsResponse>> getOrganizationStats() {
        log.info("REST request to get organization statistics");

        OrganizationStatisticsResponse response = dashboardService.getOrganizationStatistics();

        return ResponseEntity.ok(ApiResponse.success("Organization statistics retrieved successfully", response));
    }

    /**
     * Get compatibility matrix for heatmap
     * GET /api/dashboard/compatibility-matrix
     */
    @GetMapping("/compatibility-matrix")
    @Operation(summary = "Get compatibility matrix", description = "Retrieve compatibility matrix for heatmap visualization")
    public ResponseEntity<ApiResponse<CompatibilityMatrixResponse>> getCompatibilityMatrix(
            @Parameter(description = "Department ID to filter (optional)")
            @RequestParam(required = false) Long departmentId) {

        log.info("REST request to get compatibility matrix for department: {}", departmentId);

        CompatibilityMatrixResponse response = dashboardService.getCompatibilityMatrix(departmentId);

        return ResponseEntity.ok(ApiResponse.success("Compatibility matrix retrieved successfully", response));
    }

    /**
     * Refresh dashboard cache
     * POST /api/dashboard/refresh-cache
     */
    @PostMapping("/refresh-cache")
    @Operation(summary = "Refresh dashboard cache", description = "Force refresh all dashboard cached data")
    public ResponseEntity<ApiResponse<Void>> refreshDashboardCache() {
        log.info("REST request to refresh dashboard cache");

        dashboardService.refreshCache();

        return ResponseEntity.ok(ApiResponse.success("Dashboard cache refreshed successfully"));
    }
}