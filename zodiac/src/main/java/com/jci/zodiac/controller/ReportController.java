package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.GenerateReportRequest;
import com.jci.zodiac.dto.request.TeamPredictionRequest;
import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.service.ReportService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * ReportController - REST APIs for reports and analytics
 * Base URL: /api/reports
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports & Analytics", description = "APIs for generating reports and advanced analytics")
public class ReportController {

    private final ReportService reportService;

    /**
     * Get zodiac analytics report
     * GET /api/reports/zodiac-analytics
     */
    @GetMapping("/zodiac-analytics")
    @Operation(summary = "Get zodiac analytics", description = "Retrieve zodiac hiring patterns and analytics")
    public ResponseEntity<ApiResponse<ZodiacAnalyticsResponse>> getZodiacAnalytics() {
        log.info("REST request to get zodiac analytics");

        ZodiacAnalyticsResponse response = reportService.getZodiacAnalytics();

        return ResponseEntity.ok(ApiResponse.success("Zodiac analytics retrieved successfully", response));
    }

    /**
     * Get compatibility matrix for heatmap
     * GET /api/reports/compatibility-matrix
     */
    @GetMapping("/compatibility-matrix")
    @Operation(summary = "Get compatibility matrix", description = "Retrieve compatibility matrix for heatmap visualization")
    public ResponseEntity<ApiResponse<CompatibilityMatrixResponse>> getCompatibilityMatrix(
            @Parameter(description = "Department ID to filter (optional)")
            @RequestParam(required = false) Long departmentId,
            @Parameter(description = "Maximum number of members (default: 20)")
            @RequestParam(defaultValue = "20") int maxMembers) {

        log.info("REST request to get compatibility matrix for department: {}, maxMembers: {}", departmentId, maxMembers);

        CompatibilityMatrixResponse response = reportService.getCompatibilityMatrixForReport(departmentId, maxMembers);

        return ResponseEntity.ok(ApiResponse.success("Compatibility matrix retrieved successfully", response));
    }

    /**
     * Get department composition analytics
     * GET /api/reports/department-composition
     */
    @GetMapping("/department-composition")
    @Operation(summary = "Get department composition", description = "Retrieve detailed department zodiac composition")
    public ResponseEntity<ApiResponse<DepartmentCompositionResponse>> getDepartmentComposition(
            @Parameter(description = "Department ID (optional - all if not specified)")
            @RequestParam(required = false) Long departmentId) {

        log.info("REST request to get department composition for department: {}", departmentId);

        DepartmentCompositionResponse response = reportService.getDepartmentComposition(departmentId);

        return ResponseEntity.ok(ApiResponse.success("Department composition retrieved successfully", response));
    }

    /**
     * Get fun stats and insights
     * GET /api/reports/fun-stats
     */
    @GetMapping("/fun-stats")
    @Operation(summary = "Get fun statistics", description = "Retrieve fun zodiac statistics and insights")
    public ResponseEntity<ApiResponse<FunStatsResponse>> getFunStats() {
        log.info("REST request to get fun stats");

        FunStatsResponse response = reportService.getFunStats();

        return ResponseEntity.ok(ApiResponse.success("Fun stats retrieved successfully", response));
    }

    /**
     * Get comprehensive compatibility report
     * POST /api/reports/compatibility-report
     */
    @PostMapping("/compatibility-report")
    @Operation(summary = "Generate compatibility report", description = "Generate detailed compatibility report for members/team")
    public ResponseEntity<ApiResponse<CompatibilityReport>> generateCompatibilityReport(
            @Valid @RequestBody GenerateReportRequest request) {

        log.info("REST request to generate compatibility report: {}", request.getReportType());

        CompatibilityReport response = reportService.generateCompatibilityReport(request);

        return ResponseEntity.ok(ApiResponse.success("Compatibility report generated successfully", response));
    }

    /**
     * Export report to PDF
     * POST /api/reports/export-pdf
     */
    @PostMapping("/export-pdf")
    @Operation(summary = "Export report to PDF", description = "Generate and download PDF report")
    public ResponseEntity<Resource> exportPdfReport(
            @Valid @RequestBody GenerateReportRequest request) {

        log.info("REST request to export PDF report: {}", request.getReportType());

        byte[] pdfBytes = reportService.generatePdfReport(request);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        String filename = String.format("zodiac_report_%s_%s.pdf",
                request.getReportType().toLowerCase(),
                LocalDate.now().toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }

    /**
     * Export report to Excel
     * POST /api/reports/export-excel
     */
    @PostMapping("/export-excel")
    @Operation(summary = "Export report to Excel", description = "Generate and download Excel report")
    public ResponseEntity<Resource> exportExcelReport(
            @Valid @RequestBody GenerateReportRequest request) {

        log.info("REST request to export Excel report: {}", request.getReportType());

        byte[] excelBytes = reportService.generateExcelReport(request);

        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        String filename = String.format("zodiac_report_%s_%s.xlsx",
                request.getReportType().toLowerCase(),
                LocalDate.now().toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelBytes.length)
                .body(resource);
    }

    /**
     * Get hiring trends over time
     * GET /api/reports/hiring-trends
     */
    @GetMapping("/hiring-trends")
    @Operation(summary = "Get hiring trends", description = "Retrieve hiring trends by zodiac signs over time")
    public ResponseEntity<ApiResponse<HiringTrendsResponse>> getHiringTrends(
            @Parameter(description = "Number of months to analyze (default: 12)")
            @RequestParam(defaultValue = "12") int months) {

        log.info("REST request to get hiring trends for last {} months", months);

        HiringTrendsResponse response = reportService.getHiringTrends(months);

        return ResponseEntity.ok(ApiResponse.success("Hiring trends retrieved successfully", response));
    }

    /**
     * Get team performance predictions
     * POST /api/reports/team-predictions
     */
    @PostMapping("/team-predictions")
    @Operation(summary = "Get team predictions", description = "Get AI-powered team performance predictions based on zodiac compatibility")
    public ResponseEntity<ApiResponse<TeamPredictionsResponse>> getTeamPredictions(
            @RequestBody TeamPredictionRequest request) {

        log.info("REST request to get team predictions for {} members", request.getMemberIds().size());

        TeamPredictionsResponse response = reportService.getTeamPredictions(request);

        return ResponseEntity.ok(ApiResponse.success("Team predictions generated successfully", response));
    }

    /**
     * Get best team recommendations
     * GET /api/reports/team-recommendations
     */
    @GetMapping("/team-recommendations")
    @Operation(summary = "Get team recommendations", description = "Get recommended team compositions based on compatibility")
    public ResponseEntity<ApiResponse<TeamRecommendationsResponse>> getTeamRecommendations(
            @Parameter(description = "Target team size")
            @RequestParam(defaultValue = "5") int teamSize,
            @Parameter(description = "Department ID to filter (optional)")
            @RequestParam(required = false) Long departmentId) {

        log.info("REST request to get team recommendations: size={}, department={}", teamSize, departmentId);

        TeamRecommendationsResponse response = reportService.getTeamRecommendations(teamSize, departmentId);

        return ResponseEntity.ok(ApiResponse.success("Team recommendations retrieved successfully", response));
    }

    /**
     * Get element balance recommendations
     * GET /api/reports/element-recommendations
     */
    @GetMapping("/element-recommendations")
    @Operation(summary = "Get element recommendations", description = "Get recommendations for achieving element balance")
    public ResponseEntity<ApiResponse<ElementRecommendationsResponse>> getElementRecommendations() {
        log.info("REST request to get element recommendations");

        ElementRecommendationsResponse response = reportService.getElementRecommendations();

        return ResponseEntity.ok(ApiResponse.success("Element recommendations retrieved successfully", response));
    }

    /**
     * Preview report before export
     * POST /api/reports/preview
     */
    @PostMapping("/preview")
    @Operation(summary = "Preview report", description = "Preview report data before exporting")
    public ResponseEntity<ApiResponse<ReportPreviewResponse>> previewReport(
            @Valid @RequestBody GenerateReportRequest request) {

        log.info("REST request to preview report: {}", request.getReportType());

        ReportPreviewResponse response = reportService.previewReport(request);

        return ResponseEntity.ok(ApiResponse.success("Report preview generated successfully", response));
    }
}