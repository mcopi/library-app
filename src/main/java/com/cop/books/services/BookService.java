package com.cop.books.services;

import com.cop.books.dtos.BookDto;
import com.cop.books.dtos.PaginationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SuppressWarnings({"java:S112"})
public interface BookService {
    List<BookDto> getAllBooks(PaginationDto paginationDto);

    String uploadBooksCsv(MultipartFile file) throws Exception;
}
