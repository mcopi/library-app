package com.cop.books.services;

import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings({"java:S112"})
public interface FileValidationService {
    void fileNameCheck(String file) throws Exception;
    void fileFormatCheck(String file) throws Exception;
    void fileColumnCheck(MultipartFile file) throws Exception;
}
