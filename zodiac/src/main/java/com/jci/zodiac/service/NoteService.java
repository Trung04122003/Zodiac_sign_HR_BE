package com.jci.zodiac.service;

import com.jci.zodiac.dto.request.CreateNoteRequest;
import com.jci.zodiac.dto.response.NoteResponse;
import com.jci.zodiac.entity.Member;
import com.jci.zodiac.entity.Note;
import com.jci.zodiac.exception.ResourceNotFoundException;
import com.jci.zodiac.repository.MemberRepository;
import com.jci.zodiac.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public NoteResponse createNote(CreateNoteRequest request) {
        log.info("Creating note of type: {}", request.getNoteType());

        Note note = Note.builder()
                .noteType(request.getNoteType())
                .memberId(request.getMemberId())
                .teamId(request.getTeamId())
                .departmentId(request.getDepartmentId())
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .isImportant(request.getIsImportant())
                .build();

        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
        return toResponse(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByType(Note.NoteType type) {
        return noteRepository.findByNoteType(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByMember(Long memberId) {
        return noteRepository.findByMemberId(memberId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getImportantNotes() {
        return noteRepository.findByIsImportant(true).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NoteResponse updateNote(Long id, CreateNoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setTags(request.getTags());
        note.setIsImportant(request.getIsImportant());

        Note updated = noteRepository.save(note);
        return toResponse(updated);
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note", "id", id);
        }
        noteRepository.deleteById(id);
    }

    private NoteResponse toResponse(Note note) {
        String memberName = null;
        if (note.getMemberId() != null) {
            memberName = memberRepository.findById(note.getMemberId())
                    .map(Member::getFullName)
                    .orElse(null);
        }

        return NoteResponse.builder()
                .id(note.getId())
                .noteType(note.getNoteType().name())
                .memberId(note.getMemberId())
                .memberName(memberName)
                .teamId(note.getTeamId())
                .departmentId(note.getDepartmentId())
                .title(note.getTitle())
                .content(note.getContent())
                .tags(note.getTags())
                .isImportant(note.getIsImportant())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}