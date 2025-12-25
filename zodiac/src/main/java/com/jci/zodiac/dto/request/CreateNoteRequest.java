package com.jci.zodiac.dto.request;

import com.jci.zodiac.entity.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNoteRequest {

    @NotNull(message = "Note type is required")
    private Note.NoteType noteType;

    private Long memberId;
    private Long teamId;
    private Long departmentId;

    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private List<String> tags;

    @Builder.Default
    private Boolean isImportant = false;
}