package com.jci.zodiac.repository;

import com.jci.zodiac.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

/**
 * Member Repository - JCI Members CRUD & Queries
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member> {

    // ==================== Basic Queries ====================

    Optional<Member> findByMemberCode(String memberCode);

    Optional<Member> findByEmail(String email);

    boolean existsByMemberCode(String memberCode);

    boolean existsByEmail(String email);

    // ==================== Search & Filter ====================

    List<Member> findByZodiacSign(Member.ZodiacSign zodiacSign);

    List<Member> findByZodiacElement(Member.ZodiacElement zodiacElement);

    List<Member> findByMembershipStatus(Member.MembershipStatus status);

    Page<Member> findByMembershipStatus(Member.MembershipStatus status, Pageable pageable);

    List<Member> findByDepartmentId(Long departmentId);

    @Query("SELECT m FROM Member m WHERE m.fullName LIKE %:keyword% " +
            "OR m.email LIKE %:keyword% OR m.position LIKE %:keyword%")
    List<Member> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT m FROM Member m WHERE m.fullName LIKE %:keyword% " +
            "OR m.email LIKE %:keyword% OR m.position LIKE %:keyword%")
    Page<Member> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // ==================== Zodiac Queries ====================

    @Query("SELECT m.zodiacSign, COUNT(m) FROM Member m GROUP BY m.zodiacSign")
    List<Object[]> countByZodiacSign();

    @Query("SELECT m.zodiacElement, COUNT(m) FROM Member m GROUP BY m.zodiacElement")
    List<Object[]> countByZodiacElement();

    @Query("SELECT m FROM Member m WHERE m.zodiacSign = :sign AND m.membershipStatus = 'Active'")
    List<Member> findActiveByZodiacSign(@Param("sign") Member.ZodiacSign sign);

    @Query("SELECT m FROM Member m WHERE m.zodiacElement = :element AND m.membershipStatus = 'Active'")
    List<Member> findActiveByZodiacElement(@Param("element") Member.ZodiacElement element);

    // ==================== Department Queries ====================

    @Query("SELECT COUNT(m) FROM Member m WHERE m.departmentId = :departmentId " +
            "AND m.membershipStatus = 'Active'")
    long countActiveByDepartmentId(@Param("departmentId") Long departmentId);

    // ==================== Birthday Queries ====================

    /**
     * Find members with birthday on specific month and day
     */
    @Query("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.dateOfBirth) = :month " +
            "AND FUNCTION('DAY', m.dateOfBirth) = :day " +
            "AND m.membershipStatus = 'Active'")
    List<Member> findByBirthday(@Param("month") int month, @Param("day") int day);

    @Query("SELECT m FROM Member m WHERE m.dateOfBirth BETWEEN :startDate AND :endDate " +
            "ORDER BY FUNCTION('MONTH', m.dateOfBirth), FUNCTION('DAY', m.dateOfBirth)")
    List<Member> findUpcomingBirthdays(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    /**
     * Find members with birthdays between two dates
     */
    @Query("SELECT m FROM Member m WHERE " +
            "(FUNCTION('MONTH', m.dateOfBirth) > :startMonth OR " +
            "(FUNCTION('MONTH', m.dateOfBirth) = :startMonth AND FUNCTION('DAY', m.dateOfBirth) >= :startDay)) " +
            "AND (FUNCTION('MONTH', m.dateOfBirth) < :endMonth OR " +
            "(FUNCTION('MONTH', m.dateOfBirth) = :endMonth AND FUNCTION('DAY', m.dateOfBirth) <= :endDay)) " +
            "AND m.membershipStatus = 'Active' " +
            "ORDER BY FUNCTION('MONTH', m.dateOfBirth), FUNCTION('DAY', m.dateOfBirth)")
    List<Member> findUpcomingBirthdaysInRange(@Param("startMonth") int startMonth,
                                              @Param("startDay") int startDay,
                                              @Param("endMonth") int endMonth,
                                              @Param("endDay") int endDay);

    /**
     * Find members with birthdays in a specific month
     */
    @Query("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.dateOfBirth) = :month " +
            "AND m.membershipStatus = 'Active' " +
            "ORDER BY FUNCTION('DAY', m.dateOfBirth)")
    List<Member> findByBirthMonth(@Param("month") int month);

    /**
     * Find members with birthdays today
     * Used by: BirthdayNotificationService.checkBirthdaysToday()
     */
    default List<Member> findMembersWithBirthdayToday() {
        LocalDate today = LocalDate.now();
        return findByBirthday(today.getMonthValue(), today.getDayOfMonth());
    }

    /**
     * Find members with upcoming birthdays in next N days
     * Used by: BirthdayNotificationService.checkUpcomingBirthdays()
     */
    default List<Member> findMembersWithUpcomingBirthdays(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);

        // Handle year wrap-around (e.g., Dec 25 + 10 days = Jan 4)
        if (endDate.getYear() > today.getYear()) {
            // Split into two queries: rest of this year + beginning of next year
            List<Member> result = findUpcomingBirthdaysInRange(
                    today.getMonthValue(), today.getDayOfMonth(),
                    12, 31
            );
            result.addAll(findUpcomingBirthdaysInRange(
                    1, 1,
                    endDate.getMonthValue(), endDate.getDayOfMonth()
            ));
            return result;
        } else {
            return findUpcomingBirthdaysInRange(
                    today.getMonthValue(), today.getDayOfMonth(),
                    endDate.getMonthValue(), endDate.getDayOfMonth()
            );
        }
    }

    /**
     * Find members with birthdays in a specific month
     * Used by: BirthdayNotificationService.monthlyBirthdayReport()
     */
    default List<Member> findMembersByBirthMonth(Month month) {
        return findByBirthMonth(month.getValue());
    }

    // ==================== Statistics ====================

    @Query("SELECT COUNT(m) FROM Member m WHERE m.membershipStatus = 'Active'")
    long countActive();

    @Query("SELECT m.zodiacSign FROM Member m GROUP BY m.zodiacSign " +
            "ORDER BY COUNT(m) DESC")
    List<Member.ZodiacSign> findMostCommonZodiacSigns();

    @Query("SELECT m FROM Member m WHERE m.joinDate >= :date AND m.membershipStatus = 'Active'")
    List<Member> findRecentJoins(@Param("date") LocalDate date);
}