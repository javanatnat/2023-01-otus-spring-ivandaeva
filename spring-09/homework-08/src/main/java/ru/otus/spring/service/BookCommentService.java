package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;

import java.util.List;

public interface BookCommentService {
    BookComment add(String text, String bookName, String authorName);
    BookComment add(String text, Book book);
    BookComment save(BookComment bookComment);
    void delete(BookComment bookComment);
    List<BookComment> findByBookAndAuthor(String bookName, String authorName);
}
