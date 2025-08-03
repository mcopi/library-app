package com.cop.books.services.impl;

import com.cop.books.dtos.BookDto;
import com.cop.books.dtos.PaginationDto;
import com.cop.books.entities.Book;
import com.cop.books.repositories.BookCategoryRepository;
import com.cop.books.repositories.BookRepository;
import com.cop.books.services.BookService;
import com.cop.books.services.FileUploadService;
import com.cop.books.services.FileValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final FileValidationService fileValidationService;
    private final FileUploadService fileUploadService;

    @Value("${books.dir.upload}")
    private String uploadDir;

    public BookServiceImpl(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, FileUploadService fileUploadService, FileValidationService fileValidationService) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.fileUploadService = fileUploadService;
        this.fileValidationService = fileValidationService;
    }

    @Override
    public List<BookDto> getAllBooks(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(
                    paginationDto.getOffset(),
                    paginationDto.getLimit(),
                    Sort.Direction.valueOf(paginationDto.getOrderBy().toUpperCase()),
                    paginationDto.getSortBy()
                );
        List<Book> books = bookRepository.findAllWithPagination(pageable);
        return books.stream().map(this::toResponseDto).toList();
    }

    private BookDto toResponseDto(Book book) {
        BookDto dto = new BookDto();
        dto.setBookId(book.getId());
        dto.setBookName(book.getBookName());
        dto.setAuthor(book.getAuthor());
        dto.setCategoryId(book.getCategoryId().getId());
        return dto;
    }

    @Override
    public String uploadBooksCsv(MultipartFile file) throws Exception {
        // File Validation
        fileValidationService.fileNameCheck(file.getOriginalFilename());
        fileValidationService.fileFormatCheck(file.getOriginalFilename());
        fileValidationService.fileColumnCheck(file);

        // Move to Upload Path
        fileUploadService.moveFileToUploadPath(file, uploadDir);

        return "Success";
    }
}
