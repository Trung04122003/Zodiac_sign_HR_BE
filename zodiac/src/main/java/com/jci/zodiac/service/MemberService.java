package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.request.MemberSearchRequest;
import com.jci.zodiac.dto.request.UpdateMemberRequest;
import com.jci.zodiac.dto.response.MemberResponse;
import com.jci.zodiac.dto.response.MemberSummaryResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.exception.DuplicateResourceException;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.mapper.MemberMapper;
import com.jci.zodiac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MemberService - Business logic for member operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ZodiacUtilityService zodiacUtilityService;

    /**
     * Create a new member
     */
    public MemberResponse createMember(CreateMemberRequest request) {
        log.info("Creating new member: {}", request.getFullName());

        // Validate email uniqueness
        if (request.getEmail() != null && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Member", "email", request.getEmail());
        }

        // Validate date of birth
        if (!zodiacUtilityService.isValidDateOfBirth(request.getDateOfBirth())) {
            throw new IllegalArgumentException("Invalid date of birth. Member must be at least 18 years old.");
        }

        // Convert DTO to entity
        Member member = memberMapper.toEntity(request);

        // Generate unique member code
        long nextSequence = memberRepository.count() + 1;
        String memberCode = zodiacUtilityService.generateMemberCode(nextSequence);

        // Ensure uniqueness
        while (memberRepository.existsByMemberCode(memberCode)) {
            nextSequence++;
            memberCode = zodiacUtilityService.generateMemberCode(nextSequence);
        }
        member.setMemberCode(memberCode);

        // Save member
        Member savedMember = memberRepository.save(member);

        log.info("Member created successfully: {} ({})", savedMember.getFullName(), savedMember.getMemberCode());

        return memberMapper.toResponse(savedMember);
    }

    /**
     * Get member by ID
     */
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        log.debug("Fetching member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        return memberMapper.toResponse(member);
    }

    /**
     * Get member by member code
     */
    @Transactional(readOnly = true)
    public MemberResponse getMemberByCode(String memberCode) {
        log.debug("Fetching member with code: {}", memberCode);

        Member member = memberRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "memberCode", memberCode));

        return memberMapper.toResponse(member);
    }

    /**
     * Get all members (paginated)
     */
    @Transactional(readOnly = true)
    public Page<MemberSummaryResponse> getAllMembers(int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching all members - page: {}, size: {}", page, size);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Member> memberPage = memberRepository.findAll(pageable);

        return memberPage.map(memberMapper::toSummaryResponse);
    }

    /**
     * Search and filter members
     */
    @Transactional(readOnly = true)
    public Page<MemberSummaryResponse> searchMembers(MemberSearchRequest searchRequest) {
        log.debug("Searching members with filters: {}", searchRequest);

        Sort sort = searchRequest.getSortDirection().equalsIgnoreCase("ASC")
                ? Sort.by(searchRequest.getSortBy()).ascending()
                : Sort.by(searchRequest.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                sort
        );

        Page<Member> memberPage;

        // Apply filters
        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            memberPage = memberRepository.searchByKeyword(searchRequest.getKeyword(), pageable);
        } else if (searchRequest.getZodiacSign() != null) {
            memberPage = memberRepository.findAll(
                    (root, query, cb) -> cb.equal(root.get("zodiacSign"), searchRequest.getZodiacSign()),
                    pageable
            );
        } else if (searchRequest.getZodiacElement() != null) {
            memberPage = memberRepository.findAll(
                    (root, query, cb) -> cb.equal(root.get("zodiacElement"), searchRequest.getZodiacElement()),
                    pageable
            );
        } else if (searchRequest.getMembershipStatus() != null) {
            memberPage = memberRepository.findByMembershipStatus(searchRequest.getMembershipStatus(), pageable);
        } else if (searchRequest.getDepartmentId() != null) {
            memberPage = memberRepository.findAll(
                    (root, query, cb) -> cb.equal(root.get("departmentId"), searchRequest.getDepartmentId()),
                    pageable
            );
        } else {
            memberPage = memberRepository.findAll(pageable);
        }

        return memberPage.map(memberMapper::toSummaryResponse);
    }

    /**
     * Update member
     */
    public MemberResponse updateMember(Long id, UpdateMemberRequest request) {
        log.info("Updating member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Validate email uniqueness if changed
        if (request.getEmail() != null && !request.getEmail().equals(member.getEmail())) {
            if (memberRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Member", "email", request.getEmail());
            }
        }

        // Validate date of birth if changed
        if (request.getDateOfBirth() != null && !zodiacUtilityService.isValidDateOfBirth(request.getDateOfBirth())) {
            throw new IllegalArgumentException("Invalid date of birth. Member must be at least 18 years old.");
        }

        // Update entity
        memberMapper.updateEntity(member, request);

        // Save updated member
        Member updatedMember = memberRepository.save(member);

        log.info("Member updated successfully: {}", updatedMember.getMemberCode());

        return memberMapper.toResponse(updatedMember);
    }

    /**
     * Delete member (soft delete by changing status)
     */
    public void deleteMember(Long id) {
        log.info("Deleting member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Soft delete - change status to Inactive
        member.setMembershipStatus(Member.MembershipStatus.Inactive);
        memberRepository.save(member);

        log.info("Member soft deleted: {}", member.getMemberCode());
    }

    /**
     * Permanently delete member (hard delete)
     */
    public void permanentlyDeleteMember(Long id) {
        log.warn("Permanently deleting member with id: {}", id);

        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member", "id", id);
        }

        memberRepository.deleteById(id);

        log.info("Member permanently deleted with id: {}", id);
    }

    /**
     * Get active members only
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getActiveMembers() {
        log.debug("Fetching active members");

        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.Active);

        return activeMembers.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get members by zodiac sign
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getMembersByZodiacSign(Member.ZodiacSign sign) {
        log.debug("Fetching members with zodiac sign: {}", sign);

        List<Member> members = memberRepository.findByZodiacSign(sign);

        return members.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get members by zodiac element
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getMembersByZodiacElement(Member.ZodiacElement element) {
        log.debug("Fetching members with zodiac element: {}", element);

        List<Member> members = memberRepository.findByZodiacElement(element);

        return members.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get members by department
     */
    @Transactional(readOnly = true)
    public List<MemberSummaryResponse> getMembersByDepartment(Long departmentId) {
        log.debug("Fetching members in department: {}", departmentId);

        List<Member> members = memberRepository.findByDepartmentId(departmentId);

        return members.stream()
                .map(memberMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Count total members
     */
    @Transactional(readOnly = true)
    public long countTotalMembers() {
        return memberRepository.count();
    }

    /**
     * Count active members
     */
    @Transactional(readOnly = true)
    public long countActiveMembers() {
        return memberRepository.countActive();
    }
}