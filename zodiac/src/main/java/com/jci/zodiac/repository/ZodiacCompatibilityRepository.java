package com.jci.zodiac.repository;

import com.jci.zodiac.entity.ZodiacCompatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ZodiacCompatibility Repository - Compatibility Matrix Queries
 */
@Repository
public interface ZodiacCompatibilityRepository extends JpaRepository<ZodiacCompatibility, Long> {

    // ==================== Basic Queries ====================

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "(zc.zodiacSign1 = :sign1 AND zc.zodiacSign2 = :sign2) OR " +
            "(zc.zodiacSign1 = :sign2 AND zc.zodiacSign2 = :sign1)")
    Optional<ZodiacCompatibility> findBySignPair(
            @Param("sign1") ZodiacCompatibility.ZodiacSign sign1,
            @Param("sign2") ZodiacCompatibility.ZodiacSign sign2
    );

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.zodiacSign1 = :sign OR zc.zodiacSign2 = :sign " +
            "ORDER BY zc.overallScore DESC")
    List<ZodiacCompatibility> findAllForSign(@Param("sign") ZodiacCompatibility.ZodiacSign sign);

    // ==================== High/Low Compatibility ====================

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.overallScore >= :minScore ORDER BY zc.overallScore DESC")
    List<ZodiacCompatibility> findHighCompatibilityPairs(@Param("minScore") BigDecimal minScore);

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.overallScore < :maxScore ORDER BY zc.overallScore ASC")
    List<ZodiacCompatibility> findLowCompatibilityPairs(@Param("maxScore") BigDecimal maxScore);

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.compatibilityLevel = :level ORDER BY zc.overallScore DESC")
    List<ZodiacCompatibility> findByCompatibilityLevel(
            @Param("level") ZodiacCompatibility.CompatibilityLevel level
    );

    // ==================== Best Matches ====================

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "(zc.zodiacSign1 = :sign OR zc.zodiacSign2 = :sign) " +
            "ORDER BY zc.overallScore DESC")
    List<ZodiacCompatibility> findBestMatchesForSign(
            @Param("sign") ZodiacCompatibility.ZodiacSign sign
    );

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.compatibilityLevel = 'Excellent' ORDER BY zc.overallScore DESC")
    List<ZodiacCompatibility> findAllExcellentMatches();

    // ==================== Conflict Detection ====================

    @Query("SELECT zc FROM ZodiacCompatibility zc WHERE " +
            "zc.conflictPotential >= :threshold ORDER BY zc.conflictPotential DESC")
    List<ZodiacCompatibility> findHighConflictPairs(@Param("threshold") BigDecimal threshold);

    // ==================== Statistics ====================

    @Query("SELECT AVG(zc.overallScore) FROM ZodiacCompatibility zc")
    BigDecimal getAverageCompatibilityScore();

    @Query("SELECT COUNT(zc) FROM ZodiacCompatibility zc WHERE zc.overallScore >= :minScore")
    long countHighCompatibilityPairs(@Param("minScore") BigDecimal minScore);
}