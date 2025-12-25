package com.jci.zodiac.service;

import com.jci.zodiac.entity.ZodiacProfile;
import com.jci.zodiac.repository.ZodiacProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ZodiacSeedService - Seed initial zodiac profiles data
 * Auto-runs on application startup if database is empty
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Order(1) // Run first before CompatibilitySeedService
public class ZodiacSeedService implements CommandLineRunner {

    private final ZodiacProfileRepository zodiacProfileRepository;

    @Override
    public void run(String... args) {
        if (zodiacProfileRepository.count() == 0) {
            log.info("🌟 Seeding Zodiac Profiles database...");
            seedZodiacProfiles();
            log.info("✅ Zodiac Profiles seeding completed!");
        } else {
            log.info("✓ Zodiac Profiles already seeded ({} profiles)", zodiacProfileRepository.count());
        }
    }

    @Transactional
    public void seedZodiacProfiles() {
        List<ZodiacProfile> profiles = Arrays.asList(
                createAries(),
                createTaurus(),
                createGemini(),
                createCancer(),
                createLeo(),
                createVirgo(),
                createLibra(),
                createScorpio(),
                createSagittarius(), // ♐ THE BEST!
                createCapricorn(),
                createAquarius(),
                createPisces()
        );

        zodiacProfileRepository.saveAll(profiles);
        log.info("Saved {} zodiac profiles", profiles.size());
    }

    private ZodiacProfile createAries() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Aries)
                .symbol("♈")
                .element(ZodiacProfile.Element.Fire)
                .modality(ZodiacProfile.Modality.Cardinal)
                .rulingPlanet("Mars")
                .dateStart("03-21")
                .dateEnd("04-19")
                .colorPrimary("#E74C3C")
                .colorSecondary("#C0392B")
                .colorGradientStart("#E74C3C")
                .colorGradientEnd("#C0392B")
                .personalityTraits(Arrays.asList(
                        "Bold and ambitious",
                        "Confident and optimistic",
                        "Passionate and energetic",
                        "Competitive and assertive",
                        "Pioneering spirit"
                ))
                .strengths(Arrays.asList(
                        "Natural leadership",
                        "Takes initiative",
                        "Courageous in challenges",
                        "Direct communication",
                        "Quick decision-making"
                ))
                .weaknesses(Arrays.asList(
                        "Impulsive decisions",
                        "Can be impatient",
                        "Sometimes aggressive",
                        "Difficulty with routine"
                ))
                .workStyle(Arrays.asList(
                        "Prefers to lead projects",
                        "Thrives in competitive environments",
                        "Action-oriented approach",
                        "Works best with clear goals"
                ))
                .bestRoles(Arrays.asList(
                        "Team Leader", "Entrepreneur", "Sales Manager", "Project Manager"
                ))
                .communicationStyle("Direct, assertive, and sometimes blunt. Prefers quick, to-the-point conversations.")
                .motivationFactors(Arrays.asList(
                        "Recognition and achievement",
                        "New challenges",
                        "Competitive goals",
                        "Independence"
                ))
                .stressTriggers(Arrays.asList(
                        "Slow progress",
                        "Micromanagement",
                        "Lack of action",
                        "Being held back"
                ))
                .leadershipStyle("Authoritative and inspiring. Leads from the front and expects others to follow.")
                .teamContribution("Motivates team with energy and enthusiasm. Great at initiating projects.")
                .conflictResolutionStyle("Confrontational but fair. Addresses issues head-on.")
                .descriptionLong("Aries is the first sign of the zodiac, symbolizing new beginnings and pioneering spirit. Known for courage, enthusiasm, and leadership.")
                .famousPeople(Arrays.asList("Leonardo da Vinci", "Lady Gaga", "Robert Downey Jr."))
                .build();
    }

    private ZodiacProfile createTaurus() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Taurus)
                .symbol("♉")
                .element(ZodiacProfile.Element.Earth)
                .modality(ZodiacProfile.Modality.Fixed)
                .rulingPlanet("Venus")
                .dateStart("04-20")
                .dateEnd("05-20")
                .colorPrimary("#27AE60")
                .colorSecondary("#229954")
                .colorGradientStart("#27AE60")
                .colorGradientEnd("#229954")
                .personalityTraits(Arrays.asList(
                        "Reliable and patient",
                        "Practical and grounded",
                        "Determined and persistent",
                        "Loves stability",
                        "Appreciates beauty and comfort"
                ))
                .strengths(Arrays.asList(
                        "Extremely reliable",
                        "Excellent follow-through",
                        "Strong work ethic",
                        "Calm under pressure",
                        "Great with details"
                ))
                .weaknesses(Arrays.asList(
                        "Can be stubborn",
                        "Resistant to change",
                        "May be possessive",
                        "Slow to adapt"
                ))
                .workStyle(Arrays.asList(
                        "Methodical and thorough",
                        "Prefers stable environment",
                        "Values quality over speed",
                        "Builds strong foundations"
                ))
                .bestRoles(Arrays.asList(
                        "Finance Manager", "Quality Assurance", "Operations Manager", "Administrator"
                ))
                .communicationStyle("Patient, thoughtful, and clear. Takes time to express ideas fully.")
                .motivationFactors(Arrays.asList(
                        "Financial security",
                        "Stability and routine",
                        "Quality outcomes",
                        "Material rewards"
                ))
                .stressTriggers(Arrays.asList(
                        "Sudden changes",
                        "Uncertainty",
                        "Rushed deadlines",
                        "Instability"
                ))
                .leadershipStyle("Steady and reliable. Leads by example and builds trust.")
                .teamContribution("Provides stability and consistency. Great at execution.")
                .conflictResolutionStyle("Patient but firm. Prefers to resolve issues calmly.")
                .descriptionLong("Taurus is known for stability, reliability, and appreciation for the finer things in life. Grounded and practical.")
                .famousPeople(Arrays.asList("William Shakespeare", "Adele", "David Beckham"))
                .build();
    }

    private ZodiacProfile createGemini() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Gemini)
                .symbol("♊")
                .element(ZodiacProfile.Element.Air)
                .modality(ZodiacProfile.Modality.Mutable)
                .rulingPlanet("Mercury")
                .dateStart("05-21")
                .dateEnd("06-20")
                .colorPrimary("#F39C12")
                .colorSecondary("#E67E22")
                .colorGradientStart("#F39C12")
                .colorGradientEnd("#E67E22")
                .personalityTraits(Arrays.asList(
                        "Curious and adaptable",
                        "Excellent communicator",
                        "Quick-witted and clever",
                        "Social and outgoing",
                        "Loves variety"
                ))
                .strengths(Arrays.asList(
                        "Outstanding communication",
                        "Quick learner",
                        "Versatile and flexible",
                        "Great networker",
                        "Innovative thinking"
                ))
                .weaknesses(Arrays.asList(
                        "Can be inconsistent",
                        "May lack focus",
                        "Indecisive at times",
                        "Restless nature"
                ))
                .workStyle(Arrays.asList(
                        "Multitasking champion",
                        "Thrives on variety",
                        "Communicates constantly",
                        "Collaborative approach"
                ))
                .bestRoles(Arrays.asList(
                        "Communications Manager", "Marketing", "Journalist", "Consultant"
                ))
                .communicationStyle("Articulate, engaging, and persuasive. Loves discussion and debate.")
                .motivationFactors(Arrays.asList(
                        "Intellectual stimulation",
                        "Variety and change",
                        "Social interaction",
                        "Learning opportunities"
                ))
                .stressTriggers(Arrays.asList(
                        "Monotony",
                        "Isolation",
                        "Lack of mental challenge",
                        "Rigid structures"
                ))
                .leadershipStyle("Collaborative and communicative. Encourages open dialogue.")
                .teamContribution("Brings fresh ideas and facilitates communication.")
                .conflictResolutionStyle("Uses communication and diplomacy to resolve issues.")
                .descriptionLong("Gemini is the sign of the twins, representing duality and versatility. Known for communication and adaptability.")
                .famousPeople(Arrays.asList("Marilyn Monroe", "Johnny Depp", "Angelina Jolie"))
                .build();
    }

    private ZodiacProfile createCancer() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Cancer)
                .symbol("♋")
                .element(ZodiacProfile.Element.Water)
                .modality(ZodiacProfile.Modality.Cardinal)
                .rulingPlanet("Moon")
                .dateStart("06-21")
                .dateEnd("07-22")
                .colorPrimary("#3498DB")
                .colorSecondary("#2980B9")
                .colorGradientStart("#3498DB")
                .colorGradientEnd("#2980B9")
                .personalityTraits(Arrays.asList(
                        "Nurturing and caring",
                        "Emotionally intuitive",
                        "Loyal and protective",
                        "Home-loving",
                        "Sensitive and empathetic"
                ))
                .strengths(Arrays.asList(
                        "Excellent emotional intelligence",
                        "Strong team loyalty",
                        "Intuitive problem-solving",
                        "Creates supportive environment",
                        "Remembers details about people"
                ))
                .weaknesses(Arrays.asList(
                        "Can be overly emotional",
                        "May take things personally",
                        "Moody at times",
                        "Difficulty letting go"
                ))
                .workStyle(Arrays.asList(
                        "Collaborative and supportive",
                        "Values team harmony",
                        "Detail-oriented",
                        "Prefers secure environment"
                ))
                .bestRoles(Arrays.asList(
                        "HR Manager", "Social Worker", "Counselor", "Team Coordinator"
                ))
                .communicationStyle("Empathetic and considerate. Reads between the lines well.")
                .motivationFactors(Arrays.asList(
                        "Team harmony",
                        "Emotional connection",
                        "Job security",
                        "Helping others"
                ))
                .stressTriggers(Arrays.asList(
                        "Conflict and tension",
                        "Criticism",
                        "Instability",
                        "Lack of appreciation"
                ))
                .leadershipStyle("Nurturing and protective. Creates family-like team culture.")
                .teamContribution("Builds strong relationships and maintains team morale.")
                .conflictResolutionStyle("Empathetic approach. Seeks to understand all perspectives.")
                .descriptionLong("Cancer is deeply emotional, intuitive, and caring. Known for loyalty and protective nature.")
                .famousPeople(Arrays.asList("Princess Diana", "Tom Hanks", "Selena Gomez"))
                .build();
    }

    private ZodiacProfile createLeo() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Leo)
                .symbol("♌")
                .element(ZodiacProfile.Element.Fire)
                .modality(ZodiacProfile.Modality.Fixed)
                .rulingPlanet("Sun")
                .dateStart("07-23")
                .dateEnd("08-22")
                .colorPrimary("#F1C40F")
                .colorSecondary("#F39C12")
                .colorGradientStart("#F1C40F")
                .colorGradientEnd("#F39C12")
                .personalityTraits(Arrays.asList(
                        "Confident and charismatic",
                        "Generous and warm-hearted",
                        "Creative and passionate",
                        "Natural performer",
                        "Proud and dignified"
                ))
                .strengths(Arrays.asList(
                        "Strong leadership presence",
                        "Inspires and motivates others",
                        "Creative problem-solving",
                        "Confident decision-making",
                        "Excellent at public speaking"
                ))
                .weaknesses(Arrays.asList(
                        "Can be dominating",
                        "May seek too much attention",
                        "Prideful",
                        "Difficulty accepting criticism"
                ))
                .workStyle(Arrays.asList(
                        "Leads with passion",
                        "Enjoys spotlight",
                        "Creative approach",
                        "Values recognition"
                ))
                .bestRoles(Arrays.asList(
                        "CEO", "Creative Director", "Public Relations", "Entertainment"
                ))
                .communicationStyle("Dramatic, engaging, and inspiring. Commands attention naturally.")
                .motivationFactors(Arrays.asList(
                        "Recognition and praise",
                        "Creative expression",
                        "Leadership opportunities",
                        "Prestige"
                ))
                .stressTriggers(Arrays.asList(
                        "Being ignored",
                        "Lack of recognition",
                        "Boring work",
                        "Criticism of performance"
                ))
                .leadershipStyle("Charismatic and commanding. Leads with confidence and vision.")
                .teamContribution("Motivates team and brings creative energy.")
                .conflictResolutionStyle("Direct and dramatic. Expects respect.")
                .descriptionLong("Leo is ruled by the Sun, representing vitality, creativity, and leadership. Confident and charismatic.")
                .famousPeople(Arrays.asList("Barack Obama", "Jennifer Lopez", "Madonna"))
                .build();
    }

    private ZodiacProfile createVirgo() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Virgo)
                .symbol("♍")
                .element(ZodiacProfile.Element.Earth)
                .modality(ZodiacProfile.Modality.Mutable)
                .rulingPlanet("Mercury")
                .dateStart("08-23")
                .dateEnd("09-22")
                .colorPrimary("#95A5A6")
                .colorSecondary("#7F8C8D")
                .colorGradientStart("#95A5A6")
                .colorGradientEnd("#7F8C8D")
                .personalityTraits(Arrays.asList(
                        "Analytical and practical",
                        "Detail-oriented perfectionist",
                        "Helpful and service-minded",
                        "Organized and efficient",
                        "Critical thinker"
                ))
                .strengths(Arrays.asList(
                        "Exceptional attention to detail",
                        "Analytical problem-solving",
                        "High quality standards",
                        "Organized and methodical",
                        "Reliable and hardworking"
                ))
                .weaknesses(Arrays.asList(
                        "Overly critical",
                        "Perfectionist to a fault",
                        "May worry excessively",
                        "Can be too harsh on self"
                ))
                .workStyle(Arrays.asList(
                        "Meticulous and precise",
                        "Systematic approach",
                        "Quality-focused",
                        "Continuous improvement"
                ))
                .bestRoles(Arrays.asList(
                        "Analyst", "Quality Control", "Editor", "Healthcare Professional"
                ))
                .communicationStyle("Precise, logical, and detailed. Focuses on facts.")
                .motivationFactors(Arrays.asList(
                        "Perfection and quality",
                        "Being helpful",
                        "Efficiency",
                        "Learning and improvement"
                ))
                .stressTriggers(Arrays.asList(
                        "Disorder and chaos",
                        "Low standards",
                        "Inefficiency",
                        "Lack of clarity"
                ))
                .leadershipStyle("Lead by setting high standards and showing attention to detail.")
                .teamContribution("Ensures quality and catches errors others miss.")
                .conflictResolutionStyle("Logical and practical. Focuses on solutions.")
                .descriptionLong("Virgo is known for precision, analysis, and service to others. Perfectionist and detail-oriented.")
                .famousPeople(Arrays.asList("Mother Teresa", "Beyoncé", "Keanu Reeves"))
                .build();
    }

    private ZodiacProfile createLibra() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Libra)
                .symbol("♎")
                .element(ZodiacProfile.Element.Air)
                .modality(ZodiacProfile.Modality.Cardinal)
                .rulingPlanet("Venus")
                .dateStart("09-23")
                .dateEnd("10-22")
                .colorPrimary("#E91E63")
                .colorSecondary("#C2185B")
                .colorGradientStart("#E91E63")
                .colorGradientEnd("#C2185B")
                .personalityTraits(Arrays.asList(
                        "Diplomatic and fair-minded",
                        "Social and gracious",
                        "Seeks harmony and balance",
                        "Appreciates beauty",
                        "Cooperative nature"
                ))
                .strengths(Arrays.asList(
                        "Excellent mediator",
                        "Fair and balanced decisions",
                        "Strong social skills",
                        "Creates harmony",
                        "Sees multiple perspectives"
                ))
                .weaknesses(Arrays.asList(
                        "Indecisive",
                        "Avoids confrontation",
                        "Can be people-pleasing",
                        "May carry grudges"
                ))
                .workStyle(Arrays.asList(
                        "Collaborative approach",
                        "Values teamwork",
                        "Diplomatic communication",
                        "Aesthetic awareness"
                ))
                .bestRoles(Arrays.asList(
                        "Mediator", "Diplomat", "Designer", "HR Professional"
                ))
                .communicationStyle("Diplomatic, charming, and balanced. Excellent at negotiation.")
                .motivationFactors(Arrays.asList(
                        "Harmony and peace",
                        "Fairness and justice",
                        "Beautiful environment",
                        "Partnership"
                ))
                .stressTriggers(Arrays.asList(
                        "Conflict and arguments",
                        "Unfairness",
                        "Making tough decisions alone",
                        "Ugliness or disorder"
                ))
                .leadershipStyle("Collaborative and fair. Seeks consensus.")
                .teamContribution("Maintains harmony and mediates conflicts.")
                .conflictResolutionStyle("Diplomatic mediator. Seeks win-win solutions.")
                .descriptionLong("Libra is the sign of balance, harmony, and justice. Known for diplomacy and fairness.")
                .famousPeople(Arrays.asList("Mahatma Gandhi", "Kim Kardashian", "Will Smith"))
                .build();
    }

    private ZodiacProfile createScorpio() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Scorpio)
                .symbol("♏")
                .element(ZodiacProfile.Element.Water)
                .modality(ZodiacProfile.Modality.Fixed)
                .rulingPlanet("Pluto")
                .dateStart("10-23")
                .dateEnd("11-21")
                .colorPrimary("#8E44AD")
                .colorSecondary("#71368A")
                .colorGradientStart("#8E44AD")
                .colorGradientEnd("#71368A")
                .personalityTraits(Arrays.asList(
                        "Intense and passionate",
                        "Resourceful and strategic",
                        "Deeply intuitive",
                        "Loyal and protective",
                        "Mysterious and private"
                ))
                .strengths(Arrays.asList(
                        "Strategic thinking",
                        "Deep focus and concentration",
                        "Loyal to core",
                        "Excellent researcher",
                        "Transformative leadership"
                ))
                .weaknesses(Arrays.asList(
                        "Can be secretive",
                        "May be jealous",
                        "Holds grudges",
                        "Overly intense"
                ))
                .workStyle(Arrays.asList(
                        "Deep focus on projects",
                        "Strategic approach",
                        "Private work style",
                        "All-or-nothing commitment"
                ))
                .bestRoles(Arrays.asList(
                        "Investigator", "Researcher", "Strategist", "Psychologist"
                ))
                .communicationStyle("Intense, probing, and insightful. Gets to the heart of matters.")
                .motivationFactors(Arrays.asList(
                        "Depth and meaning",
                        "Power and control",
                        "Transformation",
                        "Loyalty"
                ))
                .stressTriggers(Arrays.asList(
                        "Betrayal",
                        "Superficiality",
                        "Lack of privacy",
                        "Being controlled"
                ))
                .leadershipStyle("Intense and transformative. Leads through strategy.")
                .teamContribution("Brings depth, focus, and strategic thinking.")
                .conflictResolutionStyle("Direct and intense. Doesn't forgive easily.")
                .descriptionLong("Scorpio is known for intensity, passion, and transformation. Deep and strategic.")
                .famousPeople(Arrays.asList("Bill Gates", "Leonardo DiCaprio", "Katy Perry"))
                .build();
    }

    private ZodiacProfile createSagittarius() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Sagittarius)
                .symbol("♐")
                .element(ZodiacProfile.Element.Fire)
                .modality(ZodiacProfile.Modality.Mutable)
                .rulingPlanet("Jupiter")
                .dateStart("11-22")
                .dateEnd("12-21")
                .colorPrimary("#9B59B6")
                .colorSecondary("#3498DB")
                .colorGradientStart("#9B59B6")
                .colorGradientEnd("#3498DB")
                .personalityTraits(Arrays.asList(
                        "Optimistic and enthusiastic",
                        "Love freedom and exploration",
                        "Honest and direct",
                        "Philosophical and curious",
                        "Adventurous spirit",
                        "Big-picture thinker"
                ))
                .strengths(Arrays.asList(
                        "Excellent at brainstorming and innovation",
                        "Natural motivator for team",
                        "Adaptable to change",
                        "Great at seeing opportunities",
                        "Inspiring communicator",
                        "Brings positive energy"
                ))
                .weaknesses(Arrays.asList(
                        "Can be impatient with details",
                        "May over-promise",
                        "Restless with routine tasks",
                        "Sometimes tactless",
                        "Needs variety to stay engaged"
                ))
                .workStyle(Arrays.asList(
                        "Prefers flexible environment",
                        "Thrives in dynamic projects",
                        "Loves learning new things",
                        "Works best with autonomy",
                        "Big picture focus"
                ))
                .bestRoles(Arrays.asList(
                        "Entrepreneur", "Strategist", "Sales", "Marketing", "Teacher", "Trainer", "Project Manager"
                ))
                .communicationStyle("Direct, honest, and inspiring. Sometimes brutally truthful.")
                .motivationFactors(Arrays.asList(
                        "Growth and learning opportunities",
                        "Freedom to explore ideas",
                        "Meaningful work",
                        "Variety and challenge",
                        "Travel and adventure"
                ))
                .stressTriggers(Arrays.asList(
                        "Micromanagement",
                        "Routine and boredom",
                        "Restrictions on freedom",
                        "Negativity",
                        "Lack of vision"
                ))
                .leadershipStyle("Visionary and inspiring. Leads with optimism and big ideas.")
                .teamContribution("Motivates team, brings fresh perspectives, and maintains positive energy.")
                .conflictResolutionStyle("Direct and honest. Prefers to move forward rather than dwell.")
                .descriptionLong("Sagittarius, the Archer, is known for optimism, adventure, and the quest for truth and knowledge. Ruled by Jupiter, the planet of expansion and growth, Sagittarius individuals are natural philosophers and seekers. They bring enthusiasm, honesty, and a positive outlook to everything they do.")
                .famousPeople(Arrays.asList("Taylor Swift", "Brad Pitt", "Walt Disney", "Winston Churchill"))
                .build();
    }

    private ZodiacProfile createCapricorn() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Capricorn)
                .symbol("♑")
                .element(ZodiacProfile.Element.Earth)
                .modality(ZodiacProfile.Modality.Cardinal)
                .rulingPlanet("Saturn")
                .dateStart("12-22")
                .dateEnd("01-19")
                .colorPrimary("#34495E")
                .colorSecondary("#2C3E50")
                .colorGradientStart("#34495E")
                .colorGradientEnd("#2C3E50")
                .personalityTraits(Arrays.asList(
                        "Ambitious and disciplined",
                        "Responsible and practical",
                        "Patient and persistent",
                        "Traditional and respectful",
                        "Self-controlled"
                ))
                .strengths(Arrays.asList(
                        "Excellent long-term planning",
                        "Disciplined execution",
                        "Strong sense of responsibility",
                        "Reliable and dependable",
                        "Strategic thinking"
                ))
                .weaknesses(Arrays.asList(
                        "Can be pessimistic",
                        "Workaholic tendencies",
                        "May be too serious",
                        "Difficulty relaxing"
                ))
                .workStyle(Arrays.asList(
                        "Goal-oriented",
                        "Structured approach",
                        "Long-term focus",
                        "Professional demeanor"
                ))
                .bestRoles(Arrays.asList(
                        "Executive", "Manager", "Financial Planner", "Administrator"
                ))
                .communicationStyle("Professional, formal, and goal-oriented. Gets to the point.")
                .motivationFactors(Arrays.asList(
                        "Achievement and success",
                        "Recognition of hard work",
                        "Career advancement",
                        "Stability and structure"
                ))
                .stressTriggers(Arrays.asList(
                        "Failure or setbacks",
                        "Lack of structure",
                        "Irresponsibility",
                        "Wasted time"
                ))
                .leadershipStyle("Authoritative and structured. Leads by example and hard work.")
                .teamContribution("Provides structure, discipline, and long-term vision.")
                .conflictResolutionStyle("Practical and solution-focused. Maintains professionalism.")
                .descriptionLong("Capricorn is known for ambition, discipline, and achievement. Practical and goal-oriented.")
                .famousPeople(Arrays.asList("Michelle Obama", "Martin Luther King Jr.", "Denzel Washington"))
                .build();
    }

    private ZodiacProfile createAquarius() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Aquarius)
                .symbol("♒")
                .element(ZodiacProfile.Element.Air)
                .modality(ZodiacProfile.Modality.Fixed)
                .rulingPlanet("Uranus")
                .dateStart("01-20")
                .dateEnd("02-18")
                .colorPrimary("#1ABC9C")
                .colorSecondary("#16A085")
                .colorGradientStart("#1ABC9C")
                .colorGradientEnd("#16A085")
                .personalityTraits(Arrays.asList(
                        "Innovative and original",
                        "Independent thinker",
                        "Humanitarian and progressive",
                        "Intellectual and analytical",
                        "Unconventional"
                ))
                .strengths(Arrays.asList(
                        "Innovative problem-solving",
                        "Visionary thinking",
                        "Independent work style",
                        "Objective perspective",
                        "Forward-thinking"
                ))
                .weaknesses(Arrays.asList(
                        "Can be detached emotionally",
                        "Stubborn about ideas",
                        "May be unpredictable",
                        "Difficulty with emotions"
                ))
                .workStyle(Arrays.asList(
                        "Innovative approach",
                        "Values independence",
                        "Future-focused",
                        "Unconventional methods"
                ))
                .bestRoles(Arrays.asList(
                        "Innovator", "Technology Expert", "Social Activist", "Scientist"
                ))
                .communicationStyle("Intellectual, unconventional, and objective. Thinks outside the box.")
                .motivationFactors(Arrays.asList(
                        "Innovation and progress",
                        "Independence",
                        "Making a difference",
                        "Intellectual freedom"
                ))
                .stressTriggers(Arrays.asList(
                        "Conformity",
                        "Emotional drama",
                        "Traditional restrictions",
                        "Lack of freedom"
                ))
                .leadershipStyle("Visionary and innovative. Leads with ideas and future focus.")
                .teamContribution("Brings innovation, objectivity, and progressive thinking.")
                .conflictResolutionStyle("Logical and detached. Seeks innovative solutions.")
                .descriptionLong("Aquarius is the humanitarian of the zodiac, focused on progress and innovation. Visionary and independent.")
                .famousPeople(Arrays.asList("Oprah Winfrey", "Abraham Lincoln", "Ellen DeGeneres"))
                .build();
    }

    private ZodiacProfile createPisces() {
        return ZodiacProfile.builder()
                .zodiacSign(ZodiacProfile.ZodiacSign.Pisces)
                .symbol("♓")
                .element(ZodiacProfile.Element.Water)
                .modality(ZodiacProfile.Modality.Mutable)
                .rulingPlanet("Neptune")
                .dateStart("02-19")
                .dateEnd("03-20")
                .colorPrimary("#9B59B6")
                .colorSecondary("#8E44AD")
                .colorGradientStart("#9B59B6")
                .colorGradientEnd("#8E44AD")
                .personalityTraits(Arrays.asList(
                        "Compassionate and empathetic",
                        "Artistic and imaginative",
                        "Intuitive and spiritual",
                        "Gentle and wise",
                        "Selfless and giving"
                ))
                .strengths(Arrays.asList(
                        "Deep empathy and understanding",
                        "Creative problem-solving",
                        "Intuitive insights",
                        "Adaptable and flexible",
                        "Compassionate leadership"
                ))
                .weaknesses(Arrays.asList(
                        "Can be overly emotional",
                        "May escape reality",
                        "Difficulty with boundaries",
                        "Too trusting"
                ))
                .workStyle(Arrays.asList(
                        "Creative and imaginative",
                        "Empathetic collaboration",
                        "Intuitive approach",
                        "Flexible and adaptable"
                ))
                .bestRoles(Arrays.asList(
                        "Artist", "Therapist", "Social Worker", "Creative Director"
                ))
                .communicationStyle("Empathetic, gentle, and intuitive. Reads emotions well.")
                .motivationFactors(Arrays.asList(
                        "Helping others",
                        "Creative expression",
                        "Spiritual growth",
                        "Emotional connection"
                ))
                .stressTriggers(Arrays.asList(
                        "Harsh criticism",
                        "Rigid structures",
                        "Lack of creativity",
                        "Emotional coldness"
                ))
                .leadershipStyle("Compassionate and intuitive. Leads with empathy.")
                .teamContribution("Brings creativity, empathy, and intuitive insights.")
                .conflictResolutionStyle("Empathetic and understanding. Seeks emotional resolution.")
                .descriptionLong("Pisces is the most intuitive and compassionate sign of the zodiac. Known for empathy, creativity, and spiritual depth.")
                .famousPeople(Arrays.asList("Albert Einstein", "Rihanna", "Steve Jobs"))
                .build();
    }
}