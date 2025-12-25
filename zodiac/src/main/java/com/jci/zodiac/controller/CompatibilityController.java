package com.jci.zodiac.controller;

import com.jci.zodiac.entity.ZodiacCompatibility;
import com.jci.zodiac.service.CompatibilityService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CompatibilityController - REST APIs for zodiac compatibility
 * Base URL: /api/compatibility
 */
@RestController
@RequestMapping("/compatibility")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Compatibility Calculator", description = "APIs for calculating zodiac compatibility")
public class CompatibilityController {

    private final CompatibilityService compatibilityService;

    /**
     * Get compatibility between two zodiac signs
     * GET /api/compatibility/signs?sign1={sign1}&sign2={sign2}
     */
    @GetMapping("/signs")
    @Operation(summary = "Get compatibility by signs", description = "Calculate compatibility between two zodiac signs")
    public ResponseEntity<ApiResponse<ZodiacCompatibility>> getCompatibilityBySign(
            @Parameter(description = "First zodiac sign") @RequestParam ZodiacCompatibility.ZodiacSign sign1,
            @Parameter(description = "Second zodiac sign") @RequestParam ZodiacCompatibility.ZodiacSign sign2) {

        log.info("REST request to get compatibility for {} and {}", sign1, sign2);

        ZodiacCompatibility compatibility = compatibilityService.getCompatibilityBySign(sign1, sign2);

        return ResponseEntity.ok(ApiResponse.success(compatibility));
    }

    /**
     * Get compatibility between two members
     * GET /api/compatibility/members?member1={id1}&member2={id2}
     */
    @GetMapping("/members")
    @Operation(summary = "Get compatibility by members", description = "Calculate compatibility between two members")
    public ResponseEntity<ApiResponse<ZodiacCompatibility>> getCompatibilityByMembers(
            @Parameter(description = "First member ID") @RequestParam Long member1,
            @Parameter(description = "Second member ID") @RequestParam Long member2) {

        log.info("REST request to get compatibility for members: {} and {}", member1, member2);

        ZodiacCompatibility compatibility = compatibilityService.getCompatibilityByMembers(member1, member2);

        return ResponseEntity.ok(ApiResponse.success(compatibility));
    }

    /**
     * Get all compatibilities for a zodiac sign
     * GET /api/compatibility/signs/{sign}/all
     */
    @GetMapping("/signs/{sign}/all")
    @Operation(summary = "Get all compatibilities for sign", description = "Get compatibility with all other signs")
    public ResponseEntity<ApiResponse<List<ZodiacCompatibility>>> getAllCompatibilitiesForSign(
            @Parameter(description = "Zodiac sign") @PathVariable ZodiacCompatibility.ZodiacSign sign) {

        log.info("REST request to get all compatibilities for sign: {}", sign);

        List<ZodiacCompatibility> compatibilities = compatibilityService.getAllCompatibilitiesForSign(sign);

        return ResponseEntity.ok(ApiResponse.success(compatibilities));
    }

    /**
     * Get best matches for a sign
     * GET /api/compatibility/signs/{sign}/best
     */
    @GetMapping("/signs/{sign}/best")
    @Operation(summary = "Get best matches", description = "Find top 5 best compatible signs")
    public ResponseEntity<ApiResponse<List<ZodiacCompatibility>>> getBestMatchesForSign(
            @Parameter(description = "Zodiac sign") @PathVariable ZodiacCompatibility.ZodiacSign sign) {

        log.info("REST request to get best matches for sign: {}", sign);

        List<ZodiacCompatibility> matches = compatibilityService.getBestMatchesForSign(sign);

        return ResponseEntity.ok(ApiResponse.success(matches));
    }

    /**
     * Get best compatible pairs overall
     * GET /api/compatibility/best-pairs?limit={limit}
     */
    @GetMapping("/best-pairs")
    @Operation(summary = "Get best compatible pairs", description = "Find top compatible zodiac sign pairs")
    public ResponseEntity<ApiResponse<List<ZodiacCompatibility>>> getBestCompatiblePairs(
            @Parameter(description = "Number of pairs to return") @RequestParam(defaultValue = "10") int limit) {

        log.info("REST request to get top {} best compatible pairs", limit);

        List<ZodiacCompatibility> pairs = compatibilityService.getBestCompatiblePairs(limit);

        return ResponseEntity.ok(ApiResponse.success(pairs));
    }

    /**
     * Get challenging pairs
     * GET /api/compatibility/challenging-pairs
     */
    @GetMapping("/challenging-pairs")
    @Operation(summary = "Get challenging pairs", description = "Find zodiac pairs with low compatibility")
    public ResponseEntity<ApiResponse<List<ZodiacCompatibility>>> getChallengingPairs() {
        log.info("REST request to get challenging pairs");

        List<ZodiacCompatibility> pairs = compatibilityService.getChallengingPairs();

        return ResponseEntity.ok(ApiResponse.success(pairs));
    }

    /**
     * Calculate team compatibility
     * POST /api/compatibility/team
     */
    @PostMapping("/team")
    @Operation(summary = "Calculate team compatibility", description = "Analyze compatibility for a team of members")
    public ResponseEntity<ApiResponse<CompatibilityService.TeamCompatibilityResult>> calculateTeamCompatibility(
            @RequestBody List<Long> memberIds) {

        log.info("REST request to calculate team compatibility for {} members", memberIds.size());

        CompatibilityService.TeamCompatibilityResult result =
                compatibilityService.calculateTeamCompatibility(memberIds);

        return ResponseEntity.ok(ApiResponse.success("Team compatibility calculated", result));
    }

    /**
     * Find best member pairs
     * GET /api/compatibility/member-pairs/best?limit={limit}
     */
    @GetMapping("/member-pairs/best")
    @Operation(summary = "Find best member pairs", description = "Find top compatible member pairs in the organization")
    public ResponseEntity<ApiResponse<List<CompatibilityService.MemberPairCompatibility>>> findBestMemberPairs(
            @Parameter(description = "Number of pairs to return") @RequestParam(defaultValue = "10") int limit) {

        log.info("REST request to find top {} best member pairs", limit);

        List<CompatibilityService.MemberPairCompatibility> pairs =
                compatibilityService.findBestMemberPairs(limit);

        return ResponseEntity.ok(ApiResponse.success(pairs));
    }
}