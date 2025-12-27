package com.jci.zodiac.service;

import com.jci.zodiac.dto.response.*;
import com.jci.zodiac.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * PdfExportService - Generate PDF reports using Apache PDFBox
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PdfExportService {

    private static final float MARGIN = 50;
    private static final float TITLE_SIZE = 20;
    private static final float HEADING_SIZE = 14;
    private static final float TEXT_SIZE = 11;
    private static final float LINE_HEIGHT = 15;

    /**
     * Generate PDF for Zodiac Analytics Report
     */
    public byte[] generateZodiacAnalyticsPdf(ZodiacAnalyticsResponse data) throws IOException {
        log.info("Generating Zodiac Analytics PDF");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = addTitle(content, "♐ ZODIAC ANALYTICS REPORT", yPosition);
                yPosition -= LINE_HEIGHT * 2;

                // Metadata
                yPosition = addText(content, "Generated: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        yPosition, TEXT_SIZE);
                yPosition = addText(content, "Total Members: " + data.getTotalMembers(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT;

                // Most/Least Common Signs
                yPosition = addHeading(content, "Zodiac Distribution", yPosition);
                yPosition = addText(content, "Most Common: " + data.getMostCommonSign(), yPosition, TEXT_SIZE);
                yPosition = addText(content, "Least Common: " + data.getLeastCommonSign(), yPosition, TEXT_SIZE);
                yPosition = addText(content, "Preferred Element: " + data.getPreferredElement(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT;

                // Zodiac Breakdown
                yPosition = addHeading(content, "Sign Distribution", yPosition);
                for (Map.Entry<Member.ZodiacSign, Long> entry : data.getZodiacDistribution().entrySet()) {
                    if (entry.getValue() > 0) {
                        String line = String.format("  %s: %d members (%.1f%%)",
                                entry.getKey().name(),
                                entry.getValue(),
                                (entry.getValue() * 100.0 / data.getTotalMembers()));
                        yPosition = addText(content, line, yPosition, TEXT_SIZE);
                    }
                }
                yPosition -= LINE_HEIGHT;

                // Element Distribution
                yPosition = addHeading(content, "Element Balance", yPosition);
                for (Map.Entry<Member.ZodiacElement, Long> entry : data.getElementDistribution().entrySet()) {
                    String emoji = getElementEmoji(entry.getKey());
                    String line = String.format("  %s %s: %d members",
                            emoji, entry.getKey().name(), entry.getValue());
                    yPosition = addText(content, line, yPosition, TEXT_SIZE);
                }
                yPosition -= LINE_HEIGHT;

                // Insights
                if (!data.getInsights().isEmpty()) {
                    yPosition = addHeading(content, "Key Insights", yPosition);
                    for (String insight : data.getInsights()) {
                        yPosition = addBulletPoint(content, insight, yPosition);
                    }
                    yPosition -= LINE_HEIGHT;
                }

                // Recommendations
                if (!data.getRecommendations().isEmpty()) {
                    yPosition = addHeading(content, "Recommendations", yPosition);
                    for (String recommendation : data.getRecommendations()) {
                        yPosition = addBulletPoint(content, recommendation, yPosition);
                    }
                }

                // Footer
                addFooter(content, page, "JCI Danang Junior Club - Zodiac HR Management");
            }

            return convertToByteArray(document);
        }
    }

    /**
     * Generate PDF for Department Composition Report
     */
    public byte[] generateDepartmentCompositionPdf(DepartmentCompositionResponse data) throws IOException {
        log.info("Generating Department Composition PDF");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = addTitle(content, "DEPARTMENT COMPOSITION REPORT", yPosition);
                yPosition -= LINE_HEIGHT * 2;

                yPosition = addText(content, "Total Departments: " + data.getTotalDepartments(), yPosition, TEXT_SIZE);
                yPosition = addText(content, "Generated: " + data.getGeneratedAt().format(DateTimeFormatter.ISO_DATE),
                        yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Each Department
                for (DepartmentCompositionResponse.DepartmentZodiacBreakdown dept : data.getDepartments()) {
                    // Check if we need a new page
                    if (yPosition < 150) {
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content.close();
                        PDPageContentStream newContent = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        return generateDepartmentCompositionPdf(data); // Recursive call with new page
                    }

                    yPosition = addHeading(content, dept.getDepartmentName() + " (" + dept.getDepartmentCode() + ")", yPosition);
                    yPosition = addText(content, "Total Members: " + dept.getTotalMembers(), yPosition, TEXT_SIZE);
                    yPosition = addText(content, "Dominant Sign: " + dept.getDominantSign(), yPosition, TEXT_SIZE);
                    yPosition = addText(content, "Team Vibe: " + dept.getTeamVibe(), yPosition, TEXT_SIZE);
                    yPosition = addText(content, "Balanced: " + (dept.getIsBalanced() ? "Yes" : "No"), yPosition, TEXT_SIZE);

                    if (!dept.getMissingElements().isEmpty()) {
                        yPosition = addText(content, "Missing Elements: " + String.join(", ", dept.getMissingElements()),
                                yPosition, TEXT_SIZE);
                    }

                    yPosition -= LINE_HEIGHT * 2;
                }

                addFooter(content, page, "JCI Danang Junior Club - Zodiac HR Management");
            }

            return convertToByteArray(document);
        }
    }

    /**
     * Generate PDF for Fun Stats Report
     */
    public byte[] generateFunStatsPdf(FunStatsResponse data) throws IOException {
        log.info("Generating Fun Stats PDF");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title with emoji
                yPosition = addTitle(content, "FUN ZODIAC STATISTICS", yPosition);
                yPosition -= LINE_HEIGHT * 2;

                // Sagittarius Special Section
                yPosition = addHeading(content, "Sagittarius Power", yPosition);
                yPosition = addText(content, String.format("Count: %d members", data.getSagittariusCount()),
                        yPosition, TEXT_SIZE);
                yPosition = addText(content, String.format("Percentage: %.1f%%", data.getSagittariusPercentage()),
                        yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Compatibility Highlights
                yPosition = addHeading(content, "Compatibility Highlights", yPosition);
                yPosition = addText(content, "Most Compatible Pair:", yPosition, TEXT_SIZE);
                yPosition = addText(content, "  " + data.getMostCompatiblePair(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT;

                yPosition = addText(content, "Least Compatible Pair:", yPosition, TEXT_SIZE);
                yPosition = addText(content, "  " + data.getLeastCompatiblePair(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Team Recognition
                yPosition = addHeading(content, "Team Recognition", yPosition);
                yPosition = addText(content, "Most Balanced Team: " + data.getMostBalancedTeam(), yPosition, TEXT_SIZE);
                yPosition = addText(content, "Zodiac of the Month: " + data.getZodiacOfTheMonth(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Fun Facts
                if (!data.getFunFacts().isEmpty()) {
                    yPosition = addHeading(content, "Fun Facts", yPosition);
                    for (String fact : data.getFunFacts()) {
                        yPosition = addBulletPoint(content, fact, yPosition);
                    }
                }

                addFooter(content, page, "JCI Danang Junior Club - Zodiac HR Management");
            }

            return convertToByteArray(document);
        }
    }

    /**
     * Generate PDF for Compatibility Report
     */
    public byte[] generateCompatibilityReportPdf(CompatibilityReport report) throws IOException {
        log.info("Generating Compatibility Report PDF: {}", report.getReportType());

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = addTitle(content, report.getTitle(), yPosition);
                yPosition -= LINE_HEIGHT * 2;

                // Summary
                yPosition = addText(content, report.getSummary(), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT;

                yPosition = addText(content, String.format("Overall Score: %.1f%% (%s)",
                        report.getOverallScore(), report.getOverallLevel()), yPosition, TEXT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Strengths
                if (report.getKeyStrengths() != null && !report.getKeyStrengths().isEmpty()) {
                    yPosition = addHeading(content, "Key Strengths", yPosition);
                    for (String strength : report.getKeyStrengths()) {
                        yPosition = addBulletPoint(content, strength, yPosition);
                    }
                    yPosition -= LINE_HEIGHT;
                }

                // Weaknesses
                if (report.getKeyWeaknesses() != null && !report.getKeyWeaknesses().isEmpty()) {
                    yPosition = addHeading(content, "Areas to Watch", yPosition);
                    for (String weakness : report.getKeyWeaknesses()) {
                        yPosition = addBulletPoint(content, weakness, yPosition);
                    }
                    yPosition -= LINE_HEIGHT;
                }

                // Recommendations
                if (report.getActionItems() != null && !report.getActionItems().isEmpty()) {
                    yPosition = addHeading(content, "Action Items", yPosition);
                    for (String action : report.getActionItems()) {
                        yPosition = addBulletPoint(content, action, yPosition);
                    }
                }

                addFooter(content, page, "Generated: " + report.getGeneratedAt().format(DateTimeFormatter.ISO_DATE_TIME));
            }

            return convertToByteArray(document);
        }
    }

    // ==================== Helper Methods ====================

    private float addTitle(PDPageContentStream content, String text, float yPosition) throws IOException {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), TITLE_SIZE);
        content.newLineAtOffset(MARGIN, yPosition);
        content.showText(text);
        content.endText();
        return yPosition - LINE_HEIGHT * 2;
    }

    private float addHeading(PDPageContentStream content, String text, float yPosition) throws IOException {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), HEADING_SIZE);
        content.newLineAtOffset(MARGIN, yPosition);
        content.showText(text);
        content.endText();
        return yPosition - LINE_HEIGHT * 1.5f;
    }

    private float addText(PDPageContentStream content, String text, float yPosition, float fontSize) throws IOException {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
        content.newLineAtOffset(MARGIN, yPosition);

        // Handle long text by wrapping
        String wrappedText = text.length() > 80 ? text.substring(0, 77) + "..." : text;
        content.showText(wrappedText);
        content.endText();
        return yPosition - LINE_HEIGHT;
    }

    private float addBulletPoint(PDPageContentStream content, String text, float yPosition) throws IOException {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), TEXT_SIZE);
        content.newLineAtOffset(MARGIN + 10, yPosition);
        String wrappedText = text.length() > 75 ? text.substring(0, 72) + "..." : text;
        content.showText("• " + wrappedText);
        content.endText();
        return yPosition - LINE_HEIGHT;
    }

    private void addFooter(PDPageContentStream content, PDPage page, String text) throws IOException {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.newLineAtOffset(MARGIN, 30);
        content.showText(text);
        content.endText();
    }

    private byte[] convertToByteArray(PDDocument document) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
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