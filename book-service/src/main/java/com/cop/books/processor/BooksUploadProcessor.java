package com.cop.books.processor;

import com.cop.books.dtos.BookUploadWrapper;
import com.cop.books.models.Book;
import com.cop.books.models.BookHistory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class BooksUploadProcessor implements ItemProcessor<Book, BookUploadWrapper> {
    @Override
    public BookUploadWrapper process(Book item) throws Exception {
        // Business Logic here
        item.setBookCode(setBookCode(item.getBookName()));

        BookHistory history = new BookHistory();
        history.setEvent("Upload");
        history.setDescription("New book added: " + item.getBookName());
        history.setBookId(null);

        return new BookUploadWrapper(item, history);
    }

    private String setBookCode(String bookName) {
        String[] splitNames = bookName.split(" ");
        return Arrays.stream(splitNames).map(d -> d.substring(0, 1)).collect(Collectors.joining());
    }
}
