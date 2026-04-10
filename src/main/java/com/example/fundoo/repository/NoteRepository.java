package com.example.fundoo.repository;

import com.example.fundoo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
    List<Note> findByUserIdAndArchivedFalseAndTrashedFalse(Long userId);
    List<Note> findByUserIdAndPinnedTrue(Long userId);
    List<Note> findByUserIdAndArchivedTrue(Long userId);
    List<Note> findByUserIdAndTrashedTrue(Long userId);
    List<Note> findByUserIdAndTitleContainingIgnoreCase(Long userId, String keyword);
    Optional<Note> findByIdAndUserId(Long noteId, Long userId);
}
