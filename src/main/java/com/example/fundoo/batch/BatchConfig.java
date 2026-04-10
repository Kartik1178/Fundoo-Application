package com.example.fundoo.batch;

import com.example.fundoo.repository.NoteRepository;
import com.example.fundoo.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       NoteRepository noteRepository,
                       UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public NoteExcelItemReader noteExcelItemReader() {
        // File path can be passed dynamically via JobParameters in production
        return new NoteExcelItemReader("notes_import.xlsx");
    }

    @Bean
    public NoteExcelItemProcessor noteExcelItemProcessor() {
        return new NoteExcelItemProcessor(userRepository);
    }

    @Bean
    public NoteExcelItemWriter noteExcelItemWriter() {
        return new NoteExcelItemWriter(noteRepository);
    }

    @Bean
    public Step noteImportStep() {
        return new StepBuilder("noteImportStep", jobRepository)
                .<NoteExcelRow, com.example.fundoo.entity.Note>chunk(20, transactionManager)
                .reader(noteExcelItemReader())
                .processor(noteExcelItemProcessor())
                .writer(noteExcelItemWriter())
                .build();
    }

    @Bean
    public Job importNotesJob(Step noteImportStep) {
        return new JobBuilder("importNotesJob", jobRepository)
                .start(noteImportStep)
                .build();
    }
}
