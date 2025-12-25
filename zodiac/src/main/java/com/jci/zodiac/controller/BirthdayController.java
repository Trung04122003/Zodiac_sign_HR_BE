package com.jci.zodiac.controller;

import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.service.BirthdayService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.List;

/**
 * BirthdayController - REST APIs for birthday tracking
 * Base URL: /api/birthdays
 */
@RestController
@RequestMapping("/birthdays")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Birthday Tracker", description = "APIs for tracking member birthdays")
public class BirthdayController {

    private final BirthdayService birthdayService;

    /**
     * Get birthdays today
     * GET /api/birthdays/today
     */
    @GetMapping("/today")
    @Operation(summary = "Get birthdays today", description = "Retrieve members with birthdays today")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getBirthdaysToday() {

        log.info("REST request to get birthdays today");

        List<MemberSummaryResponse> response = birthdayService.getBirthdaysToday();

        String message = response.isEmpty()
                ? "No birthdays today"
                : String.format("🎂 %d birthday(s) today!", response.size());

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    /**
     * Get upcoming birthdays
     * GET /api/birthdays/upcoming
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming birthdays", description = "Retrieve birthdays in the next N days")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getUpcomingBirthdays(
            @Parameter(description = "Number of days ahead (default: 30)")
            @RequestParam(defaultValue = "30") int daysAhead) {

        log.info("REST request to get upcoming birthdays in {} days", daysAhead);

        List<MemberSummaryResponse> response = birthdayService.getUpcomingBirthdays(daysAhead);

        String message = String.format("Found %d birthday(s) in next %d days",
                response.size(), daysAhead);

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    /**
     * Get birthdays by month
     * GET /api/birthdays/month/{month}
     */
    @GetMapping("/month/{month}")
    @Operation(summary = "Get birthdays by month", description = "Retrieve all birthdays in a specific month")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getBirthdaysByMonth(
            @Parameter(description = "Month (1-12 or JANUARY, FEBRUARY, etc.)")
            @PathVariable Month month) {

        log.info("REST request to get birthdays in month: {}", month);

        List<MemberSummaryResponse> response = birthdayService.getBirthdaysByMonth(month);

        String message = String.format("Found %d birthday(s) in %s", response.size(), month);

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    /**
     * Get birthdays by zodiac sign
     * GET /api/birthdays/zodiac/{sign}
     */
    @GetMapping("/zodiac/{sign}")
    @Operation(summary = "Get birthdays by zodiac", description = "Retrieve birthdays for a specific zodiac sign")
    public ResponseEntity<ApiResponse<List<MemberSummaryResponse>>> getBirthdaysByZodiacSign(
            @Parameter(description = "Zodiac sign") @PathVariable Member.ZodiacSign sign) {

        log.info("REST request to get birthdays for zodiac: {}", sign);

        List<MemberSummaryResponse> response = birthdayService.getBirthdaysByZodiacSign(sign);

        String message = String.format("Found %d %s birthday(s)", response.size(), sign);

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    /**
     * Check if there are birthdays today
     * GET /api/birthdays/check
     */
    @GetMapping("/check")
    @Operation(summary = "Check birthdays today", description = "Check if there are any birthdays today")
    public ResponseEntity<ApiResponse<BirthdayCheckResponse>> checkBirthdaysToday() {

        log.info("REST request to check if there are birthdays today");

        boolean hasBirthdays = birthdayService.hasBirthdaysToday();
        long count = hasBirthdays ? birthdayService.getBirthdaysToday().size() : 0;

        BirthdayCheckResponse response = new BirthdayCheckResponse(hasBirthdays, count);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get upcoming birthday count
     * GET /api/birthdays/count
     */
    @GetMapping("/count")
    @Operation(summary = "Count upcoming birthdays", description = "Get count of birthdays in next N days")
    public ResponseEntity<ApiResponse<BirthdayCountResponse>> countUpcomingBirthdays(
            @Parameter(description = "Number of days ahead")
            @RequestParam(defaultValue = "7") int daysAhead) {

        log.info("REST request to count upcoming birthdays in {} days", daysAhead);

        long count = birthdayService.countUpcomingBirthdays(daysAhead);

        BirthdayCountResponse response = new BirthdayCountResponse(daysAhead, count);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // Response DTOs
    public record BirthdayCheckResponse(boolean hasBirthdays, long count) {}
    public record BirthdayCountResponse(int daysAhead, long count) {}
}