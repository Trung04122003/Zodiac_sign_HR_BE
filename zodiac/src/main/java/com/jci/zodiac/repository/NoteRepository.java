package com.jci.zodiac.repository;

import com.jci.zodiac.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByNoteType(Note.NoteType noteType);

    List<Note> findByMemberId(Long memberId);

    List<Note> findByTeamId(Long teamId);

    List<Note> findByDepartmentId(Long departmentId);

    List<Note> findByIsImportant(Boolean isImportant);

    Page<Note> findByNoteType(Note.NoteType noteType, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword%")
    List<Note> searchByKeyword(@Param("keyword") String keyword);

    long countByMemberId(Long memberId);
}