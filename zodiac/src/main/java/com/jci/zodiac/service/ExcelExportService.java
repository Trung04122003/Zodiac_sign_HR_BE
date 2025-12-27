package com.jci.zodiac.service;

import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * ExcelExportService - Generate Excel reports using Apache POI
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportService {

    /**
     * Generate Excel for Zodiac Analytics Report
     */
    public byte[] generateZodiacAnalyticsExcel(ZodiacAnalyticsResponse data) throws IOException {
        log.info("Generating Zodiac Analytics Excel");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Sheet 1: Overview
            XSSFSheet overviewSheet = workbook.createSheet("Overview");
            int rowNum = 0;

            // Title
            Row titleRow = overviewSheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("♐ Zodiac Analytics Report");
            titleCell.setCellStyle(titleStyle);
            overviewSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            rowNum++;

            // Metadata
            addDataRow(overviewSheet, rowNum++, "Generated Date:", data.getGeneratedAt().toString(), headerStyle, dataStyle);
            addDataRow(overviewSheet, rowNum++, "Total Members:", String.valueOf(data.getTotalMembers()), headerStyle, dataStyle);
            addDataRow(overviewSheet, rowNum++, "Most Common Sign:", data.getMostCommonSign(), headerStyle, dataStyle);
            addDataRow(overviewSheet, rowNum++, "Least Common Sign:", data.getLeastCommonSign(), headerStyle, dataStyle);
            addDataRow(overviewSheet, rowNum++, "Preferred Element:", data.getPreferredElement(), headerStyle, dataStyle);
            rowNum++;

            // Zodiac Distribution Table
            addTableHeader(overviewSheet, rowNum++, new String[]{"Zodiac Sign", "Count", "Percentage"}, headerStyle);

            for (Map.Entry<Member.ZodiacSign, Long> entry : data.getZodiacDistribution().entrySet()) {
                if (entry.getValue() > 0) {
                    Row row = overviewSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(entry.getKey().name());
                    row.createCell(1).setCellValue(entry.getValue());
                    row.createCell(2).setCellValue(String.format("%.1f%%",
                            (entry.getValue() * 100.0 / data.getTotalMembers())));

                    for (int i = 0; i < 3; i++) {
                        row.getCell(i).setCellStyle(dataStyle);
                    }
                }
            }
            rowNum++;

            // Element Distribution Table
            addTableHeader(overviewSheet, rowNum++, new String[]{"Element", "Count"}, headerStyle);

            for (Map.Entry<Member.ZodiacElement, Long> entry : data.getElementDistribution().entrySet()) {
                Row row = overviewSheet.createRow(rowNum++);
                String emoji = getElementEmoji(entry.getKey());
                row.createCell(0).setCellValue(emoji + " " + entry.getKey().name());
                row.createCell(1).setCellValue(entry.getValue());

                for (int i = 0; i < 2; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                overviewSheet.autoSizeColumn(i);
            }

            // Sheet 2: Insights & Recommendations
            if (!data.getInsights().isEmpty() || !data.getRecommendations().isEmpty()) {
                XSSFSheet insightsSheet = workbook.createSheet("Insights");
                rowNum = 0;

                // Insights
                Row insightsTitle = insightsSheet.createRow(rowNum++);
                Cell insightsTitleCell = insightsTitle.createCell(0);
                insightsTitleCell.setCellValue("Key Insights");
                insightsTitleCell.setCellStyle(titleStyle);
                rowNum++;

                for (String insight : data.getInsights()) {
                    Row row = insightsSheet.createRow(rowNum++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue("• " + insight);
                    cell.setCellStyle(dataStyle);
                }
                rowNum++;

                // Recommendations
                Row recsTitle = insightsSheet.createRow(rowNum++);
                Cell recsTitleCell = recsTitle.createCell(0);
                recsTitleCell.setCellValue("Recommendations");
                recsTitleCell.setCellStyle(titleStyle);
                rowNum++;

                for (String recommendation : data.getRecommendations()) {
                    Row row = insightsSheet.createRow(rowNum++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue("• " + recommendation);
                    cell.setCellStyle(dataStyle);
                }

                insightsSheet.setColumnWidth(0, 15000);
            }

            return convertToByteArray(workbook);
        }
    }

    /**
     * Generate Excel for Department Composition Report
     */
    public byte[] generateDepartmentCompositionExcel(DepartmentCompositionResponse data) throws IOException {
        log.info("Generating Department Composition Excel");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Sheet 1: Summary
            XSSFSheet summarySheet = workbook.createSheet("Summary");
            int rowNum = 0;

            // Title
            Row titleRow = summarySheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Department Composition Report");
            titleCell.setCellStyle(titleStyle);
            summarySheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            rowNum++;

            addDataRow(summarySheet, rowNum++, "Generated Date:", data.getGeneratedAt().toString(), headerStyle, dataStyle);
            addDataRow(summarySheet, rowNum++, "Total Departments:", String.valueOf(data.getTotalDepartments()), headerStyle, dataStyle);
            rowNum++;

            // Department Summary Table
            addTableHeader(summarySheet, rowNum++,
                    new String[]{"Department", "Code", "Members", "Dominant Sign", "Balanced", "Team Vibe"},
                    headerStyle);

            for (DepartmentCompositionResponse.DepartmentZodiacBreakdown dept : data.getDepartments()) {
                Row row = summarySheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dept.getDepartmentName());
                row.createCell(1).setCellValue(dept.getDepartmentCode());
                row.createCell(2).setCellValue(dept.getTotalMembers());
                row.createCell(3).setCellValue(dept.getDominantSign());
                row.createCell(4).setCellValue(dept.getIsBalanced() ? "Yes" : "No");
                row.createCell(5).setCellValue(dept.getTeamVibe());

                for (int i = 0; i < 6; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Auto-size
            for (int i = 0; i < 6; i++) {
                summarySheet.autoSizeColumn(i);
            }

            // Sheet 2: Detailed Breakdown (one sheet per department)
            for (DepartmentCompositionResponse.DepartmentZodiacBreakdown dept : data.getDepartments()) {
                String sheetName = dept.getDepartmentCode().replaceAll("[^a-zA-Z0-9]", "");
                if (sheetName.length() > 31) {
                    sheetName = sheetName.substring(0, 31);
                }

                XSSFSheet deptSheet = workbook.createSheet(sheetName);
                rowNum = 0;

                // Department Header
                Row deptTitle = deptSheet.createRow(rowNum++);
                Cell deptTitleCell = deptTitle.createCell(0);
                deptTitleCell.setCellValue(dept.getDepartmentName());
                deptTitleCell.setCellStyle(titleStyle);
                rowNum++;

                addDataRow(deptSheet, rowNum++, "Total Members:", String.valueOf(dept.getTotalMembers()), headerStyle, dataStyle);
                addDataRow(deptSheet, rowNum++, "Dominant Sign:", dept.getDominantSign(), headerStyle, dataStyle);
                addDataRow(deptSheet, rowNum++, "Is Balanced:", dept.getIsBalanced() ? "Yes" : "No", headerStyle, dataStyle);

                if (!dept.getMissingElements().isEmpty()) {
                    addDataRow(deptSheet, rowNum++, "Missing Elements:",
                            String.join(", ", dept.getMissingElements()), headerStyle, dataStyle);
                }
                rowNum++;

                // Zodiac Distribution
                addTableHeader(deptSheet, rowNum++, new String[]{"Zodiac Sign", "Count"}, headerStyle);
                for (Map.Entry<Member.ZodiacSign, Long> entry : dept.getZodiacDistribution().entrySet()) {
                    if (entry.getValue() > 0) {
                        Row row = deptSheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(entry.getKey().name());
                        row.createCell(1).setCellValue(entry.getValue());

                        for (int i = 0; i < 2; i++) {
                            row.getCell(i).setCellStyle(dataStyle);
                        }
                    }
                }

                deptSheet.autoSizeColumn(0);
                deptSheet.autoSizeColumn(1);
            }

            return convertToByteArray(workbook);
        }
    }

    /**
     * Generate Excel for Fun Stats Report
     */
    public byte[] generateFunStatsExcel(FunStatsResponse data) throws IOException {
        log.info("Generating Fun Stats Excel");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle sagittariusStyle = createSagittariusStyle(workbook);

            XSSFSheet sheet = workbook.createSheet("Fun Statistics");
            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("♐ Fun Zodiac Statistics");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
            rowNum += 2;

            // Sagittarius Special Section
            Row sagTitle = sheet.createRow(rowNum++);
            Cell sagTitleCell = sagTitle.createCell(0);
            sagTitleCell.setCellValue("♐ SAGITTARIUS POWER");
            sagTitleCell.setCellStyle(sagittariusStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

            addDataRow(sheet, rowNum++, "Count:", String.valueOf(data.getSagittariusCount()), headerStyle, dataStyle);
            addDataRow(sheet, rowNum++, "Percentage:", String.format("%.1f%%", data.getSagittariusPercentage()), headerStyle, dataStyle);
            rowNum++;

            // Compatibility Highlights
            Row compatTitle = sheet.createRow(rowNum++);
            Cell compatTitleCell = compatTitle.createCell(0);
            compatTitleCell.setCellValue("Compatibility Highlights");
            compatTitleCell.setCellStyle(titleStyle);
            rowNum++;

            addDataRow(sheet, rowNum++, "Most Compatible Pair:", data.getMostCompatiblePair(), headerStyle, dataStyle);
            addDataRow(sheet, rowNum++, "Least Compatible Pair:", data.getLeastCompatiblePair(), headerStyle, dataStyle);
            rowNum++;

            // Team Recognition
            Row teamTitle = sheet.createRow(rowNum++);
            Cell teamTitleCell = teamTitle.createCell(0);
            teamTitleCell.setCellValue("Team Recognition");
            teamTitleCell.setCellStyle(titleStyle);
            rowNum++;

            addDataRow(sheet, rowNum++, "Most Balanced Team:", data.getMostBalancedTeam(), headerStyle, dataStyle);
            addDataRow(sheet, rowNum++, "Zodiac of the Month:", data.getZodiacOfTheMonth(), headerStyle, dataStyle);
            rowNum++;

            // Fun Facts
            if (!data.getFunFacts().isEmpty()) {
                Row factsTitle = sheet.createRow(rowNum++);
                Cell factsTitleCell = factsTitle.createCell(0);
                factsTitleCell.setCellValue("Fun Facts");
                factsTitleCell.setCellStyle(titleStyle);
                rowNum++;

                for (String fact : data.getFunFacts()) {
                    Row row = sheet.createRow(rowNum++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue("• " + fact);
                    cell.setCellStyle(dataStyle);
                    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));
                }
            }

            // Auto-size
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            return convertToByteArray(workbook);
        }
    }

    /**
     * Generate Excel for Compatibility Matrix
     */
    public byte[] generateCompatibilityMatrixExcel(CompatibilityMatrixResponse data) throws IOException {
        log.info("Generating Compatibility Matrix Excel");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            XSSFSheet sheet = workbook.createSheet("Compatibility Matrix");
            int rowNum = 0;

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Member");
            headerRow.getCell(0).setCellStyle(headerStyle);

            for (int i = 0; i < data.getColumnLabels().size(); i++) {
                Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(data.getColumnLabels().get(i));
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            for (int i = 0; i < data.getRowLabels().size(); i++) {
                Row row = sheet.createRow(rowNum++);

                // Row label
                Cell labelCell = row.createCell(0);
                labelCell.setCellValue(data.getRowLabels().get(i));
                labelCell.setCellStyle(headerStyle);

                // Matrix cells
                List<CompatibilityMatrixResponse.MatrixCell> matrixRow = data.getMatrix().get(i);
                for (int j = 0; j < matrixRow.size(); j++) {
                    CompatibilityMatrixResponse.MatrixCell matrixCell = matrixRow.get(j);
                    Cell cell = row.createCell(j + 1);

                    if (matrixCell.getLevel().equals("Self")) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(matrixCell.getValue().doubleValue());
                    }

                    // Color coding
                    CellStyle colorStyle = createCompatibilityColorStyle(workbook, matrixCell.getValue());
                    cell.setCellStyle(colorStyle);
                }
            }

            // Auto-size first column
            sheet.autoSizeColumn(0);

            return convertToByteArray(workbook);
        }
    }

    // ==================== Helper Methods ====================

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

    private CellStyle createTitleStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        return style;
    }

    private CellStyle createSagittariusStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.VIOLET.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }

    private CellStyle createCompatibilityColorStyle(XSSFWorkbook workbook, java.math.BigDecimal score) {
        CellStyle style = workbook.createCellStyle();

        if (score.compareTo(java.math.BigDecimal.valueOf(80)) >= 0) {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        } else if (score.compareTo(java.math.BigDecimal.valueOf(65)) >= 0) {
            style.setFillForegroundColor(IndexedColors.LIME.getIndex());
        } else if (score.compareTo(java.math.BigDecimal.valueOf(50)) >= 0) {
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        } else if (score.compareTo(java.math.BigDecimal.valueOf(35)) >= 0) {
            style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        } else {
            style.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        }

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private void addDataRow(XSSFSheet sheet, int rowNum, String label, String value,
                            CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
    }

    private void addTableHeader(XSSFSheet sheet, int rowNum, String[] headers, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private byte[] convertToByteArray(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    private String getElementEmoji(Member.ZodiacElement element) {
        return switch (element) {
            case Fire -> "🔥";
            case Earth -> "🌍";
            case Air -> "💨";
            case Water -> "💧";
        };
    }
}