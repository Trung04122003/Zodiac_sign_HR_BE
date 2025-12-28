package com.jci.zodiac.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jci.zodiac.entity.*;
import com.jci.zodiac.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DataExportService - Export all system data to JSON/Excel
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataExportService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final NoteRepository noteRepository;
    private final SettingRepository settingRepository;
    private final ZodiacProfileRepository zodiacProfileRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Export all data to JSON
     */
    @Transactional(readOnly = true)
    public byte[] exportAllDataToJson() {
        log.info("Exporting all data to JSON");

        try {
            Map<String, Object> allData = new HashMap<>();

            // Metadata
            allData.put("exportDate", LocalDateTime.now());
            allData.put("version", "1.0.0");
            allData.put("system", "JCI Danang - Zodiac HR Management");

            // Export all entities
            allData.put("members", memberRepository.findAll());
            allData.put("departments", departmentRepository.findAll());
            allData.put("teams", teamRepository.findAll());
            allData.put("teamMembers", teamMemberRepository.findAll());
            allData.put("notes", noteRepository.findAll());
            allData.put("settings", settingRepository.findAll());
            allData.put("zodiacProfiles", zodiacProfileRepository.findAll());
            allData.put("zodiacCompatibility", compatibilityRepository.findAll());

            // Statistics
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalMembers", memberRepository.count());
            stats.put("totalDepartments", departmentRepository.count());
            stats.put("totalTeams", teamRepository.count());
            stats.put("totalNotes", noteRepository.count());
            stats.put("totalSettings", settingRepository.count());
            allData.put("statistics", stats);

            return objectMapper.writeValueAsBytes(allData);

        } catch (Exception e) {
            log.error("Error exporting data to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export data to JSON", e);
        }
    }

    /**
     * Export only settings to JSON
     */
    @Transactional(readOnly = true)
    public byte[] exportSettingsToJson() {
        log.info("Exporting settings to JSON");

        try {
            Map<String, Object> settingsData = new HashMap<>();
            settingsData.put("exportDate", LocalDateTime.now());
            settingsData.put("settings", settingRepository.findAll());

            return objectMapper.writeValueAsBytes(settingsData);

        } catch (Exception e) {
            log.error("Error exporting settings to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export settings to JSON", e);
        }
    }

    /**
     * Export all data to Excel
     */
    @Transactional(readOnly = true)
    public byte[] exportAllDataToExcel() {
        log.info("Exporting all data to Excel");

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Sheet 1: Members
            createMembersSheet(workbook, headerStyle, dataStyle);

            // Sheet 2: Departments
            createDepartmentsSheet(workbook, headerStyle, dataStyle);

            // Sheet 3: Teams
            createTeamsSheet(workbook, headerStyle, dataStyle);

            // Sheet 4: Settings
            createSettingsSheet(workbook, headerStyle, dataStyle);

            // Sheet 5: Statistics
            createStatisticsSheet(workbook, headerStyle, dataStyle);

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            log.error("Error exporting data to Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }

    /**
     * Get export history (mock for now - would need a separate audit table)
     */
    public List<Map<String, Object>> getExportHistory() {
        log.debug("Fetching export history");

        // Mock data - in production, this would query an audit/export_history table
        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> lastExport = new HashMap<>();
        lastExport.put("date", LocalDateTime.now().minusDays(1));
        lastExport.put("type", "JSON");
        lastExport.put("size", "2.5 MB");
        lastExport.put("status", "SUCCESS");
        history.add(lastExport);

        return history;
    }

    // ==================== Helper Methods ====================

    private void createMembersSheet(XSSFWorkbook workbook, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Members");
        List<Member> members = memberRepository.findAll();

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Code", "Name", "Email", "Zodiac Sign", "Element", "Department", "Status", "Join Date"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        int rowNum = 1;
        for (Member member : members) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(member.getId());
            row.createCell(1).setCellValue(member.getMemberCode());
            row.createCell(2).setCellValue(member.getFullName());
            row.createCell(3).setCellValue(member.getEmail());
            row.createCell(4).setCellValue(member.getZodiacSign().name());
            row.createCell(5).setCellValue(member.getZodiacElement().name());
            row.createCell(6).setCellValue(member.getDepartmentId() != null ? member.getDepartmentId().toString() : "N/A");
            row.createCell(7).setCellValue(member.getMembershipStatus().name());
            row.createCell(8).setCellValue(member.getJoinDate().toString());

            for (int i = 0; i < 9; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDepartmentsSheet(XSSFWorkbook workbook, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Departments");
        List<Department> departments = departmentRepository.findAll();

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Code", "Zodiac Theme", "Member Count", "Active", "Created"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Department dept : departments) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dept.getId());
            row.createCell(1).setCellValue(dept.getName());
            row.createCell(2).setCellValue(dept.getCode());
            row.createCell(3).setCellValue(dept.getZodiacTheme() != null ? dept.getZodiacTheme().name() : "N/A");
            row.createCell(4).setCellValue(dept.getMemberCount());
            row.createCell(5).setCellValue(dept.getIsActive() ? "Yes" : "No");
            row.createCell(6).setCellValue(dept.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

            for (int i = 0; i < 7; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createTeamsSheet(XSSFWorkbook workbook, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Teams");
        List<Team> teams = teamRepository.findAll();

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Type", "Status", "Member Count", "Compatibility", "Created"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Team team : teams) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(team.getId());
            row.createCell(1).setCellValue(team.getName());
            row.createCell(2).setCellValue(team.getTeamType().name());
            row.createCell(3).setCellValue(team.getStatus().name());
            row.createCell(4).setCellValue(team.getMemberCount());
            row.createCell(5).setCellValue(team.getCompatibilityScore() != null ? team.getCompatibilityScore().doubleValue() : 0);
            row.createCell(6).setCellValue(team.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

            for (int i = 0; i < 7; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createSettingsSheet(XSSFWorkbook workbook, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Settings");
        List<Setting> settings = settingRepository.findAll();

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Key", "Value", "Category", "Type", "Public", "Editable"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Setting setting : settings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(setting.getKey());
            row.createCell(1).setCellValue(setting.getValue());
            row.createCell(2).setCellValue(setting.getCategory().name());
            row.createCell(3).setCellValue(setting.getDataType().name());
            row.createCell(4).setCellValue(setting.getIsPublic() ? "Yes" : "No");
            row.createCell(5).setCellValue(setting.getIsEditable() ? "Yes" : "No");

            for (int i = 0; i < 6; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createStatisticsSheet(XSSFWorkbook workbook, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Statistics");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Metric");
        headerRow.createCell(1).setCellValue("Value");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.getCell(1).setCellStyle(headerStyle);

        int rowNum = 1;
        Object[][] stats = {
                {"Total Members", memberRepository.count()},
                {"Active Members", memberRepository.countActive()},
                {"Total Departments", departmentRepository.count()},
                {"Total Teams", teamRepository.count()},
                {"Total Notes", noteRepository.count()},
                {"Total Settings", settingRepository.count()},
                {"Export Date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}
        };

        for (Object[] stat : stats) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat[0].toString());
            row.createCell(1).setCellValue(stat[1].toString());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}