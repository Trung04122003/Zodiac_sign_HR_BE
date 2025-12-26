package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.CreateDepartmentRequest;
import com.jci.zodiac.dto.request.UpdateDepartmentRequest;
import com.jci.zodiac.dto.response.DepartmentAnalyticsResponse;
import com.jci.zodiac.dto.response.DepartmentResponse;
import com.jci.zodiac.dto.response.DepartmentSummaryResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.service.DepartmentService;
import com.jci.zodiac.service.MemberService;
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
 * DepartmentController - REST APIs for department management
 * Base URL: /api/departments
 */
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Department Management", description = "APIs for managing JCI departments/committees")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final MemberService memberService;

    /**
     * Create department
     * POST /api/departments
     */
    @PostMapping
    @Operation(summary = "Create department", description = "Create a new department/committee")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {

        log.info("REST request to create department: {}", request.getName());

        DepartmentResponse response = departmentService.createDepartment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    /**
     * Get department by ID
     * GET /api/departments/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieve department details by ID")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @Parameter(description = "Department ID") @PathVariable Long id) {

        log.info("REST request to get department by id: {}", id);

        DepartmentResponse response = departmentService.getDepartmentById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get department by code
     * GET /api/departments/code/{code}
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get department by code", description = "Retrieve department by unique code")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentByCode(
            @Parameter(description = "Department code") @PathVariable String code) {

        log.info("REST request to get department by code: {}", code);

        DepartmentResponse response = departmentService.getDepartmentByCode(code);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all departments
     * GET /api/departments
     */
    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieve all departments")
    public ResponseEntity<ApiResponse<List<DepartmentSummaryResponse>>> getAllDepartments() {
        log.info("REST request to get all departments");

        List<DepartmentSummaryResponse> response = departmentService.getAllDepartments();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get active departments
     * GET /api/departments/active
     */
    @GetMapping("/active")
    @Operation(summary = "Get active departments", description = "Retrieve only active departments")
    public ResponseEntity<ApiResponse<List<DepartmentSummaryResponse>>> getActiveDepartments() {
        log.info("REST request to get active departments");

        List<DepartmentSummaryResponse> response = departmentService.getActiveDepartments();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update department
     * PUT /api/departments/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update department", description = "Update department information")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {

        log.info("REST request to update department: {}", id);

        DepartmentResponse response = departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    /**
     * Delete department
     * DELETE /api/departments/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department", description = "Delete a department")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @Parameter(description = "Department ID") @PathVariable Long id) {

        log.info("REST request to delete department: {}", id);

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully"));
    }

    /**
     * Assign member to department
     * POST /api/departments/{departmentId}/members/{memberId}
     */
    @PostMapping("/{departmentId}/members/{memberId}")
    @Operation(summary = "Assign member", description = "Assign a member to this department")
    public ResponseEntity<ApiResponse<Void>> assignMember(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @Parameter(description = "Member ID") @PathVariable Long memberId) {

        log.info("REST request to assign member {} to department {}", memberId, departmentId);

        departmentService.assignMemberToDepartment(departmentId, memberId);

        return ResponseEntity.ok(ApiResponse.success("Member assigned to department successfully"));
    }

    /**
     * Remove member from department
     * DELETE /api/departments/{departmentId}/members/{memberId}
     */
    @DeleteMapping("/{departmentId}/members/{memberId}")
    @Operation(summary = "Remove member", description = "Remove a member from this department")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @Parameter(description = "Member ID") @PathVariable Long memberId) {

        log.info("REST request to remove member {} from department {}", memberId, departmentId);

        departmentService.removeMemberFromDepartment(departmentId, memberId);

        return ResponseEntity.ok(ApiResponse.success("Member removed from department successfully"));
    }

    /**
     * Get department members
     * GET /api/departments/{id}/members
     */
    @GetMapping("/{id}/members")
    @Operation(summary = "Get department members", description = "Retrieve all members in this department")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getDepartmentMembers(
            @Parameter(description = "Department ID") @PathVariable Long id) {

        log.info("REST request to get members for department: {}", id);

        List<MemberSummaryResponse> response = memberService.getMembersByDepartment(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get department analytics
     * GET /api/departments/{id}/analytics
     */
    @GetMapping("/{id}/analytics")
    @Operation(summary = "Get department analytics", description = "Get zodiac analytics and team dynamics for department")
    public ResponseEntity<ApiResponse<DepartmentAnalyticsResponse>> getDepartmentAnalytics(
            @Parameter(description = "Department ID") @PathVariable Long id) {

        log.info("REST request to get analytics for department: {}", id);

        DepartmentAnalyticsResponse response = departmentService.getDepartmentAnalytics(id);

        return ResponseEntity.ok(ApiResponse.success("Department analytics generated", response));
    }
}