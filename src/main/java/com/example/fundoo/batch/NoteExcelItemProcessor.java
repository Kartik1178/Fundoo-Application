package com.example.fundoo.batch;

import com.example.fundoo.entity.Note;
import com.example.fundoo.entity.User;
import com.example.fundoo.exception.UserNotFoundException;
import com.example.fundoo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class NoteExcelItemProcessor implements ItemProcessor<NoteExcelRow, Note> {

    private static final Logger log = LoggerFactory.getLogger(NoteExcelItemProcessor.class);

    private final UserRepository userRepository;

    public NoteExcelItemProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Note process(NoteExcelRow item) {
        if (item.getTitle() == null || item.getTitle().isBlank()) {
            log.warn("Row {} skipped - title is empty", item.getRowNumber());
            return null; // null = skip this item in Spring Batch
        }
        if (item.getOwnerEmail() == null || item.getOwnerEmail().isBlank()) {
            log.warn("Row {} skipped - owner email is empty", item.getRowNumber());
            return null;
        }

        User user = userRepository.findByEmail(item.getOwnerEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found for email: " + item.getOwnerEmail()));

        Note note = new Note();
        note.setTitle(item.getTitle());
        note.setDescription(item.getDescription());
        note.setUser(user);

        log.debug("Row {} processed - note '{}' for user '{}'",
                item.getRowNumber(), note.getTitle(), user.getEmail());
        return note;
    }
}
