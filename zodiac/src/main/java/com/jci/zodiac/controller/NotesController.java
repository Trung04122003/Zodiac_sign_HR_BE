package com.jci.zodiac.controller;

import com.jci.zodiac.dto.request.CreateNoteRequest;
import com.jci.zodiac.dto.response.NoteResponse;
import com.jci.zodiac.entity.Note;
import com.jci.zodiac.service.NoteService;
import com.jci.zodiac.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NotesController - REST APIs for member notes
 * Base URL: /api/notes
 */
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notes Management", description = "APIs for managing personal notes and observations")
public class NotesController {

    private final NoteService noteService;

    /**
     * Create a new note
     * POST /api/notes
     */
    @PostMapping
    @Operation(summary = "Create note", description = "Create a new note or observation")
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(
            @Valid @RequestBody CreateNoteRequest request) {

        log.info("REST request to create note of type: {}", request.getNoteType());

        NoteResponse response = noteService.createNote(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Note created successfully", response));
    }

    /**
     * Get note by ID
     * GET /api/notes/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get note by ID", description = "Retrieve note details by ID")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteById(
            @Parameter(description = "Note ID") @PathVariable Long id) {

        log.info("REST request to get note by id: {}", id);

        NoteResponse response = noteService.getNoteById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all notes
     * GET /api/notes
     */
    @GetMapping
    @Operation(summary = "Get all notes", description = "Retrieve all notes")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getAllNotes() {

        log.info("REST request to get all notes");

        List<NoteResponse> response = noteService.getAllNotes();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get notes by type
     * GET /api/notes/type/{type}
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get notes by type", description = "Retrieve notes filtered by type (Member/Team/Department/General)")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getNotesByType(
            @Parameter(description = "Note type") @PathVariable Note.NoteType type) {

        log.info("REST request to get notes by type: {}", type);

        List<NoteResponse> response = noteService.getNotesByType(type);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get notes by member
     * GET /api/notes/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get notes by member", description = "Retrieve all notes for a specific member")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getNotesByMember(
            @Parameter(description = "Member ID") @PathVariable Long memberId) {

        log.info("REST request to get notes for member: {}", memberId);

        List<NoteResponse> response = noteService.getNotesByMember(memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get important notes
     * GET /api/notes/important
     */
    @GetMapping("/important")
    @Operation(summary = "Get important notes", description = "Retrieve all notes marked as important")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getImportantNotes() {

        log.info("REST request to get important notes");

        List<NoteResponse> response = noteService.getImportantNotes();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update note
     * PUT /api/notes/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update note", description = "Update existing note")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @Parameter(description = "Note ID") @PathVariable Long id,
            @Valid @RequestBody CreateNoteRequest request) {

        log.info("REST request to update note: {}", id);

        NoteResponse response = noteService.updateNote(id, request);

        return ResponseEntity.ok(ApiResponse.success("Note updated successfully", response));
    }

    /**
     * Delete note
     * DELETE /api/notes/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note", description = "Delete a note")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @Parameter(description = "Note ID") @PathVariable Long id) {

        log.info("REST request to delete note: {}", id);

        noteService.deleteNote(id);

        return ResponseEntity.ok(ApiResponse.success("Note deleted successfully"));
    }
}