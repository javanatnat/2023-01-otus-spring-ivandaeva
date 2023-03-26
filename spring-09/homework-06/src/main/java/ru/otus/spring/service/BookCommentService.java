package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;

public interface BookCommentService {
    BookComment add(String text, Book book);
    BookComment save(BookComment bookComment);
    void delete(BookComment bookComment);
}
