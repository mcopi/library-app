package com.cop.books.controllers;

import com.cop.books.dtos.BookDto;
import com.cop.books.dtos.PaginationDto;
import com.cop.books.dtos.ResponseDto;
import com.cop.books.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    private final BookService bookService;
    Logger log = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<ResponseDto<List<BookDto>>> getAllBooks(@RequestBody PaginationDto dto) {
        try {
            return ResponseEntity.ok().body(new ResponseDto<>(
                bookService.getAllBooks(dto),
                "Success get all books",
                HttpStatus.OK.value())
            );
        } catch (Exception e) {
            log.error("Error while getting books: {}", e.getMessage());

            return ResponseEntity.internalServerError().body(new ResponseDto<>(
                null,
                "Error get all books",
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }

    @PostMapping(value = "upload")
    public ResponseDto<String> uploadBooksCsv(@RequestParam(name = "file") MultipartFile file) {
        try {
            return new ResponseDto<>(
                bookService.uploadBooksCsv(file),
        "Success upload books csv",
                HttpStatus.OK.value()
            );
        } catch (Exception e) {
            log.error("Error while uploading books: {}", e.getMessage());

            return new ResponseDto<>(
            null,
        "Error uploading books csv",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @GetMapping("/testing")
    public ResponseEntity<String> getBook() {
        return ResponseEntity.ok().body("Book");
    }
}
