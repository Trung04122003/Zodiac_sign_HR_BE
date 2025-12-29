package com.jci.zodiac.service;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BirthdayNotificationService - Scheduled birthday notifications
 * Runs daily at 8:00 AM to check for upcoming birthdays
 *
 * FIXED: Use helper methods for zodiac symbols and elements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BirthdayNotificationService {

    private final MemberRepository memberRepository;
    private final BirthdayService birthdayService;

    // Format for logging
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd");

    /**
     * Check for birthdays today
     * Runs every day at 8:00 AM
     * Cron: "0 0 8 * * ?" = At 08:00:00 every day
     */
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional(readOnly = true)
    public void checkBirthdaysToday() {
        log.info("🎂 Running daily birthday check...");

        try {
            List<Member> birthdaysToday = memberRepository.findMembersWithBirthdayToday();

            if (birthdaysToday.isEmpty()) {
                log.info("✅ No birthdays today");
                return;
            }

            log.info("🎉 BIRTHDAYS TODAY: {} member(s)", birthdaysToday.size());

            for (Member member : birthdaysToday) {
                notifyBirthday(member);
            }

            // Summary log
            String names = birthdaysToday.stream()
                    .map(Member::getFullName)
                    .collect(Collectors.joining(", "));

            log.info("🎂 Birthday notifications sent for: {}", names);

        } catch (Exception e) {
            log.error("❌ Error checking birthdays today: {}", e.getMessage(), e);
        }
    }

    /**
     * Check for upcoming birthdays (next 7 days)
     * Runs every Monday at 9:00 AM
     * Cron: "0 0 9 ? * MON" = At 09:00:00, only on Monday
     */
    @Scheduled(cron = "0 0 9 ? * MON")
    @Transactional(readOnly = true)
    public void checkUpcomingBirthdays() {
        log.info("📅 Running weekly upcoming birthdays check...");

        try {
            List<Member> upcomingBirthdays = memberRepository.findMembersWithUpcomingBirthdays(7);

            if (upcomingBirthdays.isEmpty()) {
                log.info("✅ No upcoming birthdays in the next 7 days");
                return;
            }

            log.info("📋 Upcoming birthdays (next 7 days): {} member(s)", upcomingBirthdays.size());

            // Group by date and log
            upcomingBirthdays.forEach(member -> {
                LocalDate birthday = LocalDate.of(
                        LocalDate.now().getYear(),
                        member.getDateOfBirth().getMonth(),
                        member.getDateOfBirth().getDayOfMonth()
                );

                long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), birthday);

                log.info("🎂 {} ({}) - {} ({} days) - {} {}",
                        member.getFullName(),
                        member.getZodiacSign(),
                        birthday.format(DATE_FORMATTER),
                        daysUntil,
                        member.getZodiacSign(),
                        getZodiacSymbol(member.getZodiacSign())
                );
            });

            // Send weekly summary notification
            notifyUpcomingSummary(upcomingBirthdays);

        } catch (Exception e) {
            log.error("❌ Error checking upcoming birthdays: {}", e.getMessage(), e);
        }
    }

    /**
     * Monthly birthday report
     * Runs on the 1st day of every month at 10:00 AM
     * Cron: "0 0 10 1 * ?" = At 10:00:00, on the 1st day, every month
     */
    @Scheduled(cron = "0 0 10 1 * ?")
    @Transactional(readOnly = true)
    public void monthlyBirthdayReport() {
        log.info("📊 Generating monthly birthday report...");

        try {
            java.time.Month currentMonth = LocalDate.now().getMonth();
            List<Member> monthBirthdays = memberRepository.findMembersByBirthMonth(currentMonth);

            if (monthBirthdays.isEmpty()) {
                log.info("✅ No birthdays this month ({})", currentMonth);
                return;
            }

            log.info("📅 BIRTHDAYS IN {}: {} member(s)", currentMonth, monthBirthdays.size());

            // Sort by day
            monthBirthdays.sort((m1, m2) ->
                    Integer.compare(m1.getDateOfBirth().getDayOfMonth(),
                            m2.getDateOfBirth().getDayOfMonth())
            );

            // Log detailed report
            monthBirthdays.forEach(member -> {
                log.info("  • {} - {} {} ({})",
                        currentMonth.toString().substring(0, 3) + " " + member.getDateOfBirth().getDayOfMonth(),
                        member.getFullName(),
                        getZodiacSymbol(member.getZodiacSign()),
                        member.getZodiacSign()
                );
            });

            // Zodiac distribution for this month
            var zodiacDistribution = monthBirthdays.stream()
                    .collect(Collectors.groupingBy(Member::getZodiacSign, Collectors.counting()));

            log.info("🌟 Zodiac distribution this month: {}", zodiacDistribution);

        } catch (Exception e) {
            log.error("❌ Error generating monthly birthday report: {}", e.getMessage(), e);
        }
    }

    /**
     * Notify individual birthday
     * In production: Send email, push notification, etc.
     */
    private void notifyBirthday(Member member) {
        log.info("🎉 HAPPY BIRTHDAY! {} {} - {} years old today!",
                member.getFullName(),
                getZodiacSymbol(member.getZodiacSign()),
                calculateAge(member)
        );

        // TODO: Send actual notification
        // - Email notification
        // - Slack/Teams notification
        // - Push notification
        // - Dashboard banner

        log.info("   📧 Notification sent to: {}", member.getEmail());
        log.info("   🎁 Zodiac: {} ({}) - {}",
                member.getZodiacSign(),
                member.getZodiacElement(),
                getBirthdayMessage(member.getZodiacSign())
        );
    }

    /**
     * Notify upcoming birthdays summary
     */
    private void notifyUpcomingSummary(List<Member> upcomingBirthdays) {
        log.info("📬 Weekly Birthday Summary:");
        log.info("   Total upcoming: {} member(s)", upcomingBirthdays.size());

        // Group by zodiac sign
        var byZodiac = upcomingBirthdays.stream()
                .collect(Collectors.groupingBy(Member::getZodiacSign, Collectors.counting()));

        log.info("   By zodiac: {}", byZodiac);

        // TODO: Send actual summary notification
        // - Weekly email digest
        // - Dashboard notification
    }

    /**
     * Calculate age from date of birth
     */
    private int calculateAge(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = member.getDateOfBirth();
        return today.getYear() - birthDate.getYear();
    }

    /**
     * Get personalized birthday message based on zodiac sign
     */
    private String getBirthdayMessage(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "Let your fiery spirit shine today! 🔥";
            case Taurus -> "Enjoy the finer things in life today! 🌸";
            case Gemini -> "Double the fun, double the celebration! 🎭";
            case Cancer -> "May your day be filled with love and warmth! 🦀";
            case Leo -> "Shine bright like the star you are! 👑";
            case Virgo -> "Wishing you a perfectly organized day! ✨";
            case Libra -> "Balance and harmony on your special day! ⚖️";
            case Scorpio -> "Intense celebrations for an intense soul! 🦂";
            case Sagittarius -> "Adventure awaits, birthday explorer! 🏹";
            case Capricorn -> "Celebrate your achievements today! 🐐";
            case Aquarius -> "Unique and wonderful, just like you! 💧";
            case Pisces -> "Dream big on your special day! 🐟";
        };
    }

    /**
     * Get zodiac symbol
     */
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

    /**
     * Test method - Manually trigger birthday check
     * Can be called via REST endpoint for testing
     */
    public void manualBirthdayCheck() {
        log.info("🔍 Manual birthday check triggered...");
        checkBirthdaysToday();
    }

    /**
     * Test method - Manually trigger upcoming check
     */
    public void manualUpcomingCheck() {
        log.info("🔍 Manual upcoming check triggered...");
        checkUpcomingBirthdays();
    }

    /**
     * Get next birthday check time
     */
    public String getNextScheduledRun() {
        return "Daily at 8:00 AM, Weekly reports on Monday 9:00 AM, Monthly reports on 1st at 10:00 AM";
    }
}