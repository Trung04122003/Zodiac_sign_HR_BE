package com.jci.zodiac.util;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * ZodiacCalculator - Utility class for zodiac sign calculations
 * Calculates zodiac sign and element based on date of birth
 */
@Component
public class ZodiacCalculator {

    /**
     * Calculate zodiac sign from date of birth
     * @param dateOfBirth Date of birth
     * @return Zodiac sign enum
     */
    public static Member.ZodiacSign calculateZodiacSign(LocalDate dateOfBirth) {
        int month = dateOfBirth.getMonthValue();
        int day = dateOfBirth.getDayOfMonth();

        return switch (month) {
            case 1 -> day <= 19 ? Member.ZodiacSign.Capricorn : Member.ZodiacSign.Aquarius;
            case 2 -> day <= 18 ? Member.ZodiacSign.Aquarius : Member.ZodiacSign.Pisces;
            case 3 -> day <= 20 ? Member.ZodiacSign.Pisces : Member.ZodiacSign.Aries;
            case 4 -> day <= 19 ? Member.ZodiacSign.Aries : Member.ZodiacSign.Taurus;
            case 5 -> day <= 20 ? Member.ZodiacSign.Taurus : Member.ZodiacSign.Gemini;
            case 6 -> day <= 21 ? Member.ZodiacSign.Gemini : Member.ZodiacSign.Cancer;
            case 7 -> day <= 22 ? Member.ZodiacSign.Cancer : Member.ZodiacSign.Leo;
            case 8 -> day <= 22 ? Member.ZodiacSign.Leo : Member.ZodiacSign.Virgo;
            case 9 -> day <= 22 ? Member.ZodiacSign.Virgo : Member.ZodiacSign.Libra;
            case 10 -> day <= 23 ? Member.ZodiacSign.Libra : Member.ZodiacSign.Scorpio;
            case 11 -> day <= 21 ? Member.ZodiacSign.Scorpio : Member.ZodiacSign.Sagittarius;
            case 12 -> day <= 21 ? Member.ZodiacSign.Sagittarius : Member.ZodiacSign.Capricorn;
            default -> throw new IllegalArgumentException("Invalid date of birth");
        };
    }

    /**
     * Calculate zodiac element from date of birth
     * @param dateOfBirth Date of birth
     * @return Zodiac element
     */
    public static Member.ZodiacElement calculateZodiacElement(LocalDate dateOfBirth) {
        Member.ZodiacSign sign = calculateZodiacSign(dateOfBirth);
        return calculateZodiacElement(sign);
    }

    /**
     * Calculate zodiac element from zodiac sign
     * @param zodiacSign Zodiac sign
     * @return Zodiac element (Fire, Earth, Air, Water)
     */
    public static Member.ZodiacElement calculateZodiacElement(Member.ZodiacSign zodiacSign) {
        return switch (zodiacSign) {
            case Aries, Leo, Sagittarius -> Member.ZodiacElement.Fire;
            case Taurus, Virgo, Capricorn -> Member.ZodiacElement.Earth;
            case Gemini, Libra, Aquarius -> Member.ZodiacElement.Air;
            case Cancer, Scorpio, Pisces -> Member.ZodiacElement.Water;
        };
    }

    /**
     * Get zodiac symbol (Unicode character)
     * @param sign Zodiac sign
     * @return Unicode symbol string
     */
    public static String getSymbol(Member.ZodiacSign sign) {
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
     * Get element symbol
     * @param element Zodiac element
     * @return Element symbol
     */
    public static String getElementSymbol(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "🔥";
            case Earth -> "🌍";
            case Air -> "💨";
            case Water -> "💧";
        };
    }

    /**
     * Get date range for a zodiac sign
     * @param sign Zodiac sign
     * @return Date range string (e.g., "Mar 21 - Apr 19")
     */
    public static String getDateRange(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries -> "Mar 21 - Apr 19";
            case Taurus -> "Apr 20 - May 20";
            case Gemini -> "May 21 - Jun 21";
            case Cancer -> "Jun 22 - Jul 22";
            case Leo -> "Jul 23 - Aug 22";
            case Virgo -> "Aug 23 - Sep 22";
            case Libra -> "Sep 23 - Oct 23";
            case Scorpio -> "Oct 24 - Nov 21";
            case Sagittarius -> "Nov 22 - Dec 21";
            case Capricorn -> "Dec 22 - Jan 19";
            case Aquarius -> "Jan 20 - Feb 18";
            case Pisces -> "Feb 19 - Mar 20";
        };
    }

    /**
     * Get modality (Cardinal, Fixed, Mutable) for a zodiac sign
     * @param sign Zodiac sign
     * @return Modality
     */
    public static ZodiacProfile.Modality getModality(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Cancer, Libra, Capricorn -> ZodiacProfile.Modality.Cardinal;
            case Taurus, Leo, Scorpio, Aquarius -> ZodiacProfile.Modality.Fixed;
            case Gemini, Virgo, Sagittarius, Pisces -> ZodiacProfile.Modality.Mutable;
        };
    }

    /**
     * Check if two zodiac elements are compatible
     * @param element1 First element
     * @param element2 Second element
     * @return True if compatible
     */
    public static boolean areElementsCompatible(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        // Same element is always compatible
        if (element1 == element2) return true;

        // Fire and Air are compatible
        if ((element1 == Member.ZodiacElement.Fire && element2 == Member.ZodiacElement.Air) ||
                (element1 == Member.ZodiacElement.Air && element2 == Member.ZodiacElement.Fire)) {
            return true;
        }

        // Earth and Water are compatible
        if ((element1 == Member.ZodiacElement.Earth && element2 == Member.ZodiacElement.Water) ||
                (element1 == Member.ZodiacElement.Water && element2 == Member.ZodiacElement.Earth)) {
            return true;
        }

        return false;
    }

    /**
     * Get element harmony description
     * @param element1 First element
     * @param element2 Second element
     * @return Harmony description
     */
    public static String getElementHarmony(Member.ZodiacElement element1, Member.ZodiacElement element2) {
        if (element1 == element2) {
            return "Perfect harmony - same element";
        }

        if (areElementsCompatible(element1, element2)) {
            return "Harmonious - complementary elements";
        }

        // Fire vs Water or Earth vs Air
        if ((element1 == Member.ZodiacElement.Fire && element2 == Member.ZodiacElement.Water) ||
                (element1 == Member.ZodiacElement.Water && element2 == Member.ZodiacElement.Fire)) {
            return "Challenging - opposing elements (Fire vs Water)";
        }

        if ((element1 == Member.ZodiacElement.Earth && element2 == Member.ZodiacElement.Air) ||
                (element1 == Member.ZodiacElement.Air && element2 == Member.ZodiacElement.Earth)) {
            return "Challenging - opposing elements (Earth vs Air)";
        }

        return "Neutral";
    }

    /**
     * Check if two zodiac signs are compatible (same element)
     * @param sign1 First zodiac sign
     * @param sign2 Second zodiac sign
     * @return True if compatible element
     */
    public static boolean areElementCompatible(Member.ZodiacSign sign1, Member.ZodiacSign sign2) {
        Member.ZodiacElement element1 = calculateZodiacElement(sign1);
        Member.ZodiacElement element2 = calculateZodiacElement(sign2);
        return areElementsCompatible(element1, element2);
    }
}