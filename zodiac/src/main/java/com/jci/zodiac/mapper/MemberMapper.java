package com.jci.zodiac.mapper;

import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.request.UpdateMemberRequest;
import com.jci.zodiac.dto.response.MemberResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.util.ZodiacCalculator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * MemberMapper - Converts between Member Entity and DTOs
 * Handles zodiac calculations and computed fields
 */
@Component
public class MemberMapper {

    // ==================== Request DTOs → Entity ====================

    /**
     * Convert CreateMemberRequest to Member entity
     * Auto-calculates zodiac sign and element from dateOfBirth
     */
    public Member toEntity(CreateMemberRequest request) {
        if (request == null) {
            return null;
        }

        Member member = Member.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .position(request.getPosition())
                .departmentId(request.getDepartmentId())
                .joinDate(request.getJoinDate())
                .membershipStatus(request.getMembershipStatus())
                .membershipType(request.getMembershipType())
                .avatarUrl(request.getAvatarUrl())
                .address(request.getAddress())
                .city(request.getCity())
                .emergencyContact(request.getEmergencyContact())
                .emergencyPhone(request.getEmergencyPhone())
                .facebookUrl(request.getFacebookUrl())
                .company("JCI Danang Junior Club")
                .notes(request.getNotes())
                .tags(request.getTags())
                .build();

        // Auto-calculate zodiac sign and element
        if (request.getDateOfBirth() != null) {
            member.setZodiacSign(ZodiacCalculator.calculateZodiacSign(request.getDateOfBirth()));
            member.setZodiacElement(ZodiacCalculator.calculateZodiacElement(request.getDateOfBirth()));
        }

        return member;
    }

    /**
     * Update existing Member entity from UpdateMemberRequest
     * Only updates non-null fields (partial update)
     */
    public void updateEntity(Member member, UpdateMemberRequest request) {
        if (request == null || member == null) {
            return;
        }

        if (request.getFullName() != null) {
            member.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            member.setDateOfBirth(request.getDateOfBirth());
            // Recalculate zodiac if DOB changed
            member.setZodiacSign(ZodiacCalculator.calculateZodiacSign(request.getDateOfBirth()));
            member.setZodiacElement(ZodiacCalculator.calculateZodiacElement(request.getDateOfBirth()));
        }
        if (request.getPosition() != null) {
            member.setPosition(request.getPosition());
        }
        if (request.getDepartmentId() != null) {
            member.setDepartmentId(request.getDepartmentId());
        }
        if (request.getJoinDate() != null) {
            member.setJoinDate(request.getJoinDate());
        }
        if (request.getMembershipStatus() != null) {
            member.setMembershipStatus(request.getMembershipStatus());
        }
        if (request.getMembershipType() != null) {
            member.setMembershipType(request.getMembershipType());
        }
        if (request.getAvatarUrl() != null) {
            member.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getAddress() != null) {
            member.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            member.setCity(request.getCity());
        }
        if (request.getEmergencyContact() != null) {
            member.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getEmergencyPhone() != null) {
            member.setEmergencyPhone(request.getEmergencyPhone());
        }
        if (request.getFacebookUrl() != null) {
            member.setFacebookUrl(request.getFacebookUrl());
        }
        if (request.getNotes() != null) {
            member.setNotes(request.getNotes());
        }
        if (request.getTags() != null) {
            member.setTags(request.getTags());
        }
    }

    // ==================== Entity → Response DTOs ====================

    /**
     * Convert Member entity to full MemberResponse
     * Includes all computed fields
     */
    public MemberResponse toResponse(Member member) {
        if (member == null) {
            return null;
        }

        return MemberResponse.builder()
                .id(member.getId())
                .memberCode(member.getMemberCode())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .dateOfBirth(member.getDateOfBirth())
                .zodiacSign(member.getZodiacSign().name())
                .zodiacElement(member.getZodiacElement().name())
                .zodiacSymbol(ZodiacCalculator.getSymbol(member.getZodiacSign()))
                .zodiacDateRange(ZodiacCalculator.getDateRange(member.getZodiacSign()))
                .elementSymbol(ZodiacCalculator.getElementSymbol(member.getZodiacElement()))
                .position(member.getPosition())
                .departmentId(member.getDepartmentId())
                .joinDate(member.getJoinDate())
                .membershipStatus(member.getMembershipStatus().name())
                .membershipType(member.getMembershipType().name())
                .avatarUrl(member.getAvatarUrl())
                .address(member.getAddress())
                .city(member.getCity())
                .emergencyContact(member.getEmergencyContact())
                .emergencyPhone(member.getEmergencyPhone())
                .facebookUrl(member.getFacebookUrl())
                .notes(member.getNotes())
                .tags(member.getTags())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .createdBy(member.getCreatedBy())
                // Computed fields
                .age(calculateAge(member.getDateOfBirth()))
                .daysSinceJoined(calculateDaysSinceJoined(member.getJoinDate()))
                .isActive(member.isActive())
                .isBirthdayToday(isBirthdayToday(member.getDateOfBirth()))
                .daysUntilBirthday(calculateDaysUntilBirthday(member.getDateOfBirth()))
                .elementDescription(getElementDescription(member.getZodiacElement()))
                .build();
    }

    /**
     * Convert Member entity to MemberSummaryResponse
     * Lightweight version for list views
     */
    public MemberSummaryResponse toSummaryResponse(Member member) {
        if (member == null) {
            return null;
        }

        return MemberSummaryResponse.builder()
                .id(member.getId())
                .memberCode(member.getMemberCode())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .position(member.getPosition())
                .zodiacSign(member.getZodiacSign().name())
                .zodiacElement(member.getZodiacElement().name())
                .zodiacSymbol(ZodiacCalculator.getSymbol(member.getZodiacSign()))
                .elementSymbol(ZodiacCalculator.getElementSymbol(member.getZodiacElement()))
                .avatarUrl(member.getAvatarUrl())
                .membershipStatus(member.getMembershipStatus().name())
                .joinDate(member.getJoinDate())
                .isActive(member.isActive())
                .departmentId(member.getDepartmentId())
                // Computed fields
                .age(calculateAge(member.getDateOfBirth()))
                .daysSinceJoined(calculateDaysSinceJoined(member.getJoinDate()))
                .isBirthdayToday(isBirthdayToday(member.getDateOfBirth()))
                .isBirthdayThisWeek(isBirthdayThisWeek(member.getDateOfBirth()))
                .build();
    }

    // ==================== Helper Methods ====================

    private Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }

    private Long calculateDaysSinceJoined(LocalDate joinDate) {
        if (joinDate == null) return null;
        return ChronoUnit.DAYS.between(joinDate, LocalDate.now());
    }

    private Boolean isBirthdayToday(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return false;
        LocalDate today = LocalDate.now();
        return dateOfBirth.getMonth() == today.getMonth() &&
                dateOfBirth.getDayOfMonth() == today.getDayOfMonth();
    }

    private Boolean isBirthdayThisWeek(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return false;
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        LocalDate birthdayThisYear = LocalDate.of(today.getYear(),
                dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());

        return !birthdayThisYear.isBefore(today) && !birthdayThisYear.isAfter(nextWeek);
    }

    private Integer calculateDaysUntilBirthday(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = LocalDate.of(today.getYear(),
                dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());

        if (nextBirthday.isBefore(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        return (int) ChronoUnit.DAYS.between(today, nextBirthday);
    }

    private String getElementDescription(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "Fire signs are passionate, dynamic, and temperamental. They get angry quickly, but they also forgive easily.";
            case Earth -> "Earth signs are grounded, practical, and reliable. They are the builders and creators of the zodiac.";
            case Air -> "Air signs are intellectual, communicative, and social. They love to analyze, synthesize, and probe.";
            case Water -> "Water signs are emotional, intuitive, and deeply connected to their subconscious.";
        };
    }
}