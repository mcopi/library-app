package com.cop.books.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cop_book")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence_generator")
    @SequenceGenerator(name = "book_sequence_generator", sequenceName = "book_sequence", allocationSize = 100)
    private Long id;
    @Column(name = "book_name", length = 150)
    private String bookName;
    @Column(name = "book_code", length = 20, unique = true)
    private String bookCode;
    @Column(name = "author", length = 150)
    private String author;
    @Column(name = "amount", precision = 10, scale = 0)
    private Integer amount;
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BookCategory categoryId;
    @Column(name = "file_name")
    private String fileName;
    @OneToMany(mappedBy = "bookId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookHistory> histories = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BookCategory categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<BookHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<BookHistory> histories) {
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", bookCode='" + bookCode + '\'' +
                ", author='" + author + '\'' +
                ", amount=" + amount +
                ", categoryId=" + categoryId +
                ", fileName='" + fileName + '\'' +
                ", histories=" + histories +
                '}';
    }
}
