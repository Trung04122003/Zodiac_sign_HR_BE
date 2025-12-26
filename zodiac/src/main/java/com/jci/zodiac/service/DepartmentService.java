package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.CreateDepartmentRequest;
import com.jci.zodiac.dto.request.UpdateDepartmentRequest;
import com.jci.zodiac.dto.response.DepartmentAnalyticsResponse;
import com.jci.zodiac.dto.response.DepartmentResponse;
import com.jci.zodiac.dto.response.DepartmentSummaryResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Department;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.exception.DuplicateResourceException;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.mapper.MemberMapper;
import com.jci.zodiac.repository.DepartmentRepository;
import com.jci.zodiac.repository.MemberRepository;
import com.jci.zodiac.util.ZodiacCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DepartmentService - Department management business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final CompatibilityService compatibilityService;
    private final MemberMapper memberMapper;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Create department
     */
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        log.info("Creating department: {}", request.getName());

        // Validate uniqueness
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Department", "code", request.getCode());
        }
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department", "name", request.getName());
        }

        // Set zodiac theme colors if not provided
        if (request.getZodiacTheme() != null && request.getColorPrimary() == null) {
            setZodiacThemeColors(request);
        }

        Department department = Department.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .zodiacTheme(request.getZodiacTheme())
                .colorPrimary(request.getColorPrimary())
                .colorSecondary(request.getColorSecondary())
                .iconUrl(request.getIconUrl())
                .leadMemberId(request.getLeadMemberId())
                .activeProjectsCount(request.getActiveProjectsCount())
                .memberCount(0)
                .isActive(true)
                .build();

        Department saved = departmentRepository.save(department);
        log.info("Department created: {}", saved.getCode());

        return toResponse(saved);
    }

    /**
     * Get department by ID
     */
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        log.debug("Fetching department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        return toResponse(department);
    }

    /**
     * Get department by code
     */
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentByCode(String code) {
        log.debug("Fetching department with code: {}", code);

        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));

        return toResponse(department);
    }

    /**
     * Get all departments
     */
    @Transactional(readOnly = true)
    public List<DepartmentSummaryResponse> getAllDepartments() {
        log.debug("Fetching all departments");

        return departmentRepository.findAll().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active departments
     */
    @Transactional(readOnly = true)
    public List<DepartmentSummaryResponse> getActiveDepartments() {
        log.debug("Fetching active departments");

        return departmentRepository.findByIsActive(true).stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update department
     */
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {
        log.info("Updating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Update fields
        if (request.getName() != null) {
            if (!request.getName().equals(department.getName()) &&
                    departmentRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Department", "name", request.getName());
            }
            department.setName(request.getName());
        }

        if (request.getDescription() != null) {
            department.setDescription(request.getDescription());
        }

        if (request.getZodiacTheme() != null) {
            department.setZodiacTheme(request.getZodiacTheme());
        }

        if (request.getColorPrimary() != null) {
            department.setColorPrimary(request.getColorPrimary());
        }

        if (request.getColorSecondary() != null) {
            department.setColorSecondary(request.getColorSecondary());
        }

        if (request.getIconUrl() != null) {
            department.setIconUrl(request.getIconUrl());
        }

        if (request.getLeadMemberId() != null) {
            department.setLeadMemberId(request.getLeadMemberId());
        }

        if (request.getActiveProjectsCount() != null) {
            department.setActiveProjectsCount(request.getActiveProjectsCount());
        }

        if (request.getIsActive() != null) {
            department.setIsActive(request.getIsActive());
        }

        Department updated = departmentRepository.save(department);
        log.info("Department updated: {}", updated.getCode());

        return toResponse(updated);
    }

    /**
     * Delete department
     */
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Remove department from all members
        List<Member> members = memberRepository.findByDepartmentId(id);
        members.forEach(member -> member.setDepartmentId(null));
        memberRepository.saveAll(members);

        // Delete department
        departmentRepository.delete(department);
        log.info("Department deleted: {}", department.getCode());
    }

    /**
     * Assign member to department
     */
    public void assignMemberToDepartment(Long departmentId, Long memberId) {
        log.info("Assigning member {} to department {}", memberId, departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));

        // Remove from old department if exists
        if (member.getDepartmentId() != null) {
            Department oldDept = departmentRepository.findById(member.getDepartmentId()).orElse(null);
            if (oldDept != null) {
                oldDept.decrementMemberCount();
                departmentRepository.save(oldDept);
            }
        }

        // Assign to new department
        member.setDepartmentId(departmentId);
        memberRepository.save(member);

        department.incrementMemberCount();
        departmentRepository.save(department);

        log.info("Member {} assigned to department {}", member.getMemberCode(), department.getCode());
    }

    /**
     * Remove member from department
     */
    public void removeMemberFromDepartment(Long departmentId, Long memberId) {
        log.info("Removing member {} from department {}", memberId, departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));

        if (!departmentId.equals(member.getDepartmentId())) {
            throw new IllegalArgumentException("Member is not in this department");
        }

        member.setDepartmentId(null);
        memberRepository.save(member);

        department.decrementMemberCount();
        departmentRepository.save(department);

        log.info("Member {} removed from department {}", member.getMemberCode(), department.getCode());
    }

    /**
     * Get department analytics
     */
    @Transactional(readOnly = true)
    public DepartmentAnalyticsResponse getDepartmentAnalytics(Long departmentId) {
        log.info("Generating analytics for department: {}", departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        List<Member> members = memberRepository.findByDepartmentId(departmentId);

        if (members.isEmpty()) {
            return createEmptyAnalytics(department);
        }

        // Member statistics
        long activeCount = members.stream()
                .filter(Member::isActive)
                .count();

        // Zodiac distribution
        Map<Member.ZodiacSign, Long> zodiacDist = zodiacUtilityService.calculateZodiacDistribution(members);
        Member.ZodiacSign mostCommon = zodiacUtilityService.getMostCommonZodiacSign(members);
        Member.ZodiacSign leastCommon = zodiacUtilityService.getLeastCommonZodiacSign(members);

        // Element balance
        Map<Member.ZodiacElement, Long> elementBalance = zodiacUtilityService.calculateElementBalance(members);
        boolean isBalanced = zodiacUtilityService.isTeamBalanced(members);
        List<String> missingElements = zodiacUtilityService.getMissingElements(members).stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        Member.ZodiacElement dominantElement = elementBalance.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Compatibility analysis
        List<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toList());
        CompatibilityService.TeamCompatibilityResult teamComp = null;
        BigDecimal avgCompatibility = BigDecimal.ZERO;
        String compatLevel = "N/A";
        int conflictCount = 0;

        if (memberIds.size() >= 2) {
            teamComp = compatibilityService.calculateTeamCompatibility(memberIds);
            avgCompatibility = teamComp.averageCompatibilityScore();
            compatLevel = teamComp.overallLevel();
            conflictCount = teamComp.potentialConflicts().size();
        }

        // Insights
        String teamInsight = zodiacUtilityService.generateTeamInsight(members);
        List<String> strengths = generateDepartmentStrengths(members, teamComp);
        List<String> challenges = generateDepartmentChallenges(members, teamComp, conflictCount);
        List<String> recommendations = generateDepartmentRecommendations(members, isBalanced, conflictCount);

        return DepartmentAnalyticsResponse.builder()
                .departmentId(department.getId())
                .departmentName(department.getName())
                .departmentCode(department.getCode())
                .totalMembers(members.size())
                .activeMembers((int) activeCount)
                .inactiveMembers(members.size() - (int) activeCount)
                .zodiacDistribution(zodiacDist)
                .mostCommonZodiacSign(mostCommon != null ? mostCommon.name() : null)
                .leastCommonZodiacSign(leastCommon != null ? leastCommon.name() : null)
                .elementBalance(elementBalance)
                .isElementBalanced(isBalanced)
                .missingElements(missingElements)
                .dominantElement(dominantElement != null ? dominantElement.name() : null)
                .averageTeamCompatibility(avgCompatibility)
                .teamCompatibilityLevel(compatLevel)
                .potentialConflictCount(conflictCount)
                .teamStrengths(strengths)
                .teamChallenges(challenges)
                .recommendations(recommendations)
                .build();
    }

    // Helper methods
    private void setZodiacThemeColors(CreateDepartmentRequest request) {
        // Map zodiac signs to default colors (Sagittarius theme!)
        Map<Department.ZodiacSign, String[]> zodiacColors = Map.ofEntries(
                Map.entry(Department.ZodiacSign.Sagittarius, new String[]{"#9B59B6", "#3498DB"}),
                Map.entry(Department.ZodiacSign.Aries, new String[]{"#E74C3C", "#C0392B"}),
                Map.entry(Department.ZodiacSign.Leo, new String[]{"#F1C40F", "#F39C12"}),
                Map.entry(Department.ZodiacSign.Taurus, new String[]{"#27AE60", "#229954"}),
                Map.entry(Department.ZodiacSign.Virgo, new String[]{"#95A5A6", "#7F8C8D"}),
                Map.entry(Department.ZodiacSign.Capricorn, new String[]{"#34495E", "#2C3E50"})
        );

        String[] colors = zodiacColors.getOrDefault(request.getZodiacTheme(), new String[]{"#3498DB", "#2980B9"});
        request.setColorPrimary(colors[0]);
        request.setColorSecondary(colors[1]);
    }

    private DepartmentResponse toResponse(Department department) {
        String leadMemberName = null;
        if (department.getLeadMemberId() != null) {
            leadMemberName = memberRepository.findById(department.getLeadMemberId())
                    .map(Member::getFullName)
                    .orElse(null);
        }

        String zodiacSymbol = null;
        if (department.getZodiacTheme() != null) {
            zodiacSymbol = ZodiacCalculator.getSymbol(
                    Member.ZodiacSign.valueOf(department.getZodiacTheme().name())
            );
        }

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .zodiacTheme(department.getZodiacTheme() != null ? department.getZodiacTheme().name() : null)
                .zodiacSymbol(zodiacSymbol)
                .colorPrimary(department.getColorPrimary())
                .colorSecondary(department.getColorSecondary())
                .iconUrl(department.getIconUrl())
                .leadMemberId(department.getLeadMemberId())
                .leadMemberName(leadMemberName)
                .memberCount(department.getMemberCount())
                .activeProjectsCount(department.getActiveProjectsCount())
                .isActive(department.getIsActive())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    private DepartmentSummaryResponse toSummaryResponse(Department department) {
        String leadMemberName = null;
        if (department.getLeadMemberId() != null) {
            leadMemberName = memberRepository.findById(department.getLeadMemberId())
                    .map(Member::getFullName)
                    .orElse(null);
        }

        String zodiacSymbol = null;
        if (department.getZodiacTheme() != null) {
            zodiacSymbol = ZodiacCalculator.getSymbol(
                    Member.ZodiacSign.valueOf(department.getZodiacTheme().name())
            );
        }

        return DepartmentSummaryResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .zodiacTheme(department.getZodiacTheme() != null ? department.getZodiacTheme().name() : null)
                .zodiacSymbol(zodiacSymbol)
                .memberCount(department.getMemberCount())
                .leadMemberName(leadMemberName)
                .isActive(department.getIsActive())
                .build();
    }

    private DepartmentAnalyticsResponse createEmptyAnalytics(Department department) {
        return DepartmentAnalyticsResponse.builder()
                .departmentId(department.getId())
                .departmentName(department.getName())
                .departmentCode(department.getCode())
                .totalMembers(0)
                .activeMembers(0)
                .inactiveMembers(0)
                .zodiacDistribution(new HashMap<>())
                .elementBalance(new HashMap<>())
                .isElementBalanced(false)
                .missingElements(Arrays.asList("Fire", "Earth", "Air", "Water"))
                .averageTeamCompatibility(BigDecimal.ZERO)
                .teamCompatibilityLevel("N/A")
                .potentialConflictCount(0)
                .teamStrengths(Collections.emptyList())
                .teamChallenges(List.of("No members in department"))
                .recommendations(List.of("Add members to begin building the team"))
                .build();
    }

    private List<String> generateDepartmentStrengths(List<Member> members,
                                                     CompatibilityService.TeamCompatibilityResult teamComp) {
        List<String> strengths = new ArrayList<>();
        if (teamComp != null && teamComp.averageCompatibilityScore().compareTo(BigDecimal.valueOf(70)) >= 0) {
            strengths.add("Strong team compatibility");
        }
        return strengths;
    }

    private List<String> generateDepartmentChallenges(List<Member> members,
                                                      CompatibilityService.TeamCompatibilityResult teamComp,
                                                      int conflictCount) {
        List<String> challenges = new ArrayList<>();
        if (conflictCount > 0) {
            challenges.add(String.format("%d potential conflicts detected", conflictCount));
        }
        return challenges;
    }

    private List<String> generateDepartmentRecommendations(List<Member> members,
                                                           boolean isBalanced,
                                                           int conflictCount) {
        List<String> recommendations = new ArrayList<>();
        if (!isBalanced) {
            recommendations.add("Consider adding members from missing elements");
        }
        if (conflictCount > 0) {
            recommendations.add("Implement conflict management strategies");
        }
        return recommendations;
    }
}