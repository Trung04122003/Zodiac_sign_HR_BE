package com.jci.zodiac.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for CSV/Excel import configuration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportMembersRequest {

    private boolean skipHeader = true;
    private boolean validateEmails = true;
    private boolean allowDuplicates = false;
    private String dateFormat = "yyyy-MM-dd";

    // Column mapping (0-indexed)
    @Builder.Default
    private int fullNameColumn = 0;

    @Builder.Default
    private int emailColumn = 1;

    @Builder.Default
    private int phoneColumn = 2;

    @Builder.Default
    private int dateOfBirthColumn = 3;

    @Builder.Default
    private int positionColumn = 4;

    @Builder.Default
    private int joinDateColumn = 5;

    @Builder.Default
    private int occupationColumn = 6;
}