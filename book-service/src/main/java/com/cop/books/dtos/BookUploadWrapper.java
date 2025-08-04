package com.cop.books.dtos;

import com.cop.books.models.Book;
import com.cop.books.models.BookHistory;

public class BookUploadWrapper {
    private Book book;
    private BookHistory bookHistory;

    public BookUploadWrapper(Book book, BookHistory bookHistory) {
        this.book = book;
        this.bookHistory = bookHistory;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookHistory getBookHistory() {
        return bookHistory;
    }

    public void setBookHistory(BookHistory bookHistory) {
        this.bookHistory = bookHistory;
    }
}
