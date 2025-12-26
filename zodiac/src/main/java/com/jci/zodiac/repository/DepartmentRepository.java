package com.jci.zodiac.repository;

import com.jci.zodiac.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepository - Department data access
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Department> findByIsActive(Boolean isActive);

    List<Department> findByZodiacTheme(Department.ZodiacSign zodiacTheme);

    @Query("SELECT d FROM Department d WHERE d.name LIKE %:keyword% OR d.code LIKE %:keyword%")
    List<Department> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(d) FROM Department d WHERE d.isActive = true")
    long countActive();

    @Query("SELECT d FROM Department d ORDER BY d.memberCount DESC")
    List<Department> findAllOrderedByMemberCount();

    @Query("SELECT d FROM Department d WHERE d.leadMemberId = :memberId")
    List<Department> findByLeadMemberId(@Param("memberId") Long memberId);
}