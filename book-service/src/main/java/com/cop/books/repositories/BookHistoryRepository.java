package com.cop.books.repositories;

import com.cop.books.models.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookHistoryRepository extends JpaRepository<BookHistory, Long> {
    @Query("SELECT a FROM BookHistory a " +
            "JOIN FETCH a.bookId b " +
            "WHERE b.id = :id")
    List<BookHistory> findAllByBookId(Long id);
}
