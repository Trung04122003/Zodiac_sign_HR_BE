package com.jci.zodiac.repository;

import com.jci.zodiac.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SettingRepository - Data access for system settings
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    // ==================== Basic Queries ====================

    Optional<Setting> findByKey(String key);

    boolean existsByKey(String key);

    List<Setting> findByCategory(Setting.Category category);

    @Query("SELECT s FROM Setting s WHERE s.isPublic = true")
    List<Setting> findAllPublicSettings();

    @Query("SELECT s FROM Setting s WHERE s.isEditable = true")
    List<Setting> findAllEditableSettings();

    // ==================== Search ====================

    @Query("SELECT s FROM Setting s WHERE s.key LIKE %:keyword% OR s.description LIKE %:keyword%")
    List<Setting> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT s FROM Setting s WHERE s.category = :category AND s.isPublic = true")
    List<Setting> findPublicByCategory(@Param("category") Setting.Category category);

    // ==================== Specific Settings Queries ====================

    @Query("SELECT s FROM Setting s WHERE s.key LIKE :prefix%")
    List<Setting> findByKeyPrefix(@Param("prefix") String prefix);

    @Query("SELECT s.value FROM Setting s WHERE s.key = :key")
    Optional<String> findValueByKey(@Param("key") String key);

    // ==================== Category Statistics ====================

    @Query("SELECT s.category, COUNT(s) FROM Setting s GROUP BY s.category")
    List<Object[]> countByCategory();

    @Query("SELECT COUNT(s) FROM Setting s WHERE s.category = :category")
    long countByCategory(@Param("category") Setting.Category category);
}