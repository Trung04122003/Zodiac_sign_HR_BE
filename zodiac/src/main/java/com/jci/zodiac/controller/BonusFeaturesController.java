package com.jci.zodiac.controller;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.service.BirthdayNotificationService;
import com.jci.zodiac.service.DailyZodiacInsightsService;
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
 * BonusFeaturesController - REST APIs for Week 12 bonus features
 * Base URL: /api/bonus
 *
 * FIXED: Use helper methods for zodiac symbols
 */
@RestController
@RequestMapping("/bonus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bonus Features", description = "Fun features: Daily insights, notifications, tips")
public class BonusFeaturesController {

    private final DailyZodiacInsightsService insightsService;
    private final BirthdayNotificationService birthdayNotificationService;

    // ==================== Daily Insights ====================

    /**
     * Get today's daily zodiac insight
     * GET /api/bonus/daily-insight
     */
    @GetMapping("/daily-insight")
    @Operation(summary = "Get daily insight", description = "Retrieve today's featured zodiac insight")
    public ResponseEntity<ApiResponse<DailyZodiacInsightsService.DailyInsightResponse>> getDailyInsight() {
        log.info("REST request to get daily zodiac insight");

        DailyZodiacInsightsService.DailyInsightResponse insight = insightsService.getDailyInsight();

        return ResponseEntity.ok(ApiResponse.success("Today's zodiac insight", insight));
    }

    /**
     * Get personalized insight for user
     * GET /api/bonus/personal-insight?sign={sign}
     */
    @GetMapping("/personal-insight")
    @Operation(summary = "Get personalized insight", description = "Get personalized daily insight based on your zodiac sign")
    public ResponseEntity<ApiResponse<DailyZodiacInsightsService.PersonalizedInsightResponse>> getPersonalizedInsight(
            @Parameter(description = "Your zodiac sign") @RequestParam Member.ZodiacSign sign) {

        log.info("REST request to get personalized insight for: {}", sign);

        DailyZodiacInsightsService.PersonalizedInsightResponse insight =
                insightsService.getPersonalizedInsight(sign);

        return ResponseEntity.ok(ApiResponse.success("Your personalized insight", insight));
    }

    /**
     * Refresh daily insight (admin/testing)
     * POST /api/bonus/daily-insight/refresh
     */
    @PostMapping("/daily-insight/refresh")
    @Operation(summary = "Refresh daily insight", description = "Manually refresh today's daily insight (for testing)")
    public ResponseEntity<ApiResponse<DailyZodiacInsightsService.DailyInsightResponse>> refreshDailyInsight() {
        log.info("REST request to refresh daily insight");

        DailyZodiacInsightsService.DailyInsightResponse insight = insightsService.refreshInsight();

        return ResponseEntity.ok(ApiResponse.success("Daily insight refreshed", insight));
    }

    /**
     * Get random insight
     * GET /api/bonus/random-insight
     */
    @GetMapping("/random-insight")
    @Operation(summary = "Get random insight", description = "Get a random zodiac insight")
    public ResponseEntity<ApiResponse<String>> getRandomInsight() {
        log.info("REST request to get random insight");

        String insight = insightsService.getRandomInsight();

        return ResponseEntity.ok(ApiResponse.success("Random zodiac wisdom", insight));
    }

    /**
     * Get all insights for a specific sign
     * GET /api/bonus/insights/{sign}
     */
    @GetMapping("/insights/{sign}")
    @Operation(summary = "Get insights for sign", description = "Get all available insights for a specific zodiac sign")
    public ResponseEntity<ApiResponse<List<String>>> getInsightsForSign(
            @Parameter(description = "Zodiac sign") @PathVariable Member.ZodiacSign sign) {

        log.info("REST request to get all insights for: {}", sign);

        List<String> insights = insightsService.getAllInsightsForSign(sign);

        return ResponseEntity.ok(ApiResponse.success(
                String.format("All insights for %s %s", sign, getZodiacSymbol(sign)),
                insights
        ));
    }

    /**
     * Get compatibility tip between two signs
     * GET /api/bonus/compatibility-tip?sign1={sign1}&sign2={sign2}
     */
    @GetMapping("/compatibility-tip")
    @Operation(summary = "Get compatibility tip", description = "Get a quick compatibility tip for two zodiac signs")
    public ResponseEntity<ApiResponse<String>> getCompatibilityTip(
            @Parameter(description = "First zodiac sign") @RequestParam Member.ZodiacSign sign1,
            @Parameter(description = "Second zodiac sign") @RequestParam Member.ZodiacSign sign2) {

        log.info("REST request to get compatibility tip for {} and {}", sign1, sign2);

        String tip = insightsService.getCompatibilityTip(sign1, sign2);

        return ResponseEntity.ok(ApiResponse.success(
                String.format("Tip for %s & %s", sign1, sign2),
                tip
        ));
    }

    // ==================== Birthday Notifications ====================

    /**
     * Trigger manual birthday check (for testing)
     * POST /api/bonus/birthday-check
     */
    @PostMapping("/birthday-check")
    @Operation(summary = "Manual birthday check", description = "Manually trigger today's birthday check (for testing)")
    public ResponseEntity<ApiResponse<String>> manualBirthdayCheck() {
        log.info("REST request to manually trigger birthday check");

        birthdayNotificationService.manualBirthdayCheck();

        return ResponseEntity.ok(ApiResponse.success("Birthday check completed. Check logs for results."));
    }

    /**
     * Trigger manual upcoming check
     * POST /api/bonus/upcoming-check
     */
    @PostMapping("/upcoming-check")
    @Operation(summary = "Manual upcoming check", description = "Manually trigger upcoming birthdays check")
    public ResponseEntity<ApiResponse<String>> manualUpcomingCheck() {
        log.info("REST request to manually trigger upcoming check");

        birthdayNotificationService.manualUpcomingCheck();

        return ResponseEntity.ok(ApiResponse.success("Upcoming birthdays check completed. Check logs for results."));
    }

    /**
     * Get notification schedule info
     * GET /api/bonus/notification-schedule
     */
    @GetMapping("/notification-schedule")
    @Operation(summary = "Get notification schedule", description = "Get information about scheduled notification times")
    public ResponseEntity<ApiResponse<NotificationScheduleInfo>> getNotificationSchedule() {
        log.info("REST request to get notification schedule");

        NotificationScheduleInfo info = NotificationScheduleInfo.builder()
                .dailyBirthdayCheck("Every day at 8:00 AM")
                .weeklyUpcomingCheck("Every Monday at 9:00 AM")
                .monthlyReport("1st day of month at 10:00 AM")
                .dailyInsightRotation("Every day at midnight (00:00)")
                .nextRun(birthdayNotificationService.getNextScheduledRun())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Notification schedule", info));
    }

    // ==================== Fun Stats ====================

    /**
     * Get zodiac fun fact
     * GET /api/bonus/fun-fact?sign={sign}
     */
    @GetMapping("/fun-fact")
    @Operation(summary = "Get zodiac fun fact", description = "Get a fun fact about a zodiac sign")
    public ResponseEntity<ApiResponse<ZodiacFunFact>> getFunFact(
            @Parameter(description = "Zodiac sign") @RequestParam Member.ZodiacSign sign) {

        log.info("REST request to get fun fact for: {}", sign);

        ZodiacFunFact fact = ZodiacFunFact.builder()
                .sign(sign)
                .symbol(getZodiacSymbol(sign))
                .element(getElementForSign(sign))
                .funFact(getFunFactForSign(sign))
                .luckyDay(getLuckyDay(sign))
                .strengthKeyword(getStrengthKeyword(sign))
                .build();

        return ResponseEntity.ok(ApiResponse.success("Fun fact", fact));
    }

    // ==================== Helper Methods ====================

    private String getZodiacSymbol(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "♈";
            case Taurus -> "♉";
            case Gemini -> "♊";
            case Cancer -> "♋";
            case Leo -> "♌";
            case Virgo -> "♍";
            case Libra -> "♎";
            case Scorpio -> "♏";
            case Sagittarius -> "♐";
            case Capricorn -> "♑";
            case Aquarius -> "♒";
            case Pisces -> "♓";
        };
    }

    private String getElementForSign(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Leo, Sagittarius -> "Fire";
            case Taurus, Virgo, Capricorn -> "Earth";
            case Gemini, Libra, Aquarius -> "Air";
            case Cancer, Scorpio, Pisces -> "Water";
        };
    }

    private String getFunFactForSign(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "Aries are natural-born leaders! They make up about 8% of the population.";
            case Taurus -> "Taurus is the most likely sign to become a millionaire due to their financial savvy!";
            case Gemini -> "Gemini can adapt to any situation—they're the social chameleons of the zodiac!";
            case Cancer -> "Cancer has the most CEOs among all zodiac signs—they lead with care!";
            case Leo -> "Leos are natural performers! Many famous actors and entertainers are Leos.";
            case Virgo -> "Virgos are perfectionists. They notice details others miss!";
            case Libra -> "Libras are the diplomats of the zodiac—they hate conflict and love harmony.";
            case Scorpio -> "Scorpios are the most intense sign—when they commit, they're all in!";
            case Sagittarius -> "Sagittarius is the luckiest sign of the zodiac—optimism attracts opportunities!";
            case Capricorn -> "Capricorns age backwards—they become more fun as they get older!";
            case Aquarius -> "Aquarius is the rarest zodiac sign and known for genius-level thinking!";
            case Pisces -> "Pisces are the most creative sign—many famous artists are Pisces!";
        };
    }

    private String getLuckyDay(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Scorpio -> "Tuesday";
            case Taurus, Libra -> "Friday";
            case Gemini, Virgo -> "Wednesday";
            case Cancer -> "Monday";
            case Leo -> "Sunday";
            case Sagittarius, Pisces -> "Thursday";
            case Capricorn, Aquarius -> "Saturday";
        };
    }

    private String getStrengthKeyword(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "Courage";
            case Taurus -> "Reliability";
            case Gemini -> "Adaptability";
            case Cancer -> "Nurturing";
            case Leo -> "Confidence";
            case Virgo -> "Precision";
            case Libra -> "Diplomacy";
            case Scorpio -> "Intensity";
            case Sagittarius -> "Optimism";
            case Capricorn -> "Discipline";
            case Aquarius -> "Innovation";
            case Pisces -> "Empathy";
        };
    }

    // Response DTOs
    @lombok.Data
    @lombok.Builder
    public static class NotificationScheduleInfo {
        private String dailyBirthdayCheck;
        private String weeklyUpcomingCheck;
        private String monthlyReport;
        private String dailyInsightRotation;
        private String nextRun;
    }

    @lombok.Data
    @lombok.Builder
    public static class ZodiacFunFact {
        private Member.ZodiacSign sign;
        private String symbol;
        private String element;
        private String funFact;
        private String luckyDay;
        private String strengthKeyword;
    }
}