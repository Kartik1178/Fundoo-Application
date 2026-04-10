package com.example.fundoo.service.impl;

import com.example.fundoo.dto.request.NoteRequestDto;
import com.example.fundoo.dto.response.NoteResponseDto;
import com.example.fundoo.entity.Note;
import com.example.fundoo.entity.User;
import com.example.fundoo.exception.NoteNotFoundException;
import com.example.fundoo.exception.UserNotFoundException;
import com.example.fundoo.repository.NoteRepository;
import com.example.fundoo.repository.UserRepository;
import com.example.fundoo.service.NoteService;
import com.example.fundoo.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public NoteServiceImpl(NoteRepository noteRepository,
                           UserRepository userRepository,
                           TokenUtil tokenUtil) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    private User getUserFromToken(String token) {
        Long userId = tokenUtil.extractUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Note getNoteForUser(Long noteId, Long userId) {
        return noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));
    }

    private NoteResponseDto mapToResponse(Note note, String message) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setDescription(note.getDescription());
        dto.setPinned(note.isPinned());
        dto.setArchived(note.isArchived());
        dto.setTrashed(note.isTrashed());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        dto.setMessage(message);
        return dto;
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto requestDto, String token) {
        log.info("Creating note for authenticated user");
        log.debug("Received title: {}", requestDto.getTitle());
        User user = getUserFromToken(token);
        Note note = new Note();
        note.setTitle(requestDto.getTitle());
        note.setDescription(requestDto.getDescription());
        note.setUser(user);
        Note saved = noteRepository.save(note);
        log.info("Note created with id: {}", saved.getId());
        return mapToResponse(saved, "Note created successfully");
    }

    @Override
    public NoteResponseDto updateNote(Long noteId, NoteRequestDto requestDto, String token) {
        log.info("Updating note id: {}", noteId);
        User user = getUserFromToken(token);
        Note note = getNoteForUser(noteId, user.getId());
        note.setTitle(requestDto.getTitle());
        note.setDescription(requestDto.getDescription());
        return mapToResponse(noteRepository.save(note), "Note updated successfully");
    }

    @Override
    public void deleteNote(Long noteId, String token) {
        log.info("Deleting note id: {}", noteId);
        User user = getUserFromToken(token);
        Note note = getNoteForUser(noteId, user.getId());
        noteRepository.delete(note);
        log.info("Note deleted id: {}", noteId);
    }

    @Override
    public List<NoteResponseDto> getAllNotes(String token) {
        User user = getUserFromToken(token);
        log.debug("Fetching all active notes for user id: {}", user.getId());
        return noteRepository.findByUserIdAndArchivedFalseAndTrashedFalse(user.getId())
                .stream().map(n -> mapToResponse(n, null)).collect(Collectors.toList());
    }

    @Override
    public NoteResponseDto getNoteById(Long noteId, String token) {
        User user = getUserFromToken(token);
        return mapToResponse(getNoteForUser(noteId, user.getId()), null);
    }

    @Override
    public NoteResponseDto pinNote(Long noteId, String token) {
        User user = getUserFromToken(token);
        Note note = getNoteForUser(noteId, user.getId());
        note.setPinned(!note.isPinned());
        return mapToResponse(noteRepository.save(note), "Note pin status updated");
    }

    @Override
    public NoteResponseDto archiveNote(Long noteId, String token) {
        User user = getUserFromToken(token);
        Note note = getNoteForUser(noteId, user.getId());
        note.setArchived(!note.isArchived());
        return mapToResponse(noteRepository.save(note), "Note archive status updated");
    }

    @Override
    public NoteResponseDto trashNote(Long noteId, String token) {
        User user = getUserFromToken(token);
        Note note = getNoteForUser(noteId, user.getId());
        note.setTrashed(!note.isTrashed());
        return mapToResponse(noteRepository.save(note), "Note trash status updated");
    }

    @Override
    public List<NoteResponseDto> searchNotes(String keyword, String token) {
        User user = getUserFromToken(token);
        log.debug("Searching notes with keyword: {} for user: {}", keyword, user.getId());
        return noteRepository.findByUserIdAndTitleContainingIgnoreCase(user.getId(), keyword)
                .stream().map(n -> mapToResponse(n, null)).collect(Collectors.toList());
    }
}
