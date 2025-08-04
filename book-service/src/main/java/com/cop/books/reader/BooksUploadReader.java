package com.cop.books.reader;

import com.cop.books.configs.FieldsPropsConfig;
import com.cop.books.dtos.FieldsDto;
import com.cop.books.models.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BooksUploadReader {
    @Value("${books.dir.upload}")
    private String filePath;
    private final Logger log = LoggerFactory.getLogger(BooksUploadReader.class);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private final FieldsPropsConfig props;

    public BooksUploadReader(FieldsPropsConfig props) {
        this.props = props;
    }

    @Bean
    public FlatFileItemReader<Book> bookReader() {
        return new FlatFileItemReaderBuilder<Book>()
                .name("bookCsvReader")
                .resource(getResource())
                .delimited()
                .names(getNames())
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {{
                        setTargetType(Book.class);
                }})
                .build();
    }

    private Resource getResource() {
        String abs = "books_" + dateFormatter.format(new Date()) + ".csv";
        log.info("Resource is {}", abs);
        return new FileSystemResource(filePath + abs);
    }

    private String[] getNames() {
        return props
                .getFields()
                .stream()
                .map(FieldsDto::getName)
                .toArray(String[]::new);
    }
}
