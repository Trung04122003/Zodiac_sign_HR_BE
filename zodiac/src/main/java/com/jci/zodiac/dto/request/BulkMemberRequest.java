package com.jci.zodiac.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk creating members
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkMemberRequest {

    @NotEmpty(message = "Member list cannot be empty")
    @Valid
    private List<CreateMemberRequest> members;
}