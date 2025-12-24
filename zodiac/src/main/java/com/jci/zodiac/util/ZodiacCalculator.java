package com.jci.zodiac.util;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacProfile;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * ZodiacCalculator - Core utility for Zodiac calculations
 * Calculates zodiac sign and element from date of birth
 */
public class ZodiacCalculator {

    // ==================== Zodiac Date Ranges ====================

    private static final Map<Member.ZodiacSign, DateRange> ZODIAC_DATE_RANGES = new HashMap<>();

    static {
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Aries, new DateRange(Month.MARCH, 21, Month.APRIL, 19));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Taurus, new DateRange(Month.APRIL, 20, Month.MAY, 20));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Gemini, new DateRange(Month.MAY, 21, Month.JUNE, 20));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Cancer, new DateRange(Month.JUNE, 21, Month.JULY, 22));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Leo, new DateRange(Month.JULY, 23, Month.AUGUST, 22));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Virgo, new DateRange(Month.AUGUST, 23, Month.SEPTEMBER, 22));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Libra, new DateRange(Month.SEPTEMBER, 23, Month.OCTOBER, 22));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Scorpio, new DateRange(Month.OCTOBER, 23, Month.NOVEMBER, 21));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Sagittarius, new DateRange(Month.NOVEMBER, 22, Month.DECEMBER, 21));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Capricorn, new DateRange(Month.DECEMBER, 22, Month.JANUARY, 19));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Aquarius, new DateRange(Month.JANUARY, 20, Month.FEBRUARY, 18));
        ZODIAC_DATE_RANGES.put(Member.ZodiacSign.Pisces, new DateRange(Month.FEBRUARY, 19, Month.MARCH, 20));
    }

    // ==================== Element Mapping ====================

    private static final Map<Member.ZodiacSign, Member.ZodiacElement> SIGN_TO_ELEMENT = Map.ofEntries(
            Map.entry(Member.ZodiacSign.Aries, Member.ZodiacElement.Fire),
            Map.entry(Member.ZodiacSign.Leo, Member.ZodiacElement.Fire),
            Map.entry(Member.ZodiacSign.Sagittarius, Member.ZodiacElement.Fire),

            Map.entry(Member.ZodiacSign.Taurus, Member.ZodiacElement.Earth),
            Map.entry(Member.ZodiacSign.Virgo, Member.ZodiacElement.Earth),
            Map.entry(Member.ZodiacSign.Capricorn, Member.ZodiacElement.Earth),

            Map.entry(Member.ZodiacSign.Gemini, Member.ZodiacElement.Air),
            Map.entry(Member.ZodiacSign.Libra, Member.ZodiacElement.Air),
            Map.entry(Member.ZodiacSign.Aquarius, Member.ZodiacElement.Air),

            Map.entry(Member.ZodiacSign.Cancer, Member.ZodiacElement.Water),
            Map.entry(Member.ZodiacSign.Scorpio, Member.ZodiacElement.Water),
            Map.entry(Member.ZodiacSign.Pisces, Member.ZodiacElement.Water)
    );

    // ==================== Modality Mapping ====================

    private static final Map<Member.ZodiacSign, ZodiacProfile.Modality> SIGN_TO_MODALITY = Map.ofEntries(
            Map.entry(Member.ZodiacSign.Aries, ZodiacProfile.Modality.Cardinal),
            Map.entry(Member.ZodiacSign.Cancer, ZodiacProfile.Modality.Cardinal),
            Map.entry(Member.ZodiacSign.Libra, ZodiacProfile.Modality.Cardinal),
            Map.entry(Member.ZodiacSign.Capricorn, ZodiacProfile.Modality.Cardinal),

            Map.entry(Member.ZodiacSign.Taurus, ZodiacProfile.Modality.Fixed),
            Map.entry(Member.ZodiacSign.Leo, ZodiacProfile.Modality.Fixed),
            Map.entry(Member.ZodiacSign.Scorpio, ZodiacProfile.Modality.Fixed),
            Map.entry(Member.ZodiacSign.Aquarius, ZodiacProfile.Modality.Fixed),

            Map.entry(Member.ZodiacSign.Gemini, ZodiacProfile.Modality.Mutable),
            Map.entry(Member.ZodiacSign.Virgo, ZodiacProfile.Modality.Mutable),
            Map.entry(Member.ZodiacSign.Sagittarius, ZodiacProfile.Modality.Mutable),
            Map.entry(Member.ZodiacSign.Pisces, ZodiacProfile.Modality.Mutable)
    );

    // ==================== Symbol Mapping ====================

    private static final Map<Member.ZodiacSign, String> SIGN_TO_SYMBOL = Map.ofEntries(
            Map.entry(Member.ZodiacSign.Aries, "♈"),
            Map.entry(Member.ZodiacSign.Taurus, "♉"),
            Map.entry(Member.ZodiacSign.Gemini, "♊"),
            Map.entry(Member.ZodiacSign.Cancer, "♋"),
            Map.entry(Member.ZodiacSign.Leo, "♌"),
            Map.entry(Member.ZodiacSign.Virgo, "♍"),
            Map.entry(Member.ZodiacSign.Libra, "♎"),
            Map.entry(Member.ZodiacSign.Scorpio, "♏"),
            Map.entry(Member.ZodiacSign.Sagittarius, "♐"),
            Map.entry(Member.ZodiacSign.Capricorn, "♑"),
            Map.entry(Member.ZodiacSign.Aquarius, "♒"),
            Map.entry(Member.ZodiacSign.Pisces, "♓")
    );

    // ==================== Main Calculation Methods ====================

    /**
     * Calculate zodiac sign from date of birth
     */
    public static Member.ZodiacSign calculateZodiacSign(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        Month month = dateOfBirth.getMonth();
        int day = dateOfBirth.getDayOfMonth();

        for (Map.Entry<Member.ZodiacSign, DateRange> entry : ZODIAC_DATE_RANGES.entrySet()) {
            if (entry.getValue().contains(month, day)) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Unable to determine zodiac sign for date: " + dateOfBirth);
    }

    /**
     * Calculate zodiac element from zodiac sign
     */
    public static Member.ZodiacElement calculateZodiacElement(Member.ZodiacSign sign) {
        if (sign == null) {
            throw new IllegalArgumentException("Zodiac sign cannot be null");
        }
        return SIGN_TO_ELEMENT.get(sign);
    }

    /**
     * Calculate zodiac element directly from date of birth
     */
    public static Member.ZodiacElement calculateZodiacElement(LocalDate dateOfBirth) {
        Member.ZodiacSign sign = calculateZodiacSign(dateOfBirth);
        return calculateZodiacElement(sign);
    }

    /**
     * Get modality for zodiac sign
     */
    public static ZodiacProfile.Modality getModality(Member.ZodiacSign sign) {
        if (sign == null) {
            throw new IllegalArgumentException("Zodiac sign cannot be null");
        }
        return SIGN_TO_MODALITY.get(sign);
    }

    /**
     * Get symbol for zodiac sign
     */
    public static String getSymbol(Member.ZodiacSign sign) {
        if (sign == null) {
            throw new IllegalArgumentException("Zodiac sign cannot be null");
        }
        return SIGN_TO_SYMBOL.get(sign);
    }

    /**
     * Get date range for zodiac sign (formatted string)
     */
    public static String getDateRange(Member.ZodiacSign sign) {
        if (sign == null) {
            throw new IllegalArgumentException("Zodiac sign cannot be null");
        }
        DateRange range = ZODIAC_DATE_RANGES.get(sign);
        return range.toString();
    }

    // ==================== Compatibility Helper Methods ====================

    /**
     * Check if two elements are compatible
     * Fire ↔ Air: Harmonious
     * Earth ↔ Water: Harmonious
     * Same element: Harmonious
     */
    public static boolean areElementsCompatible(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        if (element1 == element2) return true;

        if ((element1 == Member.ZodiacElement.Fire && element2 == Member.ZodiacElement.Air) ||
                (element1 == Member.ZodiacElement.Air && element2 == Member.ZodiacElement.Fire)) {
            return true;
        }

        if ((element1 == Member.ZodiacElement.Earth && element2 == Member.ZodiacElement.Water) ||
                (element1 == Member.ZodiacElement.Water && element2 == Member.ZodiacElement.Earth)) {
            return true;
        }

        return false;
    }

    /**
     * Get element harmony level
     */
    public static String getElementHarmony(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        if (element1 == element2) return "Harmonious";
        if (areElementsCompatible(element1, element2)) return "Harmonious";

        // Fire ↔ Water or Earth ↔ Air = Challenging
        if ((element1 == Member.ZodiacElement.Fire && element2 == Member.ZodiacElement.Water) ||
                (element1 == Member.ZodiacElement.Water && element2 == Member.ZodiacElement.Fire) ||
                (element1 == Member.ZodiacElement.Earth && element2 == Member.ZodiacElement.Air) ||
                (element1 == Member.ZodiacElement.Air && element2 == Member.ZodiacElement.Earth)) {
            return "Challenging";
        }

        return "Neutral";
    }

    // ==================== Inner Class: DateRange ====================

    private static class DateRange {
        private final Month startMonth;
        private final int startDay;
        private final Month endMonth;
        private final int endDay;

        public DateRange(Month startMonth, int startDay, Month endMonth, int endDay) {
            this.startMonth = startMonth;
            this.startDay = startDay;
            this.endMonth = endMonth;
            this.endDay = endDay;
        }

        public boolean contains(Month month, int day) {
            // Handle ranges that span year boundary (e.g., Capricorn: Dec 22 - Jan 19)
            if (startMonth.getValue() > endMonth.getValue()) {
                return (month == startMonth && day >= startDay) ||
                        (month == endMonth && day <= endDay) ||
                        (month.getValue() > startMonth.getValue() || month.getValue() < endMonth.getValue());
            }

            // Normal range within same year
            if (month == startMonth && month == endMonth) {
                return day >= startDay && day <= endDay;
            }
            if (month == startMonth) {
                return day >= startDay;
            }
            if (month == endMonth) {
                return day <= endDay;
            }
            return month.getValue() > startMonth.getValue() && month.getValue() < endMonth.getValue();
        }

        @Override
        public String toString() {
            return String.format("%s %d - %s %d",
                    startMonth.toString().substring(0, 3),
                    startDay,
                    endMonth.toString().substring(0, 3),
                    endDay
            );
        }
    }
}