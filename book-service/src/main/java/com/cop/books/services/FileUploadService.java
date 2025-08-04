package com.cop.books.services;


import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileUploadService {
    void moveFileToUploadPath(MultipartFile file, String path);
}
