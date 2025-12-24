package com.jci.zodiac.mapper;

import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.request.UpdateMemberRequest;
import com.jci.zodiac.dto.response.MemberResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.util.ZodiacCalculator;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Member entity and DTOs
 */
@Component
public class MemberMapper {

    /**
     * Convert CreateMemberRequest to Member entity
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
                .occupation(request.getOccupation())
                .company(request.getCompany())
                .linkedinUrl(request.getLinkedinUrl())
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
     * Update Member entity from UpdateMemberRequest
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
        if (request.getOccupation() != null) {
            member.setOccupation(request.getOccupation());
        }
        if (request.getCompany() != null) {
            member.setCompany(request.getCompany());
        }
        if (request.getLinkedinUrl() != null) {
            member.setLinkedinUrl(request.getLinkedinUrl());
        }
        if (request.getNotes() != null) {
            member.setNotes(request.getNotes());
        }
        if (request.getTags() != null) {
            member.setTags(request.getTags());
        }
    }

    /**
     * Convert Member entity to MemberResponse (full details)
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
                .occupation(member.getOccupation())
                .company(member.getCompany())
                .linkedinUrl(member.getLinkedinUrl())
                .notes(member.getNotes())
                .tags(member.getTags())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .age(member.getAge())
                .daysSinceJoined(member.getDaysSinceJoined())
                .isActive(member.isActive())
                .build();
    }

    /**
     * Convert Member entity to MemberSummaryResponse (list view)
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
                .zodiacSymbol(ZodiacCalculator.getSymbol(member.getZodiacSign()))
                .zodiacElement(member.getZodiacElement().name())
                .avatarUrl(member.getAvatarUrl())
                .membershipStatus(member.getMembershipStatus().name())
                .joinDate(member.getJoinDate())
                .isActive(member.isActive())
                .build();
    }
}