package com.jci.zodiac.repository;

import com.jci.zodiac.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByStatus(Team.Status status);

    List<Team> findByTeamType(Team.TeamType teamType);

    List<Team> findByDepartmentId(Long departmentId);

    @Query("SELECT t FROM Team t WHERE t.name LIKE %:keyword%")
    List<Team> searchByName(@Param("keyword") String keyword);

    @Query("SELECT t FROM Team t WHERE t.status = 'Active' ORDER BY t.createdAt DESC")
    List<Team> findActiveTeams();

    @Query("SELECT t FROM Team t WHERE t.endDate < :date AND t.status = 'Active'")
    List<Team> findOverdueTeams(@Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Team t WHERE t.status = :status")
    long countByStatus(@Param("status") Team.Status status);

    @Query("SELECT t FROM Team t WHERE t.hasZodiacConflicts = true")
    List<Team> findTeamsWithConflicts();
}