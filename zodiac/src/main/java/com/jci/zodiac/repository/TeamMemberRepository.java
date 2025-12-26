package com.jci.zodiac.repository;

import com.jci.zodiac.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeamId(Long teamId);

    List<TeamMember> findByMemberId(Long memberId);

    List<TeamMember> findByTeamIdAndIsActive(Long teamId, Boolean isActive);

    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);

    boolean existsByTeamIdAndMemberIdAndIsActive(Long teamId, Long memberId, Boolean isActive);

    @Query("SELECT COUNT(tm) FROM TeamMember tm WHERE tm.teamId = :teamId AND tm.isActive = true")
    long countActiveByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.memberId = :memberId AND tm.isActive = true")
    List<TeamMember> findActiveMembershipsByMember(@Param("memberId") Long memberId);

    @Query("SELECT tm.role, COUNT(tm) FROM TeamMember tm WHERE tm.teamId = :teamId AND tm.isActive = true GROUP BY tm.role")
    List<Object[]> countMembersByRole(@Param("teamId") Long teamId);
}