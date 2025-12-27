package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportPreviewResponse {
    private String reportType;
    private String title;
    private String summary;
    private Integer pageCount;
    private String estimatedSize;
    private List<String> availableFormats;
    private Object preview; // Can be CompatibilityReport or other report types
}