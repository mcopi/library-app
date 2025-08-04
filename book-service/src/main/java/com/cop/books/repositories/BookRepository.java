package com.cop.books.repositories;

import com.cop.books.models.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT a FROM Book a JOIN FETCH a.categoryId")
    List<Book> findAllWithPagination(Pageable pageable);
}
