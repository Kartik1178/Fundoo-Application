package com.example.fundoo.batch;

import com.example.fundoo.entity.Note;
import com.example.fundoo.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class NoteExcelItemWriter implements ItemWriter<Note> {

    private static final Logger log = LoggerFactory.getLogger(NoteExcelItemWriter.class);

    private final NoteRepository noteRepository;

    public NoteExcelItemWriter(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void write(Chunk<? extends Note> chunk) {
        log.info("Writing chunk of {} notes to database", chunk.size());
        noteRepository.saveAll(chunk.getItems());
        log.info("Successfully saved {} notes", chunk.size());
    }
}
