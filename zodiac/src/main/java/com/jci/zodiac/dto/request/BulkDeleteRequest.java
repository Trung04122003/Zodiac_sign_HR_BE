package com.jci.zodiac.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk deleting members
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkDeleteRequest {

    @NotEmpty(message = "Member IDs list cannot be empty")
    private List<Long> memberIds;

    private boolean permanent = false; // Soft delete by default
}