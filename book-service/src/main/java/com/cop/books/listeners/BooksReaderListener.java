package com.cop.books.listeners;

import com.cop.books.models.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BooksReaderListener implements ItemReadListener<Book> {
    @Value("${books.dir.upload}")
    private String filePath;
    @Value("${books.dir.proceed}")
    private String fileProceedPath;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private final Logger log = LoggerFactory.getLogger(BooksReaderListener.class);

    @Override
    public void afterRead(Book item) {
        String fileName = getFileName();
        item.setFileName(fileName);
        ItemReadListener.super.afterRead(item);
    }

    private String getFileName() {
        final AtomicInteger index = new AtomicInteger(0);
        String fileName = null;
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                log.info("File ke {} is {}", index, file.getName());
                if (file.isFile() && file.getName().contains("books_" + dateFormatter.format(new Date()))) {
                    index.getAndIncrement();

                    fileName = file.getName();
                    moveFileToProceed(file.getAbsolutePath(), file.getName());
                }
            }
        }

        return fileName;
    }

    private void moveFileToProceed(String abs, String name) {
        Path destinationPath = Paths.get(fileProceedPath + name);
        Path sourcePath = Paths.get(abs);
        try {
            Files.move(sourcePath, destinationPath);
        } catch (IOException e) {
            log.error("Error while moving file: {} with error: {}", abs, e.getMessage());
        }
    }
}
