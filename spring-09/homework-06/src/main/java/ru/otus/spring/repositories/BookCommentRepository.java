package ru.otus.spring.repositories;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;

import java.util.Optional;

public interface BookCommentRepository {
    long countByBook(Book book);
    BookComment save(BookComment comment);
    Optional<BookComment> getById(long id);
    void delete(BookComment bookComment);
    Optional<BookComment> findByTextAndBook(String comment, Book book);
}
