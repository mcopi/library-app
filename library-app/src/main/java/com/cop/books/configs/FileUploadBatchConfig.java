package com.cop.books.configs;

import com.cop.books.dtos.BookUploadWrapper;
import com.cop.books.entities.Book;
import com.cop.books.listeners.BooksReaderListener;
import com.cop.books.processor.BooksUploadProcessor;
import com.cop.books.reader.BooksUploadReader;
import com.cop.books.writer.BooksUploadWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class FileUploadBatchConfig {
    private final BooksUploadProcessor booksUploadProcessor;
    private final BooksUploadReader booksUploadReader;
    private final BooksUploadWriter booksUploadWriter;
    private final BooksReaderListener booksReaderListener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Value("${books.chunk-size}")
    private int chunkSize;
    @Value("${books.dir.upload}")
    private String filePath;
    private final Logger log = LoggerFactory.getLogger(FileUploadBatchConfig.class);

    public FileUploadBatchConfig(BooksUploadProcessor booksUploadProcessor, BooksUploadReader booksUploadReader,
                                 BooksUploadWriter booksUploadWriter, JobRepository jobRepository,
                                 PlatformTransactionManager platformTransactionManager, BooksReaderListener booksReaderListener) {
        this.booksReaderListener = booksReaderListener;
        this.booksUploadProcessor = booksUploadProcessor;
        this.booksUploadReader = booksUploadReader;
        this.booksUploadWriter = booksUploadWriter;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public FlatFileItemReader<Book> bookUploadReaderBean() {
        return booksUploadReader.bookReader();
    }

    @Bean
    public Step bookImportStep() {
        return new StepBuilder("bookCsvImport", jobRepository)
                .<Book, BookUploadWrapper>chunk(chunkSize, platformTransactionManager)
                .reader(bookUploadReaderBean())
                .processor(booksUploadProcessor)
                .writer(booksUploadWriter)
                .allowStartIfComplete(true)
                .listener(booksReaderListener)
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importBooks", jobRepository)
                .start(bookImportStep())
                .build();
    }
}
