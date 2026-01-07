package com.jci.zodiac.service;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacProfile;
import com.jci.zodiac.util.ZodiacCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ZodiacUtilityService - Business logic for Zodiac calculations
 * Provides high-level zodiac operations for the application
 */
@Service
@Slf4j
public class ZodiacUtilityService {

    // ==================== Basic Zodiac Calculations ====================

    /**
     * Calculate zodiac sign from date of birth
     */
    public Member.ZodiacSign calculateZodiacSign(LocalDate dateOfBirth) {
        log.debug("Calculating zodiac sign for date: {}", dateOfBirth);
        return ZodiacCalculator.calculateZodiacSign(dateOfBirth);
    }

    /**
     * Calculate zodiac element from date of birth
     */
    public Member.ZodiacElement calculateZodiacElement(LocalDate dateOfBirth) {
        log.debug("Calculating zodiac element for date: {}", dateOfBirth);
        return ZodiacCalculator.calculateZodiacElement(dateOfBirth);
    }

    /**
     * Get zodiac element from zodiac sign
     */
    public Member.ZodiacElement getElementFromSign(Member.ZodiacSign sign) {
        return ZodiacCalculator.calculateZodiacElement(sign);
    }

    /**
     * Get zodiac symbol
     */
    public String getZodiacSymbol(Member.ZodiacSign sign) {
        return ZodiacCalculator.getSymbol(sign);
    }

    /**
     * Get zodiac date range
     */
    public String getZodiacDateRange(Member.ZodiacSign sign) {
        return ZodiacCalculator.getDateRange(sign);
    }

    /**
     * Get modality for zodiac sign
     */
    public ZodiacProfile.Modality getModality(Member.ZodiacSign sign) {
        return ZodiacCalculator.getModality(sign);
    }

    // ==================== Member Code Generation ====================

    /**
     * Generate unique member code in format: JCI-DN-XXX
     * XXX is a sequential 3-digit number
     */
    public String generateMemberCode(long nextSequence) {
        return String.format("JCI-DN-%03d", nextSequence);
    }

    /**
     * Validate member code format
     */
    public boolean isValidMemberCode(String memberCode) {
        if (memberCode == null || memberCode.isEmpty()) {
            return false;
        }
        // Format: JCI-DN-XXX where XXX is 3 digits
        return memberCode.matches("^JCI-DN-\\d{3}$");
    }

    // ==================== Element Compatibility ====================

    /**
     * Check if two zodiac elements are compatible
     */
    public boolean areElementsCompatible(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        return ZodiacCalculator.areElementsCompatible(element1, element2);
    }

    /**
     * Get element harmony description
     */
    public String getElementHarmony(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        return ZodiacCalculator.getElementHarmony(element1, element2);
    }

    // ==================== Team Element Balance ====================

    /**
     * Calculate element balance for a list of members
     * Returns a map with element counts
     */
    public Map<Member.ZodiacElement, Long> calculateElementBalance(List<Member> members) {
        log.debug("Calculating element balance for {} members", members.size());

        Map<Member.ZodiacElement, Long> balance = members.stream()
                .collect(Collectors.groupingBy(
                        Member::getZodiacElement,
                        Collectors.counting()
                ));

        // Ensure all elements are present in map (even with 0 count)
        for (Member.ZodiacElement element : Member.ZodiacElement.values()) {
            balance.putIfAbsent(element, 0L);
        }

        return balance;
    }

    /**
     * Check if team has balanced elements
     * Balanced = all 4 elements present
     */
    public boolean isTeamBalanced(List<Member> members) {
        Map<Member.ZodiacElement, Long> balance = calculateElementBalance(members);
        return balance.values().stream().noneMatch(count -> count == 0);
    }

    /**
     * Get missing elements in a team
     */
    public List<Member.ZodiacElement> getMissingElements(List<Member> members) {
        Map<Member.ZodiacElement, Long> balance = calculateElementBalance(members);
        return balance.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // ==================== Zodiac Distribution ====================

    /**
     * Calculate zodiac sign distribution for a list of members
     */
    public Map<Member.ZodiacSign, Long> calculateZodiacDistribution(List<Member> members) {
        log.debug("Calculating zodiac distribution for {} members", members.size());

        Map<Member.ZodiacSign, Long> distribution = members.stream()
                .collect(Collectors.groupingBy(
                        Member::getZodiacSign,
                        Collectors.counting()
                ));

        // Ensure all signs are present (even with 0 count)
        for (Member.ZodiacSign sign : Member.ZodiacSign.values()) {
            distribution.putIfAbsent(sign, 0L);
        }

        return distribution;
    }

    /**
     * Find most common zodiac sign in a list
     */
    public Member.ZodiacSign getMostCommonZodiacSign(List<Member> members) {
        if (members == null || members.isEmpty()) {
            return null;
        }

        Map<Member.ZodiacSign, Long> distribution = calculateZodiacDistribution(members);

        return distribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Find least common zodiac sign in a list
     */
    public Member.ZodiacSign getLeastCommonZodiacSign(List<Member> members) {
        if (members == null || members.isEmpty()) {
            return null;
        }

        Map<Member.ZodiacSign, Long> distribution = calculateZodiacDistribution(members);

        return distribution.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // Exclude signs with 0 count
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // ==================== Birthday Calculations ====================

    /**
     * Check if today is someone's birthday
     */
    public boolean isBirthdayToday(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        return dateOfBirth.getMonth() == today.getMonth() &&
                dateOfBirth.getDayOfMonth() == today.getDayOfMonth();
    }

    /**
     * Get upcoming birthdays within next N days
     */
    public List<Member> getUpcomingBirthdays(List<Member> members, int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);

        return members.stream()
                .filter(member -> {
                    LocalDate birthday = LocalDate.of(
                            today.getYear(),
                            member.getDateOfBirth().getMonth(),
                            member.getDateOfBirth().getDayOfMonth()
                    );

                    // Handle birthdays that already passed this year
                    if (birthday.isBefore(today)) {
                        birthday = birthday.plusYears(1);
                    }

                    return !birthday.isBefore(today) && !birthday.isAfter(endDate);
                })
                .sorted(Comparator.comparing(member ->
                        LocalDate.of(
                                today.getYear(),
                                member.getDateOfBirth().getMonth(),
                                member.getDateOfBirth().getDayOfMonth()
                        )
                ))
                .collect(Collectors.toList());
    }

    // ==================== Validation Helpers ====================

    /**
     * Validate date of birth (must be in the past, reasonable age)
     */
    public boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }

        LocalDate now = LocalDate.now();

        // Must be in the past
        if (!dateOfBirth.isBefore(now)) {
            return false;
        }

        // Must be at least 18 years old (JCI requirement)
        LocalDate minDate = now.minusYears(18);
        if (dateOfBirth.isAfter(minDate)) {
            return false;
        }

        // Must not be older than 120 years (reasonable limit)
        LocalDate maxDate = now.minusYears(120);
        if (dateOfBirth.isBefore(maxDate)) {
            return false;
        }

        return true;
    }

    // ==================== Sagittarius Special Features ♐ ====================

    /**
     * Check if a member is Sagittarius (the best sign!)
     */
    public boolean isSagittarius(Member member) {
        return member.getZodiacSign() == Member.ZodiacSign.Sagittarius;
    }

    /**
     * Get all Sagittarius members from a list
     */
    public List<Member> getSagittariusMembers(List<Member> members) {
        return members.stream()
                .filter(this::isSagittarius)
                .collect(Collectors.toList());
    }

    /**
     * Count Sagittarius members
     */
    public long countSagittarius(List<Member> members) {
        return members.stream()
                .filter(this::isSagittarius)
                .count();
    }

    /**
     * Get Sagittarius percentage in team
     */
    public double getSagittariusPercentage(List<Member> members) {
        if (members == null || members.isEmpty()) {
            return 0.0;
        }
        long sagCount = countSagittarius(members);
        return (sagCount * 100.0) / members.size();
    }

    // ==================== Fun Stats ====================

    /**
     * Generate fun insight about team composition
     */
    public String generateTeamInsight(List<Member> members) {
        if (members == null || members.isEmpty()) {
            return "No members to analyze";
        }

        Map<Member.ZodiacElement, Long> elementBalance = calculateElementBalance(members);

        long fireCount = elementBalance.get(Member.ZodiacElement.Fire);
        long earthCount = elementBalance.get(Member.ZodiacElement.Earth);
        long airCount = elementBalance.get(Member.ZodiacElement.Air);
        long waterCount = elementBalance.get(Member.ZodiacElement.Water);

        // Generate insight based on dominant element
        if (fireCount > earthCount && fireCount > airCount && fireCount > waterCount) {
            return "🔥 Fire-dominant team! Expect high energy, enthusiasm, and bold initiatives.";
        } else if (earthCount > fireCount && earthCount > airCount && earthCount > waterCount) {
            return "🌍 Earth-strong team! Practical, reliable, and excellent at execution.";
        } else if (airCount > fireCount && airCount > earthCount && airCount > waterCount) {
            return "💨 Air-focused team! Great at communication, brainstorming, and innovation.";
        } else if (waterCount > fireCount && waterCount > earthCount && waterCount > airCount) {
            return "💧 Water-rich team! Emotionally intelligent, intuitive, and collaborative.";
        } else {
            return "⚖️ Balanced team! A harmonious mix of all elements. Great synergy potential!";
        }
    }
}