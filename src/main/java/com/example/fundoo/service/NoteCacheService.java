package com.example.fundoo.service;

import com.example.fundoo.dto.response.NoteResponseDto;
import com.example.fundoo.entity.Note;
import com.example.fundoo.exception.NoteNotFoundException;
import com.example.fundoo.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class NoteCacheService {

    private static final Logger log = LoggerFactory.getLogger(NoteCacheService.class);

    private final NoteRepository noteRepository;

    public NoteCacheService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Cache note by ID - returns from cache if already stored
    @Cacheable(value = "notes", key = "#noteId")
    public NoteResponseDto getCachedNote(Long noteId) {
        log.debug("Cache miss - fetching note id: {} from database", noteId);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found: " + noteId));
        return mapToDto(note);
    }

    // Update cache when note is updated
    @CachePut(value = "notes", key = "#noteId")
    public NoteResponseDto refreshCachedNote(Long noteId) {
        log.debug("Refreshing cache for note id: {}", noteId);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found: " + noteId));
        return mapToDto(note);
    }

    // Evict cache when note is deleted
    @CacheEvict(value = "notes", key = "#noteId")
    public void evictNoteCache(Long noteId) {
        log.debug("Evicting cache for note id: {}", noteId);
    }

    // Evict all notes cache
    @CacheEvict(value = "notes", allEntries = true)
    public void evictAllNotesCache() {
        log.info("Evicting all notes cache");
    }

    private NoteResponseDto mapToDto(Note note) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setDescription(note.getDescription());
        dto.setPinned(note.isPinned());
        dto.setArchived(note.isArchived());
        dto.setTrashed(note.isTrashed());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }
}
