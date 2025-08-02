package com.cop.books.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cop_book_history")
public class BookHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_history_sequence_generator")
    @SequenceGenerator(name = "book_history_sequence_generator", sequenceName = "book_history_sequence", allocationSize = 100)
    private Long id;
    @Column(name = "event", length = 50)
    private String event;
    @Column(name = "description", length = 200)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "BookHistory{" +
                "id=" + id +
                ", event='" + event + '\'' +
                ", description='" + description + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}
