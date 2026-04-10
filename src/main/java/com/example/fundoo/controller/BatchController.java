package com.example.fundoo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private static final Logger log = LoggerFactory.getLogger(BatchController.class);

    private final JobLauncher jobLauncher;
    private final Job importNotesJob;

    public BatchController(JobLauncher jobLauncher, Job importNotesJob) {
        this.jobLauncher = jobLauncher;
        this.importNotesJob = importNotesJob;
    }

    @PostMapping("/import-notes")
    public ResponseEntity<String> importNotes() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(importNotesJob, params);
            log.info("Notes import batch job launched");
            return ResponseEntity.ok("Batch import job started successfully");
        } catch (Exception e) {
            log.error("Batch job failed: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Batch job failed: " + e.getMessage());
        }
    }
}
