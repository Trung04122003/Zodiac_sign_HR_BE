package com.jci.zodiac.repository;

import com.jci.zodiac.entity.ZodiacProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ZodiacProfile Repository - 12 Zodiac Signs Master Data
 */
@Repository
public interface ZodiacProfileRepository extends JpaRepository<ZodiacProfile, Long> {

    Optional<ZodiacProfile> findByZodiacSign(ZodiacProfile.ZodiacSign zodiacSign);

    List<ZodiacProfile> findByElement(ZodiacProfile.Element element);

    List<ZodiacProfile> findByModality(ZodiacProfile.Modality modality);

    @Query("SELECT zp FROM ZodiacProfile zp WHERE zp.element = :element " +
            "ORDER BY zp.zodiacSign")
    List<ZodiacProfile> findByElementOrdered(@Param("element") ZodiacProfile.Element element);

    @Query("SELECT zp FROM ZodiacProfile zp ORDER BY zp.element, zp.zodiacSign")
    List<ZodiacProfile> findAllOrderedByElement();

    boolean existsByZodiacSign(ZodiacProfile.ZodiacSign zodiacSign);
}