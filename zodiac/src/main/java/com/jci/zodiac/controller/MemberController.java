package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.request.MemberSearchRequest;
import com.jci.zodiac.dto.request.UpdateMemberRequest;
import com.jci.zodiac.dto.response.MemberResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.service.MemberService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MemberController - REST APIs for member management
 * Base URL: /api/members
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member Management", description = "APIs for managing JCI members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Create a new member
     * POST /api/members
     */
    @PostMapping
    @Operation(summary = "Create new member", description = "Create a new JCI member with auto-generated member code and zodiac calculation")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Valid @RequestBody CreateMemberRequest request) {

        log.info("REST request to create member: {}", request.getFullName());

        MemberResponse response = memberService.createMember(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member created successfully", response));
    }

    /**
     * Get member by ID
     * GET /api/members/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID", description = "Retrieve full member details by ID")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(
            @Parameter(description = "Member ID") @PathVariable Long id) {

        log.info("REST request to get member by id: {}", id);

        MemberResponse response = memberService.getMemberById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get member by member code
     * GET /api/members/code/{memberCode}
     */
    @GetMapping("/code/{memberCode}")
    @Operation(summary = "Get member by code", description = "Retrieve member details by member code (e.g., JCI-DN-001)")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberByCode(
            @Parameter(description = "Member Code (e.g., JCI-DN-001)") @PathVariable String memberCode) {

        log.info("REST request to get member by code: {}", memberCode);

        MemberResponse response = memberService.getMemberByCode(memberCode);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all members (paginated)
     * GET /api/members
     */
    @GetMapping
    @Operation(summary = "Get all members", description = "Retrieve paginated list of all members")
    public ResponseEntity<ApiResponse<Page<MemberSummaryResponse>>> getAllMembers(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get all members - page: {}, size: {}", page, size);

        Page<MemberSummaryResponse> response = memberService.getAllMembers(page, size, sortBy, sortDirection);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Search and filter members
     * POST /api/members/search
     */
    @PostMapping("/search")
    @Operation(summary = "Search members", description = "Search and filter members by various criteria")
    public ResponseEntity<ApiResponse<Page<MemberSummaryResponse>>> searchMembers(
            @RequestBody MemberSearchRequest searchRequest) {

        log.info("REST request to search members: {}", searchRequest);

        Page<MemberSummaryResponse> response = memberService.searchMembers(searchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update member
     * PUT /api/members/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update member", description = "Update existing member information")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @Parameter(description = "Member ID") @PathVariable Long id,
            @Valid @RequestBody UpdateMemberRequest request) {

        log.info("REST request to update member: {}", id);

        MemberResponse response = memberService.updateMember(id, request);

        return ResponseEntity.ok(ApiResponse.success("Member updated successfully", response));
    }

    /**
     * Delete member (soft delete)
     * DELETE /api/members/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete member", description = "Soft delete member (changes status to Inactive)")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @Parameter(description = "Member ID") @PathVariable Long id) {

        log.info("REST request to delete member: {}", id);

        memberService.deleteMember(id);

        return ResponseEntity.ok(ApiResponse.success("Member deleted successfully"));
    }

    /**
     * Permanently delete member
     * DELETE /api/members/{id}/permanent
     */
    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Permanently delete member", description = "Hard delete member from database (cannot be undone)")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteMember(
            @Parameter(description = "Member ID") @PathVariable Long id) {

        log.warn("REST request to permanently delete member: {}", id);

        memberService.permanentlyDeleteMember(id);

        return ResponseEntity.ok(ApiResponse.success("Member permanently deleted"));
    }

    /**
     * Get active members only
     * GET /api/members/active
     */
    @GetMapping("/active")
    @Operation(summary = "Get active members", description = "Retrieve all members with Active status")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getActiveMembers() {

        log.info("REST request to get active members");

        List<MemberSummaryResponse> response = memberService.getActiveMembers();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get members by zodiac sign
     * GET /api/members/zodiac/{sign}
     */
    @GetMapping("/zodiac/{sign}")
    @Operation(summary = "Get members by zodiac sign", description = "Retrieve members filtered by zodiac sign")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getMembersByZodiacSign(
            @Parameter(description = "Zodiac sign (e.g., Sagittarius)") @PathVariable Member.ZodiacSign sign) {

        log.info("REST request to get members by zodiac sign: {}", sign);

        List<MemberSummaryResponse> response = memberService.getMembersByZodiacSign(sign);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get members by zodiac element
     * GET /api/members/element/{element}
     */
    @GetMapping("/element/{element}")
    @Operation(summary = "Get members by zodiac element", description = "Retrieve members filtered by zodiac element (Fire/Earth/Air/Water)")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getMembersByZodiacElement(
            @Parameter(description = "Zodiac element (Fire/Earth/Air/Water)") @PathVariable Member.ZodiacElement element) {

        log.info("REST request to get members by zodiac element: {}", element);

        List<MemberSummaryResponse> response = memberService.getMembersByZodiacElement(element);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get members by department
     * GET /api/members/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get members by department", description = "Retrieve all members in a specific department")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getMembersByDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {

        log.info("REST request to get members by department: {}", departmentId);

        List<MemberSummaryResponse> response = memberService.getMembersByDepartment(departmentId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get member statistics
     * GET /api/members/stats
     */
    @GetMapping("/stats")
    @Operation(summary = "Get member statistics", description = "Retrieve member count statistics")
    public ResponseEntity<ApiResponse<MemberStats>> getMemberStats() {

        log.info("REST request to get member statistics");

        long totalMembers = memberService.countTotalMembers();
        long activeMembers = memberService.countActiveMembers();

        MemberStats stats = new MemberStats(totalMembers, activeMembers);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * Inner class for statistics response
     */
    public record MemberStats(long totalMembers, long activeMembers) {}
}