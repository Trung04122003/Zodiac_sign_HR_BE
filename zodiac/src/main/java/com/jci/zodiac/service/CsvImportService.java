package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.CreateMemberRequest;
import com.jci.zodiac.dto.request.ImportMembersRequest;
import com.jci.zodiac.dto.response.ImportResult;
import com.jci.zodiac.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvImportService - Import members from CSV files
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CsvImportService {

    private final MemberService memberService;

    /**
     * Import members from CSV file
     */
    public ImportResult importFromCsv(MultipartFile file, ImportMembersRequest config) {
        log.info("Importing members from CSV file: {}", file.getOriginalFilename());

        ImportResult result = ImportResult.builder()
                .totalRows(0)
                .successfulImports(0)
                .failedImports(0)
                .skippedRows(0)
                .importedMembers(new ArrayList<>())
                .errors(new ArrayList<>())
                .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowNumber = 0;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(config.getDateFormat());

            while ((line = reader.readLine()) != null) {
                rowNumber++;

                // Skip header row
                if (config.isSkipHeader() && rowNumber == 1) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    continue;
                }

                result.setTotalRows(result.getTotalRows() + 1);

                try {
                    String[] columns = line.split(",");

                    // Validate minimum columns
                    if (columns.length < 6) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber,
                                "row",
                                line,
                                "Insufficient columns. Expected at least 6 columns."
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        continue;
                    }

                    // Parse data
                    String fullName = getColumn(columns, config.getFullNameColumn());
                    String email = getColumn(columns, config.getEmailColumn());
                    String phone = getColumn(columns, config.getPhoneColumn());
                    String dobStr = getColumn(columns, config.getDateOfBirthColumn());
                    String position = getColumn(columns, config.getPositionColumn());
                    String joinDateStr = getColumn(columns, config.getJoinDateColumn());
                    String occupation = getColumn(columns, config.getOccupationColumn());

                    // Validate required fields
                    if (fullName == null || fullName.trim().isEmpty()) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber, "fullName", fullName, "Full name is required"
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        continue;
                    }

                    // Parse dates
                    LocalDate dateOfBirth = null;
                    LocalDate joinDate = null;

                    try {
                        if (dobStr != null && !dobStr.trim().isEmpty()) {
                            dateOfBirth = LocalDate.parse(dobStr.trim(), dateFormatter);
                        }
                    } catch (DateTimeParseException e) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber, "dateOfBirth", dobStr, "Invalid date format. Expected: " + config.getDateFormat()
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        continue;
                    }

                    try {
                        if (joinDateStr != null && !joinDateStr.trim().isEmpty()) {
                            joinDate = LocalDate.parse(joinDateStr.trim(), dateFormatter);
                        } else {
                            joinDate = LocalDate.now(); // Default to today
                        }
                    } catch (DateTimeParseException e) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber, "joinDate", joinDateStr, "Invalid date format. Expected: " + config.getDateFormat()
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        continue;
                    }

                    if (dateOfBirth == null) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber, "dateOfBirth", dobStr, "Date of birth is required"
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        continue;
                    }

                    // Validate email uniqueness if required
                    if (config.isValidateEmails() && !config.isAllowDuplicates()) {
                        if (email != null && !email.trim().isEmpty()) {
                            // Check if email exists (would be done in service layer)
                            // For now, we let the service handle it
                        }
                    }

                    // Create member request
                    CreateMemberRequest memberRequest = CreateMemberRequest.builder()
                            .fullName(fullName.trim())
                            .email(email != null && !email.trim().isEmpty() ? email.trim() : null)
                            .phone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null)
                            .dateOfBirth(dateOfBirth)
                            .position(position != null && !position.trim().isEmpty() ? position.trim() : null)
                            .joinDate(joinDate)
                            .position(occupation != null && !occupation.trim().isEmpty() ? occupation.trim() : null)
                            .city("Da Nang")
                            .build();

                    // Create member
                    try {
                        MemberResponse created = memberService.createMember(memberRequest);
                        result.getImportedMembers().add(created);
                        result.setSuccessfulImports(result.getSuccessfulImports() + 1);
                        log.debug("Imported member: {} from row {}", created.getFullName(), rowNumber);
                    } catch (Exception e) {
                        result.getErrors().add(new ImportResult.ImportError(
                                rowNumber, "member", fullName, e.getMessage()
                        ));
                        result.setFailedImports(result.getFailedImports() + 1);
                        log.error("Failed to import member at row {}: {}", rowNumber, e.getMessage());
                    }

                } catch (Exception e) {
                    result.getErrors().add(new ImportResult.ImportError(
                            rowNumber, "row", line, "Unexpected error: " + e.getMessage()
                    ));
                    result.setFailedImports(result.getFailedImports() + 1);
                    log.error("Unexpected error at row {}: {}", rowNumber, e.getMessage());
                }
            }

            log.info("CSV import completed. Success: {}, Failed: {}, Skipped: {}",
                    result.getSuccessfulImports(), result.getFailedImports(), result.getSkippedRows());

        } catch (Exception e) {
            log.error("Failed to read CSV file: {}", e.getMessage());
            result.getErrors().add(new ImportResult.ImportError(
                    0, "file", file.getOriginalFilename(), "Failed to read file: " + e.getMessage()
            ));
        }

        return result;
    }

    /**
     * Safely get column value
     */
    private String getColumn(String[] columns, int index) {
        if (index < 0 || index >= columns.length) {
            return null;
        }
        String value = columns[index].trim();
        return value.isEmpty() ? null : value;
    }

    /**
     * Generate CSV template for download
     */
    public String generateCsvTemplate() {
        return "Full Name,Email,Phone,Date of Birth (yyyy-MM-dd),Position,Join Date (yyyy-MM-dd),Occupation\n" +
                "Nguyễn Văn A,nguyenvana@gmail.com,0905123456,2000-11-25,Member,2024-01-15,Software Engineer\n" +
                "Trần Thị B,tranthib@gmail.com,0905234567,1998-03-15,Board Member,2023-06-10,Business Analyst";
    }
}