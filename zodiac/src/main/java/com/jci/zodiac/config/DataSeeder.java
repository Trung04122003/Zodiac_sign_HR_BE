package com.jci.zodiac.config;

import com.jci.zodiac.entity.Department;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.repository.DepartmentRepository;
import com.jci.zodiac.repository.MemberRepository;
import com.jci.zodiac.util.ZodiacCalculator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSeeder - Seeds initial data for JCI Danang Junior Club
 * Only runs in DEV profile
 * Seeds: 18 Members + 7 Departments
 */
@Component
@Profile("dev") // Only run in development environment
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    private final Map<String, Long> departmentMap = new HashMap<>();

    @PostConstruct
    @Transactional
    public void seedData() {
        log.info("🌟 Starting DataSeeder for JCI Danang Junior Club...");

        // Check if data already exists
        if (memberRepository.count() > 0) {
            log.info("✅ Data already seeded. Skipping...");
            return;
        }

        try {
            seedDepartments();
            seedMembers();
            log.info("🎉 Data seeding completed successfully!");
        } catch (Exception e) {
            log.error("❌ Error during data seeding: {}", e.getMessage(), e);
        }
    }

    private void seedDepartments() {
        log.info("📂 Seeding Departments...");

        Department[] departments = {
                createDepartment("Nhân sự", "HR", "Human Resources Department", Member.ZodiacSign.Leo, "#F59E0B", "#FCD34D"),
                createDepartment("Đào tạo", "TRAINING", "Training & Development Department", Member.ZodiacSign.Sagittarius, "#8B5CF6", "#C4B5FD"),
                createDepartment("Truyền thông", "SOCIAL_MEDIA", "Social Media & Communications Department", Member.ZodiacSign.Gemini, "#3B82F6", "#93C5FD"),
                createDepartment("Sự kiện", "EVENT", "Events Management Department", Member.ZodiacSign.Aries, "#EF4444", "#FCA5A5"),
                createDepartment("Đối ngoại", "PARTNERSHIP", "External Affairs & Partnership Department", Member.ZodiacSign.Libra, "#10B981", "#6EE7B7"),
                createDepartment("Dự án", "PROJECT", "Project Management Department", Member.ZodiacSign.Capricorn, "#6366F1", "#A5B4FC"),
                createDepartment("Thư ký", "SECRETARY", "Secretary Department", Member.ZodiacSign.Virgo, "#EC4899", "#F9A8D4")
        };

        for (Department dept : departments) {
            Department saved = departmentRepository.save(dept);
            departmentMap.put(dept.getCode(), saved.getId());
            log.info("  ✓ Created department: {} (ID: {})", saved.getName(), saved.getId());
        }

        log.info("✅ {} departments seeded successfully!", departments.length);
    }

    private void seedMembers() {
        log.info("👥 Seeding Members...");

        int memberCount = 1;

        // Executive Board (Excom)
        memberCount = createMember(memberCount, "Trần Thị Kiều Duyên", LocalDate.of(2005, 2, 25),
                "Chair Person", null, "tranthikieuduyen2502@gmail.com", "0868954916",
                "https://www.facebook.com/duyen.tranthikieu.56#", "Chair");

        memberCount = createMember(memberCount, "Lê Thái Trung", LocalDate.of(2003, 12, 4),
                "Vice President - Membership & Training", departmentMap.get("TRAINING"),
                "lethaitrung03@gmail.com", "0799002186",
                "https://www.facebook.com/thaitrung.le.94", "Vice President");

        memberCount = createMember(memberCount, "Võ Hoàng Như Ý", LocalDate.of(2005, 5, 18),
                "Vice President - External Affairs", departmentMap.get("PARTNERSHIP"),
                "kieravo1805@gmail.com", "0935397418",
                "https://www.facebook.com/NhuYVoKiera#", "Vice President");

        memberCount = createMember(memberCount, "Nguyễn Hồ Thành Nhân", LocalDate.of(2003, 10, 23),
                "Vice President - Social & Events", departmentMap.get("EVENT"),
                "nguyenperba2310@gmail.com", "0769644085",
                "https://www.facebook.com/nguyenPer2003#", "Vice President");

        memberCount = createMember(memberCount, "Trương Quốc Dũng", LocalDate.of(2005, 4, 21),
                "Secretary General", departmentMap.get("SECRETARY"),
                "vetday892@gmail.com", "0349730891",
                "https://www.facebook.com/Truongquocdung.21#", "Secretary General");

        memberCount = createMember(memberCount, "Trần Thị Hoài Linh", LocalDate.of(2005, 9, 30),
                "Treasurer", null, "tranthihoailinh3009@gmail.com", "0385195294",
                "https://www.facebook.com/hoailinh.tranthi.180410#", "Treasurer");

        memberCount = createMember(memberCount, "Nguyễn Đại Hải Long", LocalDate.of(2007, 7, 27),
                "Legal Advisor", null, "tonyandhailong@gmail.com", "0935056738",
                "https://www.facebook.com/long.hello.9400#", "Legal Advisor");

        // Team Leaders
        memberCount = createMember(memberCount, "Dũ Ngọc Mỹ Dung", LocalDate.of(2006, 11, 15),
                "Leader Team - Human Resources", departmentMap.get("HR"),
                "dungocmydung@gmail.com", "0327117464",
                "https://www.facebook.com/dung.du.73345#", "Team Leader");

        memberCount = createMember(memberCount, "Nguyễn Gia Triều", LocalDate.of(2005, 11, 2),
                "Leader Team - Training", departmentMap.get("TRAINING"),
                "giatrieu.study@gmail.com", "0935097901",
                "https://www.facebook.com/giatrieuodayne05#", "Team Leader");

        memberCount = createMember(memberCount, "Nguyễn Phi Long", LocalDate.of(2006, 4, 13),
                "Leader Team - Partnership", departmentMap.get("PARTNERSHIP"),
                "plwgg13@gmail.com", "0387279917",
                "https://www.facebook.com/Plwgg13#", "Team Leader");

        memberCount = createMember(memberCount, "Tống Phước Hoài Nam", LocalDate.of(2005, 6, 26),
                "Leader Team - Social Media", departmentMap.get("SOCIAL_MEDIA"),
                "tphn26.work@gmail.com", "0859215285",
                "https://www.facebook.com/tetsdee#", "Team Leader");

        memberCount = createMember(memberCount, "Lưu Thúy Diễm", LocalDate.of(2006, 9, 24),
                "Leader Team - Events", departmentMap.get("EVENT"),
                "luuhoangdiem2409@gmail.com", "0772145846",
                "https://www.facebook.com/k.s.y.2409#", "Team Leader");

        // Regular Members
        memberCount = createMember(memberCount, "Nguyễn Hoàng Phú", LocalDate.of(2004, 11, 29),
                "Member", departmentMap.get("PROJECT"),
                "nguyenhoangphudut@gmail.com", "0329277079",
                "https://www.facebook.com/nguyen.hoang.phu.2911#", "Member");

        memberCount = createMember(memberCount, "Lê Quốc Lập", LocalDate.of(2001, 12, 19),
                "Member", departmentMap.get("PROJECT"),
                "lelap7919@gmail.com", "0396803253",
                "https://www.facebook.com/quoc.lap.tft.21495367#", "Member");

        memberCount = createMember(memberCount, "Lê Thường An", LocalDate.of(2005, 5, 2),
                "Member", departmentMap.get("TRAINING"),
                "thuongancr265@gmail.com", "0777403919",
                "https://www.facebook.com/an.lethuong.9#", "Member");

        memberCount = createMember(memberCount, "Nguyễn Thị Diệp Trinh", LocalDate.of(2006, 4, 7),
                "Member", departmentMap.get("SOCIAL_MEDIA"),
                "nguyenthidieptrinh066@gmail.com", "0392009386",
                "https://www.facebook.com/ndtrinhh", "Member");

        memberCount = createMember(memberCount, "Mai Thị Thu Ngọc", LocalDate.of(2005, 2, 1),
                "Member", departmentMap.get("EVENT"),
                "maithithungoc01022005@gmail.com", "0819126252",
                "https://www.facebook.com/thu.ngoc.550998#", "Member");

        memberCount = createMember(memberCount, "Lương Thị Xuân Quỳnh", LocalDate.of(2005, 10, 19),
                "Member", departmentMap.get("HR"),
                "xuannquynhh63@gmail.com", "0854765159",
                "https://www.facebook.com/xuann.quynhh.205#", "Member");

        log.info("✅ {} members seeded successfully!", memberCount - 1);
    }

    private int createMember(int count, String fullName, LocalDate dob,
                             String position, Long departmentId,
                             String email, String phone, String facebookUrl, String memberType) {

        // Auto-calculate zodiac sign & element
        Member.ZodiacSign zodiacSign = ZodiacCalculator.calculateZodiacSign(dob);
        Member.ZodiacElement zodiacElement = ZodiacCalculator.calculateZodiacElement(zodiacSign);

        // Generate member code
        String memberCode = String.format("JCI-DN-%03d", count);

        Member member = Member.builder()
                .memberCode(memberCode)
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .dateOfBirth(dob)
                .zodiacSign(zodiacSign)
                .zodiacElement(zodiacElement)
                .position(position)
                .departmentId(departmentId)
                .joinDate(LocalDate.of(2024, 1, 1)) // All joined Jan 1, 2024
                .membershipStatus(Member.MembershipStatus.Active)
                .membershipType(getMembershipType(memberType))
                .city("Da Nang")
                .facebookUrl(facebookUrl)
                .company("JCI Danang Junior Club")
                .createdBy(1L)
                .build();

        Member saved = memberRepository.save(member);
        log.info("  ✓ Created member: {} - {} ({}) - Zodiac: {} {}",
                saved.getMemberCode(), saved.getFullName(), saved.getPosition(),
                saved.getZodiacSign(), getZodiacSymbol(saved.getZodiacSign()));

        return count + 1;
    }

    private Department createDepartment(String name, String code, String description,
                                        Member.ZodiacSign zodiacTheme,
                                        String colorPrimary, String colorSecondary) {
        return Department.builder()
                .name(name)
                .code(code)
                .description(description)
                .zodiacTheme(Department.ZodiacSign.valueOf(zodiacTheme.name()))
                .colorPrimary(colorPrimary)
                .colorSecondary(colorSecondary)
                .memberCount(0)
                .activeProjectsCount(0)
                .isActive(true)
                .build();
    }

    private Member.MembershipType getMembershipType(String memberType) {
        return switch (memberType) {
            case "Chair", "Vice President", "Secretary General", "Treasurer", "Legal Advisor" ->
                    Member.MembershipType.FullMember;
            case "Team Leader" -> Member.MembershipType.FullMember;
            default -> Member.MembershipType.FullMember;
        };
    }

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
}