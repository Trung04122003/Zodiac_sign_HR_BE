package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.AddTeamMemberRequest;
import com.jci.zodiac.dto.request.CreateTeamRequest;
import com.jci.zodiac.dto.request.UpdateTeamRequest;
import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.*;
import com.jci.zodiac.exception.DuplicateResourceException;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.mapper.MemberMapper;
import com.jci.zodiac.repository.*;
import com.jci.zodiac.util.ZodiacCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TeamService - Team management business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final CompatibilityService compatibilityService;
    private final MemberMapper memberMapper;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Create team
     */
    public TeamResponse createTeam(CreateTeamRequest request) {
        log.info("Creating team: {}", request.getName());

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .departmentId(request.getDepartmentId())
                .teamType(request.getTeamType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .targetMemberCount(request.getTargetMemberCount())
                .memberCount(0)
                .status(Team.Status.Planning)
                .build();

        Team savedTeam = teamRepository.save(team);

        // Add initial members if provided
        if (request.getInitialMemberIds() != null && !request.getInitialMemberIds().isEmpty()) {
            for (Long memberId : request.getInitialMemberIds()) {
                addMemberToTeam(savedTeam.getId(), AddTeamMemberRequest.builder()
                        .memberId(memberId)
                        .role("Member")
                        .joinedDate(LocalDate.now())
                        .build());
            }
            savedTeam = teamRepository.findById(savedTeam.getId()).orElse(savedTeam);
        }

        log.info("Team created: {}", savedTeam.getName());
        return toResponse(savedTeam);
    }

    /**
     * Get team by ID
     */
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) {
        log.debug("Fetching team with id: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        return toResponse(team);
    }

    /**
     * Get all teams
     */
    @Transactional(readOnly = true)
    public List<TeamSummaryResponse> getAllTeams() {
        log.debug("Fetching all teams");

        return teamRepository.findAll().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active teams
     */
    @Transactional(readOnly = true)
    public List<TeamSummaryResponse> getActiveTeams() {
        log.debug("Fetching active teams");

        return teamRepository.findActiveTeams().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get teams by status
     */
    @Transactional(readOnly = true)
    public List<TeamSummaryResponse> getTeamsByStatus(Team.Status status) {
        log.debug("Fetching teams with status: {}", status);

        return teamRepository.findByStatus(status).stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update team
     */
    public TeamResponse updateTeam(Long id, UpdateTeamRequest request) {
        log.info("Updating team with id: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        if (request.getName() != null) {
            team.setName(request.getName());
        }
        if (request.getDescription() != null) {
            team.setDescription(request.getDescription());
        }
        if (request.getDepartmentId() != null) {
            team.setDepartmentId(request.getDepartmentId());
        }
        if (request.getTeamType() != null) {
            team.setTeamType(request.getTeamType());
        }
        if (request.getStartDate() != null) {
            team.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            team.setEndDate(request.getEndDate());
        }
        if (request.getStatus() != null) {
            team.setStatus(request.getStatus());
        }
        if (request.getTargetMemberCount() != null) {
            team.setTargetMemberCount(request.getTargetMemberCount());
        }

        Team updated = teamRepository.save(team);
        log.info("Team updated: {}", updated.getName());

        return toResponse(updated);
    }

    /**
     * Delete team
     */
    public void deleteTeam(Long id) {
        log.info("Deleting team with id: {}", id);

        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team", "id", id);
        }

        // TeamMember records will be cascade deleted
        teamRepository.deleteById(id);
        log.info("Team deleted with id: {}", id);
    }

    /**
     * Add member to team
     */
    public TeamMemberResponse addMemberToTeam(Long teamId, AddTeamMemberRequest request) {
        log.info("Adding member {} to team {}", request.getMemberId(), teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", request.getMemberId()));

        // Check if already member
        if (teamMemberRepository.existsByTeamIdAndMemberIdAndIsActive(teamId, request.getMemberId(), true)) {
            throw new DuplicateResourceException("Member already in team");
        }

        TeamMember teamMember = TeamMember.builder()
                .teamId(teamId)
                .memberId(request.getMemberId())
                .role(request.getRole())
                .joinedDate(request.getJoinedDate())
                .notes(request.getNotes())
                .isActive(true)
                .build();

        TeamMember saved = teamMemberRepository.save(teamMember);

        // Update team member count
        team.incrementMemberCount();
        teamRepository.save(team);

        // Recalculate team compatibility
        recalculateTeamCompatibility(teamId);

        log.info("Member {} added to team {}", member.getMemberCode(), team.getName());

        return toTeamMemberResponse(saved);
    }

    /**
     * Remove member from team
     */
    public void removeMemberFromTeam(Long teamId, Long memberId) {
        log.info("Removing member {} from team {}", memberId, teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember not found"));

        // Mark as inactive
        teamMember.setIsActive(false);
        teamMember.setLeftDate(LocalDate.now());
        teamMemberRepository.save(teamMember);

        // Update team member count
        team.decrementMemberCount();
        teamRepository.save(team);

        // Recalculate team compatibility
        recalculateTeamCompatibility(teamId);

        log.info("Member removed from team");
    }

    /**
     * Get team members
     */
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {
        log.debug("Fetching members for team: {}", teamId);

        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Team", "id", teamId);
        }

        List<TeamMember> teamMembers = teamMemberRepository.findByTeamIdAndIsActive(teamId, true);

        return teamMembers.stream()
                .map(this::toTeamMemberResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get team analytics
     */
    @Transactional(readOnly = true)
    public TeamAnalyticsResponse getTeamAnalytics(Long teamId) {
        log.info("Generating analytics for team: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        List<TeamMember> teamMembers = teamMemberRepository.findByTeamIdAndIsActive(teamId, true);

        if (teamMembers.isEmpty()) {
            return createEmptyTeamAnalytics(team);
        }

        // Get member IDs
        List<Long> memberIds = teamMembers.stream()
                .map(TeamMember::getMemberId)
                .collect(Collectors.toList());

        // Calculate team compatibility
        CompatibilityService.TeamCompatibilityResult compatResult =
                compatibilityService.calculateTeamCompatibility(memberIds);

        // Get members
        List<Member> members = memberRepository.findAllById(memberIds);

        // Zodiac distribution
        Map<Member.ZodiacSign, Long> zodiacDist = zodiacUtilityService.calculateZodiacDistribution(members);

        // Element balance
        Map<Member.ZodiacElement, Long> elementBalance = zodiacUtilityService.calculateElementBalance(members);
        boolean isBalanced = zodiacUtilityService.isTeamBalanced(members);
        List<String> missingElements = zodiacUtilityService.getMissingElements(members).stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        // Conflicts
        List<TeamAnalyticsResponse.ConflictPairInfo> conflicts = compatResult.potentialConflicts().stream()
                .map(pc -> {
                    Member m1 = members.stream().filter(m -> m.getId().equals(pc.member1Id())).findFirst().orElse(null);
                    Member m2 = members.stream().filter(m -> m.getId().equals(pc.member2Id())).findFirst().orElse(null);
                    return new TeamAnalyticsResponse.ConflictPairInfo(
                            m1 != null ? m1.getFullName() : "Unknown",
                            m2 != null ? m2.getFullName() : "Unknown",
                            pc.compatibilityScore(),
                            "Monitor interactions and provide clear communication"
                    );
                })
                .collect(Collectors.toList());

        // Best pairs
        List<TeamAnalyticsResponse.BestPairInfo> bestPairs = compatResult.bestPairs().stream()
                .map(bp -> {
                    Member m1 = members.stream().filter(m -> m.getId().equals(bp.member1Id())).findFirst().orElse(null);
                    Member m2 = members.stream().filter(m -> m.getId().equals(bp.member2Id())).findFirst().orElse(null);
                    ZodiacCompatibility comp = compatibilityService.getCompatibilityByMembers(bp.member1Id(), bp.member2Id());
                    return new TeamAnalyticsResponse.BestPairInfo(
                            m1 != null ? m1.getFullName() : "Unknown",
                            m2 != null ? m2.getFullName() : "Unknown",
                            bp.compatibilityScore(),
                            comp.getBestCollaborationType()
                    );
                })
                .limit(3)
                .collect(Collectors.toList());

        // Generate insights
        String teamInsight = zodiacUtilityService.generateTeamInsight(members);
        List<String> strengths = new ArrayList<>(compatResult.insights());
        List<String> challenges = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();

        if (!conflicts.isEmpty()) {
            challenges.add(String.format("%d potential conflicts detected", conflicts.size()));
            recommendations.add("Implement conflict management strategies");
        }
        if (!isBalanced) {
            challenges.add("Element imbalance in team");
            recommendations.add("Consider adding members from missing elements");
        }

        return TeamAnalyticsResponse.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .overallCompatibilityScore(compatResult.averageCompatibilityScore())
                .compatibilityLevel(compatResult.overallLevel())
                .zodiacDistribution(zodiacDist)
                .elementBalance(elementBalance)
                .isElementBalanced(isBalanced)
                .missingElements(missingElements)
                .potentialConflictCount(conflicts.size())
                .conflictPairs(conflicts)
                .topCompatiblePairs(bestPairs)
                .teamStrengths(strengths)
                .teamChallenges(challenges)
                .recommendations(recommendations)
                .teamDynamicsSummary(teamInsight)
                .build();
    }

    /**
     * Recalculate team compatibility
     */
    private void recalculateTeamCompatibility(Long teamId) {
        try {
            List<Long> memberIds = teamMemberRepository.findByTeamIdAndIsActive(teamId, true)
                    .stream()
                    .map(TeamMember::getMemberId)
                    .collect(Collectors.toList());

            if (memberIds.size() < 2) {
                Team team = teamRepository.findById(teamId).orElse(null);
                if (team != null) {
                    team.setCompatibilityScore(null);
                    team.setHasZodiacConflicts(false);
                    teamRepository.save(team);
                }
                return;
            }

            CompatibilityService.TeamCompatibilityResult result =
                    compatibilityService.calculateTeamCompatibility(memberIds);

            Team team = teamRepository.findById(teamId).orElse(null);
            if (team != null) {
                team.setCompatibilityScore(result.averageCompatibilityScore());
                team.setHasZodiacConflicts(!result.potentialConflicts().isEmpty());

                // Convert element balance
                Map<String, Integer> elementBalanceMap = new HashMap<>();
                result.elementBalance().forEach((k, v) -> elementBalanceMap.put(k.name(), v.intValue()));
                team.setElementBalance(elementBalanceMap);

                teamRepository.save(team);
            }

        } catch (Exception e) {
            log.error("Error recalculating team compatibility: {}", e.getMessage());
        }
    }

    // Helper methods
    private TeamResponse toResponse(Team team) {
        String departmentName = null;
        if (team.getDepartmentId() != null) {
            departmentName = departmentRepository.findById(team.getDepartmentId())
                    .map(Department::getName)
                    .orElse(null);
        }

        List<TeamMemberResponse> members = teamMemberRepository.findByTeamIdAndIsActive(team.getId(), true)
                .stream()
                .map(this::toTeamMemberResponse)
                .collect(Collectors.toList());

        String compatibilityLevel = null;
        if (team.getCompatibilityScore() != null) {
            if (team.getCompatibilityScore().compareTo(BigDecimal.valueOf(80)) >= 0) compatibilityLevel = "Excellent";
            else if (team.getCompatibilityScore().compareTo(BigDecimal.valueOf(65)) >= 0) compatibilityLevel = "Good";
            else if (team.getCompatibilityScore().compareTo(BigDecimal.valueOf(50)) >= 0) compatibilityLevel = "Moderate";
            else compatibilityLevel = "Challenging";
        }

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .departmentId(team.getDepartmentId())
                .departmentName(departmentName)
                .teamType(team.getTeamType().name())
                .startDate(team.getStartDate())
                .endDate(team.getEndDate())
                .status(team.getStatus().name())
                .memberCount(team.getMemberCount())
                .targetMemberCount(team.getTargetMemberCount())
                .compatibilityScore(team.getCompatibilityScore())
                .compatibilityLevel(compatibilityLevel)
                .elementBalance(team.getElementBalance())
                .hasZodiacConflicts(team.getHasZodiacConflicts())
                .members(members)
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }

    private TeamSummaryResponse toSummaryResponse(Team team) {
        String departmentName = null;
        if (team.getDepartmentId() != null) {
            departmentName = departmentRepository.findById(team.getDepartmentId())
                    .map(Department::getName)
                    .orElse(null);
        }

        return TeamSummaryResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .teamType(team.getTeamType().name())
                .status(team.getStatus().name())
                .memberCount(team.getMemberCount())
                .targetMemberCount(team.getTargetMemberCount())
                .compatibilityScore(team.getCompatibilityScore())
                .startDate(team.getStartDate())
                .endDate(team.getEndDate())
                .departmentName(departmentName)
                .build();
    }

    private TeamMemberResponse toTeamMemberResponse(TeamMember teamMember) {
        Member member = memberRepository.findById(teamMember.getMemberId()).orElse(null);

        if (member == null) {
            return null;
        }

        return TeamMemberResponse.builder()
                .id(teamMember.getId())
                .memberId(member.getId())
                .memberName(member.getFullName())
                .memberCode(member.getMemberCode())
                .zodiacSign(member.getZodiacSign().name())
                .zodiacSymbol(ZodiacCalculator.getSymbol(member.getZodiacSign()))
                .role(teamMember.getRole())
                .joinedDate(teamMember.getJoinedDate())
                .leftDate(teamMember.getLeftDate())
                .isActive(teamMember.getIsActive())
                .notes(teamMember.getNotes())
                .build();
    }

    private TeamAnalyticsResponse createEmptyTeamAnalytics(Team team) {
        return TeamAnalyticsResponse.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .overallCompatibilityScore(BigDecimal.ZERO)
                .compatibilityLevel("N/A")
                .zodiacDistribution(new HashMap<>())
                .elementBalance(new HashMap<>())
                .isElementBalanced(false)
                .missingElements(Arrays.asList("Fire", "Earth", "Air", "Water"))
                .potentialConflictCount(0)
                .conflictPairs(Collections.emptyList())
                .topCompatiblePairs(Collections.emptyList())
                .teamStrengths(Collections.emptyList())
                .teamChallenges(List.of("No active members in team"))
                .recommendations(List.of("Add members to build the team"))
                .teamDynamicsSummary("Team has no active members")
                .build();
    }
}