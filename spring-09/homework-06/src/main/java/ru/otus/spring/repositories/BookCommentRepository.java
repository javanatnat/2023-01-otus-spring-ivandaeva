package ru.otus.spring.repositories;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    long countByBook(Book book);
    BookComment save(BookComment comment);
    Optional<BookComment> getById(long id);
    void deleteById(long id);
    List<BookComment> getByBook(Book book);
    Optional<BookComment> findByTextAndBook(String comment, Book book);
}
