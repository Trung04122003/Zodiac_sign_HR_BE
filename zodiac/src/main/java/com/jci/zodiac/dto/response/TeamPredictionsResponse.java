package com.jci.zodiac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPredictionsResponse {
    private Integer teamSize;
    private BigDecimal predictedCompatibility;
    private List<String> predictions;
    private List<String> risks;
    private List<String> opportunities;
    private BigDecimal confidence; // 0-100%
}