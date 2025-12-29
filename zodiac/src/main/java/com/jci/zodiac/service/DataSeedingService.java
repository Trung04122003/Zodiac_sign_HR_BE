package com.jci.zodiac.service;

import com.jci.zodiac.entity.*;
import com.jci.zodiac.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * DataSeedingService - Seeds initial data for the application
 * Implements CommandLineRunner to run on application startup
 *
 * FIXED: Removed references to non-existent enum methods and User.Role
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSeedingService implements CommandLineRunner {

    private final ZodiacProfileRepository zodiacProfileRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("🌱 Starting data seeding process...");

        try {
            seedZodiacProfiles();
            seedCompatibilityMatrix();
            seedInitialUser();

            // Optional: Uncomment to seed demo data
            // seedDemoData();

            log.info("✅ Data seeding completed successfully!");
        } catch (Exception e) {
            log.error("❌ Error during data seeding: {}", e.getMessage(), e);
        }
    }

    /**
     * Seed all 12 zodiac profiles with complete data
     */
    @Transactional
    public void seedZodiacProfiles() {
        if (zodiacProfileRepository.count() > 0) {
            log.info("ℹ️  Zodiac profiles already exist, skipping...");
            return;
        }

        log.info("🌟 Seeding 12 zodiac profiles...");

        List<ZodiacProfile> profiles = Arrays.asList(
                createZodiacProfile(ZodiacProfile.ZodiacSign.Aries, "♈", ZodiacProfile.Element.Fire, ZodiacProfile.Modality.Cardinal,
                        "The Ram", "Mars", "03-21", "04-19",
                        Arrays.asList("Courageous", "Determined", "Confident", "Enthusiastic", "Optimistic", "Honest", "Passionate"),
                        Arrays.asList("Impatient", "Moody", "Short-tempered", "Impulsive", "Aggressive"),
                        Arrays.asList("Direct", "Decisive", "Action-oriented", "Thrives in leadership roles"),
                        Arrays.asList("Team Leader", "Project Manager", "Entrepreneur", "Sales Executive"),
                        "Direct and straightforward. Appreciates honesty and quick decision-making.",
                        Arrays.asList("New challenges", "Competition", "Independence", "Recognition"),
                        Arrays.asList("Micromanagement", "Slow pace", "Bureaucracy", "Inaction")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Taurus, "♉", ZodiacProfile.Element.Earth, ZodiacProfile.Modality.Fixed,
                        "The Bull", "Venus", "04-20", "05-20",
                        Arrays.asList("Reliable", "Patient", "Practical", "Devoted", "Responsible", "Stable"),
                        Arrays.asList("Stubborn", "Possessive", "Uncompromising", "Resistant to change"),
                        Arrays.asList("Methodical", "Dependable", "Focused on quality", "Excels in stable projects"),
                        Arrays.asList("Quality Assurance", "Financial Management", "Operations", "Administration"),
                        "Calm and steady. Prefers face-to-face conversations and values loyalty.",
                        Arrays.asList("Stability", "Quality work", "Tangible results", "Security"),
                        Arrays.asList("Sudden changes", "Uncertainty", "Rushed decisions", "Instability")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Gemini, "♊", ZodiacProfile.Element.Air, ZodiacProfile.Modality.Mutable,
                        "The Twins", "Mercury", "05-21", "06-20",
                        Arrays.asList("Gentle", "Affectionate", "Curious", "Adaptable", "Quick-learner", "Witty"),
                        Arrays.asList("Nervous", "Inconsistent", "Indecisive", "Superficial"),
                        Arrays.asList("Versatile multitasker", "Excellent communication", "Thrives on variety"),
                        Arrays.asList("Communications", "Marketing", "Journalism", "Public Relations", "Training"),
                        "Articulate and engaging. Enjoys brainstorming and collaborative discussions.",
                        Arrays.asList("Variety", "Learning", "Social interaction", "Mental challenges"),
                        Arrays.asList("Monotony", "Isolation", "Rigid routines", "Boredom")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Cancer, "♋", ZodiacProfile.Element.Water, ZodiacProfile.Modality.Cardinal,
                        "The Crab", "Moon", "06-21", "07-22",
                        Arrays.asList("Tenacious", "Loyal", "Emotional", "Sympathetic", "Persuasive", "Intuitive"),
                        Arrays.asList("Moody", "Pessimistic", "Suspicious", "Manipulative", "Insecure"),
                        Arrays.asList("Empathetic team player", "Creates supportive environments", "Strong emotional intelligence"),
                        Arrays.asList("HR", "Customer Care", "Healthcare", "Education", "Social Work"),
                        "Sensitive and intuitive. Values emotional connection and creates safe spaces.",
                        Arrays.asList("Emotional connection", "Security", "Appreciation", "Team bonding"),
                        Arrays.asList("Criticism", "Conflict", "Lack of appreciation", "Emotional coldness")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Leo, "♌", ZodiacProfile.Element.Fire, ZodiacProfile.Modality.Fixed,
                        "The Lion", "Sun", "07-23", "08-22",
                        Arrays.asList("Creative", "Passionate", "Generous", "Warm-hearted", "Cheerful", "Humorous"),
                        Arrays.asList("Arrogant", "Stubborn", "Self-centered", "Lazy", "Inflexible"),
                        Arrays.asList("Charismatic natural leader", "Inspires teams", "Thrives in spotlight"),
                        Arrays.asList("Leadership", "Entertainment", "Public Speaking", "Creative Direction"),
                        "Warm and expressive. Commands attention naturally and motivates through enthusiasm.",
                        Arrays.asList("Recognition", "Appreciation", "Creative freedom", "Prestige"),
                        Arrays.asList("Being ignored", "Lack of recognition", "Criticism in public", "Routine tasks")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Virgo, "♍", ZodiacProfile.Element.Earth, ZodiacProfile.Modality.Mutable,
                        "The Virgin", "Mercury", "08-23", "09-22",
                        Arrays.asList("Loyal", "Analytical", "Kind", "Hardworking", "Practical", "Detail-oriented"),
                        Arrays.asList("Shyness", "Worry", "Overly critical", "Perfectionism", "Conservative"),
                        Arrays.asList("Precise problem-solver", "High standards", "Excels in quality control"),
                        Arrays.asList("Quality Assurance", "Data Analysis", "Research", "Process Improvement"),
                        "Clear and precise. Appreciates structure and thorough explanations.",
                        Arrays.asList("Organization", "Efficiency", "Accuracy", "Continuous improvement"),
                        Arrays.asList("Chaos", "Incompetence", "Wastefulness", "Lack of standards")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Libra, "♎", ZodiacProfile.Element.Air, ZodiacProfile.Modality.Cardinal,
                        "The Scales", "Venus", "09-23", "10-22",
                        Arrays.asList("Cooperative", "Diplomatic", "Gracious", "Fair-minded", "Social", "Balanced"),
                        Arrays.asList("Indecisive", "Avoids confrontation", "Holds grudges", "Self-pity"),
                        Arrays.asList("Excellent mediator", "Brings balance", "Thrives in collaboration"),
                        Arrays.asList("Mediation", "Customer Relations", "Partnership Management", "Design"),
                        "Diplomatic and tactful. Seeks consensus and values balanced perspectives.",
                        Arrays.asList("Harmony", "Fairness", "Partnership", "Beauty", "Balance"),
                        Arrays.asList("Injustice", "Conflict", "Rudeness", "Loudness", "Conformity")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Scorpio, "♏", ZodiacProfile.Element.Water, ZodiacProfile.Modality.Fixed,
                        "The Scorpion", "Pluto", "10-23", "11-21",
                        Arrays.asList("Resourceful", "Brave", "Passionate", "Stubborn", "Strategic", "Loyal"),
                        Arrays.asList("Distrusting", "Jealous", "Secretive", "Manipulative"),
                        Arrays.asList("Intense focus", "Investigative skills", "Excels at deep analysis"),
                        Arrays.asList("Research", "Investigation", "Strategy", "Crisis Management", "Psychology"),
                        "Intense and direct. Values honesty and can read between the lines.",
                        Arrays.asList("Truth", "Privacy", "Deep work", "Loyalty", "Transformation"),
                        Arrays.asList("Dishonesty", "Betrayal", "Superficiality", "Disloyalty")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Sagittarius, "♐", ZodiacProfile.Element.Fire, ZodiacProfile.Modality.Mutable,
                        "The Archer", "Jupiter", "11-22", "12-21",
                        Arrays.asList("Generous", "Idealistic", "Great sense of humor", "Optimistic", "Adventurous", "Honest"),
                        Arrays.asList("Promises more than can deliver", "Impatient", "Tactless", "Overconfident"),
                        Arrays.asList("Optimistic innovator", "Brings fresh perspectives", "Thrives on growth"),
                        Arrays.asList("Education", "Innovation", "Travel Industry", "Philosophy", "Entrepreneurship"),
                        "Straightforward and enthusiastic. Inspirational communicator who motivates with vision.",
                        Arrays.asList("Freedom", "Growth", "Learning", "Adventure", "Optimism"),
                        Arrays.asList("Constraints", "Micromanagement", "Negativity", "Routine", "Details")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Capricorn, "♑", ZodiacProfile.Element.Earth, ZodiacProfile.Modality.Cardinal,
                        "The Goat", "Saturn", "12-22", "01-19",
                        Arrays.asList("Responsible", "Disciplined", "Self-control", "Strategic", "Ambitious", "Professional"),
                        Arrays.asList("Know-it-all", "Unforgiving", "Condescending", "Pessimistic", "Workaholic"),
                        Arrays.asList("Strategic planner", "Strong work ethic", "Excels in management"),
                        Arrays.asList("Management", "Strategy", "Planning", "Finance", "Administration"),
                        "Professional and structured. Values hierarchy and clear expectations.",
                        Arrays.asList("Achievement", "Recognition", "Career advancement", "Structure"),
                        Arrays.asList("Lack of ambition", "Disorganization", "Unprofessionalism", "Failure")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Aquarius, "♒", ZodiacProfile.Element.Air, ZodiacProfile.Modality.Fixed,
                        "The Water Bearer", "Uranus", "01-20", "02-18",
                        Arrays.asList("Progressive", "Original", "Independent", "Humanitarian", "Innovative", "Visionary"),
                        Arrays.asList("Runs from emotional expression", "Temperamental", "Uncompromising", "Aloof"),
                        Arrays.asList("Forward-thinking innovator", "Unique perspectives", "Creative problem-solving"),
                        Arrays.asList("Innovation", "Technology", "Research", "Social Impact", "Future Planning"),
                        "Intellectual and unconventional. Values independence and original thinking.",
                        Arrays.asList("Independence", "Innovation", "Intellectual freedom", "Social causes"),
                        Arrays.asList("Limitations", "Conformity", "Emotional drama", "Boredom")),

                createZodiacProfile(ZodiacProfile.ZodiacSign.Pisces, "♓", ZodiacProfile.Element.Water, ZodiacProfile.Modality.Mutable,
                        "The Fish", "Neptune", "02-19", "03-20",
                        Arrays.asList("Compassionate", "Artistic", "Intuitive", "Gentle", "Wise", "Musical", "Empathetic"),
                        Arrays.asList("Fearful", "Overly trusting", "Sad", "Desire to escape reality", "Victim mentality"),
                        Arrays.asList("Creative empath", "Strong intuition", "Excels in caring professions"),
                        Arrays.asList("Creative Arts", "Healthcare", "Counseling", "Non-profit", "Spirituality"),
                        "Empathetic and gentle. Values emotional connection and creative expression.",
                        Arrays.asList("Empathy", "Creativity", "Spirituality", "Helping others", "Flexibility"),
                        Arrays.asList("Cruelty", "Criticism", "Harsh reality", "Conflict", "Pressure"))
        );

        zodiacProfileRepository.saveAll(profiles);
        log.info("✅ Seeded {} zodiac profiles", profiles.size());
    }

    /**
     * Seed compatibility matrix (12 x 12 = 144 pairs)
     */
    @Transactional
    public void seedCompatibilityMatrix() {
        if (compatibilityRepository.count() > 0) {
            log.info("ℹ️  Compatibility matrix already exists, skipping...");
            return;
        }

        log.info("💫 Seeding zodiac compatibility matrix...");

        List<ZodiacCompatibility> compatibilities = new ArrayList<>();
        ZodiacCompatibility.ZodiacSign[] signs = ZodiacCompatibility.ZodiacSign.values();

        // Compatibility scores matrix (simplified version)
        Map<String, Integer> compatibilityScores = getCompatibilityScores();

        for (ZodiacCompatibility.ZodiacSign sign1 : signs) {
            for (ZodiacCompatibility.ZodiacSign sign2 : signs) {
                String key = getCompatibilityKey(sign1, sign2);
                int overallScore = compatibilityScores.getOrDefault(key, 50);

                ZodiacCompatibility compat = ZodiacCompatibility.builder()
                        .zodiacSign1(sign1)
                        .zodiacSign2(sign2)
                        .overallScore(BigDecimal.valueOf(overallScore))
                        .workCompatibilityScore(BigDecimal.valueOf(calculateWorkScore(overallScore)))
                        .communicationScore(BigDecimal.valueOf(calculateCommunicationScore(overallScore)))
                        .conflictPotential(BigDecimal.valueOf(100 - overallScore))
                        .synergyScore(BigDecimal.valueOf(calculateSynergyScore(overallScore)))
                        .strengthsTogether(generateStrengths(sign1, sign2))
                        .challengesTogether(generateChallenges(sign1, sign2))
                        .managementTips(generateWorkTips(sign1, sign2))
                        .build();

                compatibilities.add(compat);
            }
        }

        compatibilityRepository.saveAll(compatibilities);
        log.info("✅ Seeded {} compatibility pairs", compatibilities.size());
    }

    /**
     * Seed initial admin user
     * FIXED: User entity doesn't have Role enum
     */
    @Transactional
    public void seedInitialUser() {
        if (userRepository.count() > 0) {
            log.info("ℹ️  Users already exist, skipping...");
            return;
        }

        log.info("👤 Seeding initial admin user...");

        User admin = User.builder()
                .username("admin")
                .email("admin@jcidanang.com")
                .password("$2a$10$dummyHashedPassword") // In production, use proper BCrypt
                .fullName("JCI Danang Admin")
                .dateOfBirth(LocalDate.of(1995, 12, 1)) // Sagittarius!
                .zodiacSign(User.ZodiacSign.Sagittarius)
                .organization("JCI Danang Junior Club")
                .position("Vice President - Membership & Training")
                .build();

        userRepository.save(admin);
        log.info("✅ Seeded admin user");
    }

    /**
     * Seed demo data (optional - call manually)
     */
    @Transactional
    public void seedDemoData() {
        log.info("🎭 Seeding demo data...");

        // Seed departments
        seedDemoDepartments();

        // Seed members
        seedDemoMembers();

        log.info("✅ Demo data seeded successfully");
    }

    // ==================== Helper Methods ====================

    private ZodiacProfile createZodiacProfile(
            ZodiacProfile.ZodiacSign sign, String symbol, ZodiacProfile.Element element,
            ZodiacProfile.Modality modality, String nickname, String rulingPlanet,
            String dateStart, String dateEnd,
            List<String> strengths, List<String> weaknesses, List<String> workStyle,
            List<String> bestRoles, String communicationStyle, List<String> motivation,
            List<String> stressTriggers) {

        return ZodiacProfile.builder()
                .zodiacSign(sign)
                .symbol(symbol)
                .element(element)
                .modality(modality)
                .rulingPlanet(rulingPlanet)
                .dateStart(dateStart)
                .dateEnd(dateEnd)
                .personalityTraits(strengths)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .workStyle(workStyle)
                .bestRoles(bestRoles)
                .communicationStyle(communicationStyle)
                .motivationFactors(motivation)
                .stressTriggers(stressTriggers)
                .descriptionLong(nickname)
                .build();
    }

    private Map<String, Integer> getCompatibilityScores() {
        Map<String, Integer> scores = new HashMap<>();
        // High compatibility pairs (same element or compatible elements)
        scores.put("Aries-Leo", 90);
        scores.put("Aries-Sagittarius", 88);
        scores.put("Taurus-Virgo", 92);
        scores.put("Taurus-Capricorn", 90);
        scores.put("Gemini-Libra", 88);
        scores.put("Gemini-Aquarius", 87);
        scores.put("Cancer-Scorpio", 91);
        scores.put("Cancer-Pisces", 89);
        scores.put("Leo-Aries", 90);
        scores.put("Leo-Sagittarius", 89);
        scores.put("Virgo-Taurus", 92);
        scores.put("Virgo-Capricorn", 88);
        scores.put("Libra-Gemini", 88);
        scores.put("Libra-Aquarius", 90);
        scores.put("Scorpio-Cancer", 91);
        scores.put("Scorpio-Pisces", 87);
        scores.put("Sagittarius-Aries", 88);
        scores.put("Sagittarius-Leo", 89);
        scores.put("Capricorn-Taurus", 90);
        scores.put("Capricorn-Virgo", 88);
        scores.put("Aquarius-Gemini", 87);
        scores.put("Aquarius-Libra", 90);
        scores.put("Pisces-Cancer", 89);
        scores.put("Pisces-Scorpio", 87);
        return scores;
    }

    private String getCompatibilityKey(ZodiacCompatibility.ZodiacSign sign1, ZodiacCompatibility.ZodiacSign sign2) {
        return sign1.name() + "-" + sign2.name();
    }

    private int calculateWorkScore(int overall) {
        return Math.min(100, overall + ThreadLocalRandom.current().nextInt(-5, 6));
    }

    private int calculateCommunicationScore(int overall) {
        return Math.min(100, overall + ThreadLocalRandom.current().nextInt(-5, 6));
    }

    private int calculateSynergyScore(int overall) {
        return Math.min(100, overall + ThreadLocalRandom.current().nextInt(-5, 6));
    }

    private String generateStrengths(ZodiacCompatibility.ZodiacSign sign1, ZodiacCompatibility.ZodiacSign sign2) {
        return String.format("%s and %s bring complementary skills and perspectives to the team", sign1, sign2);
    }

    private String generateChallenges(ZodiacCompatibility.ZodiacSign sign1, ZodiacCompatibility.ZodiacSign sign2) {
        return "Different communication styles may require adjustment and mutual understanding";
    }

    private String generateWorkTips(ZodiacCompatibility.ZodiacSign sign1, ZodiacCompatibility.ZodiacSign sign2) {
        return "Establish clear communication channels early and respect each other's working styles";
    }

    private void seedDemoDepartments() {
        if (departmentRepository.count() > 0) return;

        List<Department> departments = Arrays.asList(
                Department.builder()
                        .name("Membership & Growth")
                        .code("JCI-DN-MG")
                        .zodiacTheme(Department.ZodiacSign.Sagittarius)
                        .isActive(true)
                        .build(),
                Department.builder()
                        .name("Community Impact")
                        .code("JCI-DN-CI")
                        .zodiacTheme(Department.ZodiacSign.Cancer)
                        .isActive(true)
                        .build()
        );

        departmentRepository.saveAll(departments);
        log.info("✅ Seeded {} demo departments", departments.size());
    }

    private void seedDemoMembers() {
        if (memberRepository.count() > 0) return;

        // Add sample members with various zodiac signs
        List<Member> members = Arrays.asList(
                createDemoMember("Lê Thái Trung", LocalDate.of(2003, 12, 4), Member.ZodiacSign.Sagittarius),
                createDemoMember("Dũ Ngọc Mỹ Dung", LocalDate.of(2006, 11, 15), Member.ZodiacSign.Scorpio),
                createDemoMember("Nguyễn Gia Triều", LocalDate.of(2005, 11, 2), Member.ZodiacSign.Scorpio)
        );

        memberRepository.saveAll(members);
        log.info("✅ Seeded {} demo members", members.size());
    }

    private Member createDemoMember(String name, LocalDate dob, Member.ZodiacSign zodiacSign) {
        return Member.builder()
                .memberCode("JCI-DN-" + String.format("%03d", ThreadLocalRandom.current().nextInt(1, 100)))
                .fullName(name)
                .email(name.toLowerCase().replace(" ", ".") + "@example.com")
                .dateOfBirth(dob)
                .zodiacSign(zodiacSign)
                .zodiacElement(getElementForSign(zodiacSign))
                .phone("+84" + ThreadLocalRandom.current().nextInt(100000000, 999999999))
                .joinDate(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(1, 24)))
                .membershipStatus(Member.MembershipStatus.Active)
                .build();
    }

    private Member.ZodiacElement getElementForSign(Member.ZodiacSign sign) {
        return switch (sign) {
            case Aries, Leo, Sagittarius -> Member.ZodiacElement.Fire;
            case Taurus, Virgo, Capricorn -> Member.ZodiacElement.Earth;
            case Gemini, Libra, Aquarius -> Member.ZodiacElement.Air;
            case Cancer, Scorpio, Pisces -> Member.ZodiacElement.Water;
        };
    }
}