package com.jci.zodiac.controller;

import com.jci.zodiac.entity.ZodiacProfile;
import com.jci.zodiac.service.ZodiacProfileService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ZodiacProfileController - REST APIs for zodiac profiles
 * Base URL: /api/zodiac/profiles
 */
@RestController
@RequestMapping("/zodiac/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Zodiac Profiles", description = "APIs for 12 zodiac sign profiles")
public class ZodiacProfileController {

    private final ZodiacProfileService zodiacProfileService;

    /**
     * Get all 12 zodiac profiles
     * GET /api/zodiac/profiles
     */
    @GetMapping
    @Operation(summary = "Get all zodiac profiles", description = "Retrieve all 12 zodiac sign profiles")
    public ResponseEntity<ApiResponse<List<ZodiacProfile>>> getAllProfiles() {
        log.info("REST request to get all zodiac profiles");

        List<ZodiacProfile> profiles = zodiacProfileService.getAllProfiles();

        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    /**
     * Get profile by zodiac sign
     * GET /api/zodiac/profiles/{sign}
     */
    @GetMapping("/{sign}")
    @Operation(summary = "Get profile by sign", description = "Retrieve detailed profile for a specific zodiac sign")
    public ResponseEntity<ApiResponse<ZodiacProfile>> getProfileBySign(
            @Parameter(description = "Zodiac sign (e.g., Sagittarius)")
            @PathVariable ZodiacProfile.ZodiacSign sign) {

        log.info("REST request to get profile for sign: {}", sign);

        ZodiacProfile profile = zodiacProfileService.getProfileBySign(sign);

        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    /**
     * Get profiles by element
     * GET /api/zodiac/profiles/element/{element}
     */
    @GetMapping("/element/{element}")
    @Operation(summary = "Get profiles by element", description = "Retrieve all zodiac signs of a specific element")
    public ResponseEntity<ApiResponse<List<ZodiacProfile>>> getProfilesByElement(
            @Parameter(description = "Element (Fire/Earth/Air/Water)")
            @PathVariable ZodiacProfile.Element element) {

        log.info("REST request to get profiles for element: {}", element);

        List<ZodiacProfile> profiles = zodiacProfileService.getProfilesByElement(element);

        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    /**
     * Get profiles by modality
     * GET /api/zodiac/profiles/modality/{modality}
     */
    @GetMapping("/modality/{modality}")
    @Operation(summary = "Get profiles by modality", description = "Retrieve zodiac signs by modality (Cardinal/Fixed/Mutable)")
    public ResponseEntity<ApiResponse<List<ZodiacProfile>>> getProfilesByModality(
            @Parameter(description = "Modality (Cardinal/Fixed/Mutable)")
            @PathVariable ZodiacProfile.Modality modality) {

        log.info("REST request to get profiles for modality: {}", modality);

        List<ZodiacProfile> profiles = zodiacProfileService.getProfilesByModality(modality);

        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    /**
     * Get element distribution
     * GET /api/zodiac/profiles/stats/elements
     */
    @GetMapping("/stats/elements")
    @Operation(summary = "Get element distribution", description = "Get distribution of zodiac signs across elements")
    public ResponseEntity<ApiResponse<Map<ZodiacProfile.Element, Long>>> getElementDistribution() {
        log.info("REST request to get element distribution");

        Map<ZodiacProfile.Element, Long> distribution = zodiacProfileService.getElementDistribution();

        return ResponseEntity.ok(ApiResponse.success(distribution));
    }

    /**
     * Search profiles
     * GET /api/zodiac/profiles/search?keyword={keyword}
     */
    @GetMapping("/search")
    @Operation(summary = "Search zodiac profiles", description = "Search profiles by keyword")
    public ResponseEntity<ApiResponse<List<ZodiacProfile>>> searchProfiles(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {

        log.info("REST request to search profiles with keyword: {}", keyword);

        List<ZodiacProfile> profiles = zodiacProfileService.searchProfiles(keyword);

        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    /**
     * Update zodiac profile (customization)
     * PUT /api/zodiac/profiles/{sign}
     */
    @PutMapping("/{sign}")
    @Operation(summary = "Update zodiac profile", description = "Customize zodiac profile attributes")
    public ResponseEntity<ApiResponse<ZodiacProfile>> updateProfile(
            @Parameter(description = "Zodiac sign") @PathVariable ZodiacProfile.ZodiacSign sign,
            @RequestBody ZodiacProfile updates) {

        log.info("REST request to update profile for sign: {}", sign);

        ZodiacProfile updated = zodiacProfileService.updateProfile(sign, updates);

        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }
}