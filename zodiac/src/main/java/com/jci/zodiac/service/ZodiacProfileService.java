package com.jci.zodiac.service;

import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.ZodiacProfile;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.repository.ZodiacProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ZodiacProfileService - Zodiac profile operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ZodiacProfileService {

    private final ZodiacProfileRepository zodiacProfileRepository;

    /**
     * Get all zodiac profiles (12 signs)
     */
    @Transactional(readOnly = true)
    public List<ZodiacProfile> getAllProfiles() {
        log.debug("Fetching all zodiac profiles");
        return zodiacProfileRepository.findAllOrderedByElement();
    }

    /**
     * Get profile by zodiac sign
     */
    @Transactional(readOnly = true)
    public ZodiacProfile getProfileBySign(ZodiacProfile.ZodiacSign sign) {
        log.debug("Fetching profile for sign: {}", sign);
        return zodiacProfileRepository.findByZodiacSign(sign)
                .orElseThrow(() -> new ResourceNotFoundException("ZodiacProfile", "sign", sign));
    }

    /**
     * Get profiles by element
     */
    @Transactional(readOnly = true)
    public List<ZodiacProfile> getProfilesByElement(ZodiacProfile.Element element) {
        log.debug("Fetching profiles for element: {}", element);
        return zodiacProfileRepository.findByElementOrdered(element);
    }

    /**
     * Get profiles by modality
     */
    @Transactional(readOnly = true)
    public List<ZodiacProfile> getProfilesByModality(ZodiacProfile.Modality modality) {
        log.debug("Fetching profiles for modality: {}", modality);
        return zodiacProfileRepository.findByModality(modality);
    }

    /**
     * Get profile for specific member
     */
    @Transactional(readOnly = true)
    public ZodiacProfile getProfileForMember(Member member) {
        return getProfileBySign(convertToProfileSign(member.getZodiacSign()));
    }

    /**
     * Update zodiac profile (for customization)
     */
    public ZodiacProfile updateProfile(ZodiacProfile.ZodiacSign sign, ZodiacProfile updates) {
        log.info("Updating profile for sign: {}", sign);

        ZodiacProfile profile = getProfileBySign(sign);

        // Update customizable fields
        if (updates.getPersonalityTraits() != null) {
            profile.setPersonalityTraits(updates.getPersonalityTraits());
        }
        if (updates.getStrengths() != null) {
            profile.setStrengths(updates.getStrengths());
        }
        if (updates.getWeaknesses() != null) {
            profile.setWeaknesses(updates.getWeaknesses());
        }
        if (updates.getWorkStyle() != null) {
            profile.setWorkStyle(updates.getWorkStyle());
        }
        if (updates.getBestRoles() != null) {
            profile.setBestRoles(updates.getBestRoles());
        }
        if (updates.getCommunicationStyle() != null) {
            profile.setCommunicationStyle(updates.getCommunicationStyle());
        }
        if (updates.getMotivationFactors() != null) {
            profile.setMotivationFactors(updates.getMotivationFactors());
        }
        if (updates.getStressTriggers() != null) {
            profile.setStressTriggers(updates.getStressTriggers());
        }
        if (updates.getCustomAttributes() != null) {
            profile.setCustomAttributes(updates.getCustomAttributes());
        }

        return zodiacProfileRepository.save(profile);
    }

    /**
     * Get element distribution statistics
     */
    @Transactional(readOnly = true)
    public Map<ZodiacProfile.Element, Long> getElementDistribution() {
        List<ZodiacProfile> allProfiles = zodiacProfileRepository.findAll();
        return allProfiles.stream()
                .collect(Collectors.groupingBy(
                        ZodiacProfile::getElement,
                        Collectors.counting()
                ));
    }

    /**
     * Search profiles by keyword
     */
    @Transactional(readOnly = true)
    public List<ZodiacProfile> searchProfiles(String keyword) {
        log.debug("Searching profiles with keyword: {}", keyword);
        return zodiacProfileRepository.findAll().stream()
                .filter(profile ->
                        profile.getZodiacSign().name().toLowerCase().contains(keyword.toLowerCase()) ||
                                profile.getDescriptionLong().toLowerCase().contains(keyword.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    // Helper method to convert Member.ZodiacSign to ZodiacProfile.ZodiacSign
    private ZodiacProfile.ZodiacSign convertToProfileSign(Member.ZodiacSign memberSign) {
        return ZodiacProfile.ZodiacSign.valueOf(memberSign.name());
    }
}