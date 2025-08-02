package com.cop.books.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/scheduler-books")
public class SchedulerBooksController {
    private final JobLauncher jobLauncher;
    private final Job job;

    private final Logger log = LoggerFactory.getLogger(SchedulerBooksController.class);

    @Value("${books.dir.upload}")
    private String filePath;

    public SchedulerBooksController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping(value = "/import-books")
    @Scheduled(cron = "${books.cron.import-books}")
    public void importBooksJob() {
        log.info("Running import books job...");


        try (Stream<Path> entries = Files.list(Paths.get(filePath))) {
            if (entries.findAny().isEmpty()) {
                log.warn("Scheduled job skipped because uploaded file not found at {}", filePath);
                return; // Exit the method, do not launch the job
            }

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job Exception Already Running Exception: {}", e.getMessage());
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("Job Instance Already Complete Exception: {}", e.getMessage());
        } catch (JobRestartException e) {
            log.error("Job Restart Exception: {}", e.getMessage());
        } catch (JobParametersInvalidException e) {
            log.error("Job Parameters Invalid Exception: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
