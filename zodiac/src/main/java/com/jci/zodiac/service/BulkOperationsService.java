package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.BulkDeleteRequest;
import com.jci.zodiac.dto.request.BulkMemberRequest;
import com.jci.zodiac.dto.request.BulkUpdateStatusRequest;
import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.response.BulkOperationResponse;
import com.jci.zodiac.dto.response.MemberResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BulkOperationsService - Handle bulk member operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BulkOperationsService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * Bulk create members
     */
    @Transactional
    public BulkOperationResponse bulkCreateMembers(BulkMemberRequest request) {
        log.info("Bulk creating {} members", request.getMembers().size());

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        List<MemberResponse> createdMembers = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (int i = 0; i < request.getMembers().size(); i++) {
            CreateMemberRequest memberRequest = request.getMembers().get(i);
            try {
                MemberResponse created = memberService.createMember(memberRequest);
                createdMembers.add(created);
                successMessages.add(String.format("Row %d: Created %s (%s)",
                        i + 1, created.getFullName(), created.getMemberCode()));
                successCount++;
            } catch (Exception e) {
                errorMessages.add(String.format("Row %d: Failed to create %s - %s",
                        i + 1, memberRequest.getFullName(), e.getMessage()));
                failureCount++;
                log.error("Failed to create member at row {}: {}", i + 1, e.getMessage());
            }
        }

        Map<String, Object> details = new HashMap<>();
        details.put("createdMembers", createdMembers);

        return BulkOperationResponse.builder()
                .totalRequested(request.getMembers().size())
                .successCount(successCount)
                .failureCount(failureCount)
                .successMessages(successMessages)
                .errorMessages(errorMessages)
                .details(details)
                .build();
    }

    /**
     * Bulk update member status
     */
    @Transactional
    public BulkOperationResponse bulkUpdateStatus(BulkUpdateStatusRequest request) {
        log.info("Bulk updating status for {} members to {}",
                request.getMemberIds().size(), request.getNewStatus());

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (Long memberId : request.getMemberIds()) {
            try {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

                Member.MembershipStatus oldStatus = member.getMembershipStatus();
                member.setMembershipStatus(request.getNewStatus());
                memberRepository.save(member);

                successMessages.add(String.format("Member %s (%s): %s → %s",
                        member.getMemberCode(), member.getFullName(), oldStatus, request.getNewStatus()));
                successCount++;
            } catch (Exception e) {
                errorMessages.add(String.format("Member ID %d: %s", memberId, e.getMessage()));
                failureCount++;
                log.error("Failed to update status for member {}: {}", memberId, e.getMessage());
            }
        }

        return BulkOperationResponse.builder()
                .totalRequested(request.getMemberIds().size())
                .successCount(successCount)
                .failureCount(failureCount)
                .successMessages(successMessages)
                .errorMessages(errorMessages)
                .build();
    }

    /**
     * Bulk delete members
     */
    @Transactional
    public BulkOperationResponse bulkDeleteMembers(BulkDeleteRequest request) {
        log.info("Bulk deleting {} members (permanent: {})",
                request.getMemberIds().size(), request.isPermanent());

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (Long memberId : request.getMemberIds()) {
            try {
                if (request.isPermanent()) {
                    memberService.permanentlyDeleteMember(memberId);
                    successMessages.add(String.format("Member ID %d: Permanently deleted", memberId));
                } else {
                    memberService.deleteMember(memberId);
                    successMessages.add(String.format("Member ID %d: Soft deleted (status changed to Inactive)", memberId));
                }
                successCount++;
            } catch (Exception e) {
                errorMessages.add(String.format("Member ID %d: %s", memberId, e.getMessage()));
                failureCount++;
                log.error("Failed to delete member {}: {}", memberId, e.getMessage());
            }
        }

        return BulkOperationResponse.builder()
                .totalRequested(request.getMemberIds().size())
                .successCount(successCount)
                .failureCount(failureCount)
                .successMessages(successMessages)
                .errorMessages(errorMessages)
                .build();
    }

    /**
     * Bulk update department
     */
    @Transactional
    public BulkOperationResponse bulkUpdateDepartment(List<Long> memberIds, Long newDepartmentId) {
        log.info("Bulk updating department for {} members to department {}",
                memberIds.size(), newDepartmentId);

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (Long memberId : memberIds) {
            try {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

                member.setDepartmentId(newDepartmentId);
                memberRepository.save(member);

                successMessages.add(String.format("Member %s (%s): Department updated",
                        member.getMemberCode(), member.getFullName()));
                successCount++;
            } catch (Exception e) {
                errorMessages.add(String.format("Member ID %d: %s", memberId, e.getMessage()));
                failureCount++;
                log.error("Failed to update department for member {}: {}", memberId, e.getMessage());
            }
        }

        return BulkOperationResponse.builder()
                .totalRequested(memberIds.size())
                .successCount(successCount)
                .failureCount(failureCount)
                .successMessages(successMessages)
                .errorMessages(errorMessages)
                .build();
    }
}