package com.jci.zodiac.service;

import com.jci.zodiac.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * DailyZodiacInsightsService - Provides daily zodiac tips and insights
 * Rotates insights daily and provides random zodiac wisdom
 *
 * FIXED: Removed getSymbol() and getElement() calls - use helper methods
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyZodiacInsightsService {

    private String currentDailyInsight;
    private Member.ZodiacSign currentFeaturedSign;
    private LocalDate lastUpdateDate;

    // Zodiac insights database
    private static final Map<Member.ZodiacSign, List<String>> ZODIAC_INSIGHTS = new HashMap<>();

    static {
        // ♈ Aries insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Aries, Arrays.asList(
                "Aries team members thrive when given leadership opportunities. Trust their initiative! 🔥",
                "Aries excel in fast-paced environments. Assign them time-sensitive projects for best results.",
                "When working with Aries, be direct and concise. They appreciate straightforward communication.",
                "Aries' competitive nature can drive team performance. Channel it into healthy challenges!",
                "Aries bring enthusiasm to every project. Their energy is contagious—use it to motivate teams!"
        ));

        // ♉ Taurus insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Taurus, Arrays.asList(
                "Taurus team members value stability. Provide clear expectations for optimal performance. 🌱",
                "Taurus excel at long-term projects requiring patience and dedication.",
                "When managing Taurus, respect their need for routine and gradual change.",
                "Taurus are reliable anchors in teams. Count on them for consistency!",
                "Taurus appreciate tangible rewards. Recognition with concrete benefits works best."
        ));

        // ♊ Gemini insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Gemini, Arrays.asList(
                "Gemini thrive on variety! Rotate their tasks to keep them engaged and creative. 🎭",
                "Gemini are excellent communicators. Leverage them for client-facing roles.",
                "When working with Gemini, embrace their multitasking abilities—don't box them in!",
                "Gemini bring fresh perspectives. Invite them to brainstorming sessions!",
                "Gemini adapt quickly to change. They're your go-to for pivots and new initiatives."
        ));

        // ♋ Cancer insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Cancer, Arrays.asList(
                "Cancer team members create supportive environments. They're natural team nurturers! 🦀",
                "Cancer excel when they feel emotionally connected to their work. Share the 'why' behind projects.",
                "When managing Cancer, provide emotional support alongside professional guidance.",
                "Cancer have strong intuition. Trust their gut feelings about team dynamics!",
                "Cancer are loyal team players. Invest in them and they'll invest in the organization."
        ));

        // ♌ Leo insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Leo, Arrays.asList(
                "Leo shine when given recognition. Public acknowledgment motivates them greatly! 👑",
                "Leo have natural charisma. Put them in roles where they can inspire others.",
                "When working with Leo, appreciate their contributions openly and genuinely.",
                "Leo bring confidence to teams. Their self-assurance can boost team morale!",
                "Leo excel at presentations and public speaking. Leverage this strength!"
        ));

        // ♍ Virgo insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Virgo, Arrays.asList(
                "Virgo excel at detail-oriented tasks. Trust them with quality control! ✨",
                "Virgo bring analytical precision to teams. Consult them for process improvements.",
                "When managing Virgo, provide clear standards—they appreciate well-defined excellence.",
                "Virgo are natural problem-solvers. Present them with complex challenges!",
                "Virgo value efficiency. Listen to their suggestions for workflow optimization."
        ));

        // ♎ Libra insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Libra, Arrays.asList(
                "Libra excel at mediating conflicts. They bring balance to team dynamics! ⚖️",
                "Libra thrive in collaborative environments. Foster teamwork for their best performance.",
                "When working with Libra, encourage their diplomatic approach to challenges.",
                "Libra have strong aesthetic sense. Involve them in design and presentation decisions.",
                "Libra seek harmony. They're invaluable in maintaining positive team culture!"
        ));

        // ♏ Scorpio insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Scorpio, Arrays.asList(
                "Scorpio bring intensity and focus. Assign them deep-dive research projects! 🦂",
                "Scorpio are naturally investigative. They excel at uncovering hidden insights.",
                "When managing Scorpio, respect their need for privacy and trust.",
                "Scorpio are fiercely loyal. Earn their trust and they'll be committed allies!",
                "Scorpio have strong intuition about people. Value their assessments."
        ));

        // ♐ Sagittarius insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Sagittarius, Arrays.asList(
                "Sagittarius love exploration! Give them opportunities to innovate and experiment. 🏹",
                "Sagittarius bring optimism to challenges. Their positive outlook uplifts teams!",
                "When working with Sagittarius, allow freedom and autonomy—micromanagement stifles them.",
                "Sagittarius are natural teachers. Let them mentor and share knowledge!",
                "Sagittarius thrive on growth. Provide learning opportunities for engagement!"
        ));

        // ♑ Capricorn insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Capricorn, Arrays.asList(
                "Capricorn are goal-oriented achievers. Set clear targets for optimal performance! 🐐",
                "Capricorn excel at strategic planning. Involve them in long-term initiatives.",
                "When managing Capricorn, recognize their hard work and advancement potential.",
                "Capricorn bring discipline to teams. They set strong examples of professionalism!",
                "Capricorn value structure. Provide clear hierarchies and career paths."
        ));

        // ♒ Aquarius insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Aquarius, Arrays.asList(
                "Aquarius think outside the box! Encourage their innovative ideas. 💧",
                "Aquarius excel at seeing future trends. Consult them for forward-thinking strategies.",
                "When working with Aquarius, embrace their unconventional approaches.",
                "Aquarius value independence. Give them autonomy to explore creative solutions!",
                "Aquarius are humanitarian. Connect projects to larger social impact for engagement."
        ));

        // ♓ Pisces insights
        ZODIAC_INSIGHTS.put(Member.ZodiacSign.Pisces, Arrays.asList(
                "Pisces bring creativity and empathy to teams. They excel in caring professions! 🐟",
                "Pisces have strong intuition. Trust their feelings about team dynamics.",
                "When managing Pisces, create a supportive atmosphere—they're sensitive to energy.",
                "Pisces are naturally artistic. Leverage their creative talents in projects!",
                "Pisces adapt like water. They're flexible team members in changing situations."
        ));
    }

    /**
     * Rotate daily insight
     * Runs every day at midnight (00:00)
     * Cron: "0 0 0 * * ?" = At 00:00:00 every day
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void rotateDailyInsight() {
        log.info("🌟 Rotating daily zodiac insight...");

        try {
            // Select random zodiac sign
            Member.ZodiacSign[] signs = Member.ZodiacSign.values();
            currentFeaturedSign = signs[ThreadLocalRandom.current().nextInt(signs.length)];

            // Select random insight for that sign
            List<String> insights = ZODIAC_INSIGHTS.get(currentFeaturedSign);
            currentDailyInsight = insights.get(ThreadLocalRandom.current().nextInt(insights.size()));

            lastUpdateDate = LocalDate.now();

            log.info("✨ Today's Featured Sign: {} {}", currentFeaturedSign, getZodiacSymbol(currentFeaturedSign));
            log.info("💡 Daily Insight: {}", currentDailyInsight);

        } catch (Exception e) {
            log.error("❌ Error rotating daily insight: {}", e.getMessage(), e);
        }
    }

    /**
     * Get today's daily insight
     */
    public DailyInsightResponse getDailyInsight() {
        // Initialize if not yet set
        if (currentDailyInsight == null || lastUpdateDate == null || !lastUpdateDate.equals(LocalDate.now())) {
            rotateDailyInsight();
        }

        return DailyInsightResponse.builder()
                .date(LocalDate.now())
                .featuredSign(currentFeaturedSign)
                .signSymbol(getZodiacSymbol(currentFeaturedSign))
                .insight(currentDailyInsight)
                .category("Work & Team Dynamics")
                .build();
    }

    /**
     * Get random insight for any zodiac sign
     */
    public String getRandomInsightForSign(Member.ZodiacSign sign) {
        List<String> insights = ZODIAC_INSIGHTS.get(sign);
        return insights.get(ThreadLocalRandom.current().nextInt(insights.size()));
    }

    /**
     * Get all insights for a specific sign
     */
    public List<String> getAllInsightsForSign(Member.ZodiacSign sign) {
        return new ArrayList<>(ZODIAC_INSIGHTS.get(sign));
    }

    /**
     * Get insight of the day for a specific user
     * Personalized based on their zodiac sign
     */
    public PersonalizedInsightResponse getPersonalizedInsight(Member.ZodiacSign userSign) {
        String personalInsight = getRandomInsightForSign(userSign);
        String generalInsight = currentDailyInsight != null ? currentDailyInsight : getRandomInsight();

        return PersonalizedInsightResponse.builder()
                .date(LocalDate.now())
                .yourSign(userSign)
                .yourSignSymbol(getZodiacSymbol(userSign))
                .personalInsight(personalInsight)
                .featuredSign(currentFeaturedSign)
                .featuredSignSymbol(currentFeaturedSign != null ? getZodiacSymbol(currentFeaturedSign) : "✨")
                .generalInsight(generalInsight)
                .build();
    }

    /**
     * Get random insight from any sign
     */
    public String getRandomInsight() {
        Member.ZodiacSign[] signs = Member.ZodiacSign.values();
        Member.ZodiacSign randomSign = signs[ThreadLocalRandom.current().nextInt(signs.length)];
        return getRandomInsightForSign(randomSign);
    }

    /**
     * Get compatibility tip between two signs
     */
    public String getCompatibilityTip(Member.ZodiacSign sign1, Member.ZodiacSign sign2) {
        // Get element compatibility
        String element1 = getElementForSign(sign1);
        String element2 = getElementForSign(sign2);

        if (element1.equals(element2)) {
            return String.format("%s and %s share the same element! They naturally understand each other's approach to work.",
                    sign1, sign2);
        }

        return switch (element1 + "-" + element2) {
            case "Fire-Air", "Air-Fire" -> "Fire and Air complement each other! One brings passion, the other brings ideas.";
            case "Earth-Water", "Water-Earth" -> "Earth and Water work harmoniously! Stability meets emotional intelligence.";
            case "Fire-Water", "Water-Fire" -> "Fire and Water can clash but create powerful steam when balanced!";
            case "Earth-Air", "Air-Earth" -> "Earth grounds Air's ideas into reality. A practical partnership!";
            default -> String.format("%s %s and %s %s bring diverse perspectives to the team!",
                    sign1, getZodiacSymbol(sign1), sign2, getZodiacSymbol(sign2));
        };
    }

    /**
     * Manual refresh for testing
     */
    public DailyInsightResponse refreshInsight() {
        log.info("🔄 Manually refreshing daily insight...");
        rotateDailyInsight();
        return getDailyInsight();
    }

    // ==================== Helper Methods ====================

    /**
     * Get zodiac symbol for a sign
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
     * Get element for a zodiac sign
     */
    private String getElementForSign(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Leo, Sagittarius -> "Fire";
            case Taurus, Virgo, Capricorn -> "Earth";
            case Gemini, Libra, Aquarius -> "Air";
            case Cancer, Scorpio, Pisces -> "Water";
        };
    }

    // Response DTOs
    @lombok.Data
    @lombok.Builder
    public static class DailyInsightResponse {
        private LocalDate date;
        private Member.ZodiacSign featuredSign;
        private String signSymbol;
        private String insight;
        private String category;
    }

    @lombok.Data
    @lombok.Builder
    public static class PersonalizedInsightResponse {
        private LocalDate date;
        private Member.ZodiacSign yourSign;
        private String yourSignSymbol;
        private String personalInsight;
        private Member.ZodiacSign featuredSign;
        private String featuredSignSymbol;
        private String generalInsight;
    }
}