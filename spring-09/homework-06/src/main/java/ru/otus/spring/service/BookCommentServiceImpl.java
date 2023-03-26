package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.repositories.BookCommentRepository;

import java.util.Objects;

@Component
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    public  BookCommentServiceImpl(BookCommentRepository bookCommentRepository) {
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    @Transactional
    public BookComment add(String text, Book book) {
        checkString(text);
        Objects.requireNonNull(book);
        return bookCommentRepository.save(new BookComment(text, book));
    }

    @Override
    @Transactional
    public BookComment save(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        return bookCommentRepository.save(bookComment);
    }

    @Override
    @Transactional
    public void delete(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        Objects.requireNonNull(bookComment.getBook());
        bookCommentRepository.delete(bookComment);
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
