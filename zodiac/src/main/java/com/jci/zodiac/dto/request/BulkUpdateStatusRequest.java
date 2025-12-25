package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk updating member status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateStatusRequest {

    @NotEmpty(message = "Member IDs list cannot be empty")
    private List<Long> memberIds;

    @NotNull(message = "New status is required")
    private Member.MembershipStatus newStatus;
}