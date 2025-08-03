package com.cop.books.writer;

import com.cop.books.dtos.BookUploadWrapper;
import com.cop.books.entities.Book;
import com.cop.books.entities.BookHistory;
import com.cop.books.repositories.BookHistoryRepository;
import com.cop.books.repositories.BookRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BooksUploadWriter implements ItemWriter<BookUploadWrapper> {
    private final BookRepository bookRepository;
    private final BookHistoryRepository bookHistoryRepository;

    public BooksUploadWriter(BookRepository bookRepository, BookHistoryRepository bookHistoryRepository) {
        this.bookRepository = bookRepository;
        this.bookHistoryRepository = bookHistoryRepository;
    }

    @Override
    public void write(Chunk<? extends BookUploadWrapper> chunk) throws Exception {
        List<Book> books = new ArrayList<>();
        List<BookHistory> histories = new ArrayList<>();

        for (BookUploadWrapper output : chunk.getItems()) {
            books.add(output.getBook());
            histories.add(output.getBookHistory());
        }

        bookRepository.saveAll(books);

        final AtomicInteger i = new AtomicInteger(0);
        for (BookHistory history : histories) {
            history.setBookId(books.get(i.getAndIncrement()));
        }

        bookHistoryRepository.saveAll(histories);
    }
}
