package com.cop.books.services.impl;

import com.cop.books.services.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    public void moveFileToUploadPath(MultipartFile file, String path) {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Path.of(path + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully move File to {}", path);
        } catch (IOException e) {
            log.error("Error while moving file to Path: {}", e.getMessage());
        }
    }
}
