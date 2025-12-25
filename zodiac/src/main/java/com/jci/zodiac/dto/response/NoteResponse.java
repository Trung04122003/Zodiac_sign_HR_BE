package com.jci.zodiac.dto.response;

import com.jci.zodiac.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteResponse {

    private Long id;
    private String noteType;
    private Long memberId;
    private String memberName; // Populated if member note
    private Long teamId;
    private Long departmentId;
    private String title;
    private String content;
    private List<String> tags;
    private Boolean isImportant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}