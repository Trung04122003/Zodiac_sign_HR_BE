package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.*;
import com.jci.zodiac.dto.response.BulkOperationResponse;
import com.jci.zodiac.dto.response.ImportResult;
import com.jci.zodiac.service.BulkOperationsService;
import com.jci.zodiac.service.CsvImportService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * BulkOperationsController - REST APIs for bulk member operations
 * Base URL: /api/members/bulk
 */
@RestController
@RequestMapping("/members/bulk")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bulk Operations", description = "APIs for bulk member operations")
public class BulkOperationsController {

    private final BulkOperationsService bulkOperationsService;
    private final CsvImportService csvImportService;

    /**
     * Bulk create members
     * POST /api/members/bulk/create
     */
    @PostMapping("/create")
    @Operation(summary = "Bulk create members", description = "Create multiple members at once")
    public ResponseEntity<ApiResponse<BulkOperationResponse>> bulkCreateMembers(
            @Valid @RequestBody BulkMemberRequest request) {

        log.info("REST request to bulk create {} members", request.getMembers().size());

        BulkOperationResponse response = bulkOperationsService.bulkCreateMembers(request);

        HttpStatus status = response.isFullySuccessful() ? HttpStatus.CREATED :
                response.isPartiallySuccessful() ? HttpStatus.MULTI_STATUS :
                        HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.success("Bulk creation completed", response));
    }

    /**
     * Bulk update member status
     * PUT /api/members/bulk/status
     */
    @PutMapping("/status")
    @Operation(summary = "Bulk update status", description = "Update status for multiple members")
    public ResponseEntity<ApiResponse<BulkOperationResponse>> bulkUpdateStatus(
            @Valid @RequestBody BulkUpdateStatusRequest request) {

        log.info("REST request to bulk update status for {} members", request.getMemberIds().size());

        BulkOperationResponse response = bulkOperationsService.bulkUpdateStatus(request);

        HttpStatus status = response.isFullySuccessful() ? HttpStatus.OK :
                response.isPartiallySuccessful() ? HttpStatus.MULTI_STATUS :
                        HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.success("Bulk status update completed", response));
    }

    /**
     * Bulk delete members
     * DELETE /api/members/bulk/delete
     */
    @DeleteMapping("/delete")
    @Operation(summary = "Bulk delete members", description = "Delete multiple members (soft or hard delete)")
    public ResponseEntity<ApiResponse<BulkOperationResponse>> bulkDeleteMembers(
            @Valid @RequestBody BulkDeleteRequest request) {

        log.info("REST request to bulk delete {} members (permanent: {})",
                request.getMemberIds().size(), request.isPermanent());

        BulkOperationResponse response = bulkOperationsService.bulkDeleteMembers(request);

        HttpStatus status = response.isFullySuccessful() ? HttpStatus.OK :
                response.isPartiallySuccessful() ? HttpStatus.MULTI_STATUS :
                        HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.success("Bulk deletion completed", response));
    }

    /**
     * Bulk update department
     * PUT /api/members/bulk/department/{departmentId}
     */
    @PutMapping("/department/{departmentId}")
    @Operation(summary = "Bulk update department", description = "Assign multiple members to a department")
    public ResponseEntity<ApiResponse<BulkOperationResponse>> bulkUpdateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @RequestBody List<Long> memberIds) {

        log.info("REST request to bulk update department for {} members", memberIds.size());

        BulkOperationResponse response = bulkOperationsService.bulkUpdateDepartment(memberIds, departmentId);

        HttpStatus status = response.isFullySuccessful() ? HttpStatus.OK :
                response.isPartiallySuccessful() ? HttpStatus.MULTI_STATUS :
                        HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.success("Bulk department update completed", response));
    }

    /**
     * Import members from CSV
     * POST /api/members/bulk/import/csv
     */
    @PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import from CSV", description = "Import members from CSV file")
    public ResponseEntity<ApiResponse<ImportResult>> importFromCsv(
            @Parameter(description = "CSV file") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Skip header row") @RequestParam(defaultValue = "true") boolean skipHeader,
            @Parameter(description = "Date format") @RequestParam(defaultValue = "yyyy-MM-dd") String dateFormat) {

        log.info("REST request to import members from CSV: {}", file.getOriginalFilename());

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("File is empty"));
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Only CSV files are supported"));
        }

        ImportMembersRequest config = ImportMembersRequest.builder()
                .skipHeader(skipHeader)
                .dateFormat(dateFormat)
                .build();

        ImportResult result = csvImportService.importFromCsv(file, config);

        HttpStatus status = result.isFullySuccessful() ? HttpStatus.CREATED :
                result.getSuccessfulImports() > 0 ? HttpStatus.MULTI_STATUS :
                        HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.success(
                        String.format("Import completed: %d successful, %d failed",
                                result.getSuccessfulImports(), result.getFailedImports()),
                        result
                ));
    }

    /**
     * Download CSV template
     * GET /api/members/bulk/template/csv
     */
    @GetMapping("/template/csv")
    @Operation(summary = "Download CSV template", description = "Download CSV template for bulk import")
    public ResponseEntity<String> downloadCsvTemplate() {
        log.info("REST request to download CSV template");

        String csvContent = csvImportService.generateCsvTemplate();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"member_import_template.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }
}