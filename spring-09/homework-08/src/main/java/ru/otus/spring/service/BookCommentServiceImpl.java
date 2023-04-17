package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.repository.BookCommentRepository;

import java.util.List;
import java.util.Optional;

@Component
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;
    private final BookService bookService;

    public BookCommentServiceImpl(
            BookCommentRepository bookCommentRepository,
            BookService bookService
    ) {
        this.bookCommentRepository = bookCommentRepository;
        this.bookService = bookService;
    }

    @Override
    public BookComment add(String text, String bookName, String authorName) {
        Optional<Book> findBook = bookService.findByNameAndAuthorName(bookName, authorName);
        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("There is no book with name=" + bookName
                    + " and author's name=" + authorName);
        }
        return add(text, findBook.get());
    }

    @Override
    public BookComment add(String text, Book book) {
        return bookCommentRepository.insert(new BookComment(text, book));
    }

    @Override
    public BookComment save(BookComment bookComment) {
        return bookCommentRepository.save(bookComment);
    }

    @Override
    public void delete(BookComment bookComment) {
        bookCommentRepository.delete(bookComment);
    }

    @Override
    public List<BookComment> findByBookAndAuthor(String bookName, String authorName) {
        Optional<Book> findBook = bookService.findByNameAndAuthorName(bookName, authorName);
        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("There is no book with name=" + bookName
                    + " and author's name=" + authorName);
        }
        return bookCommentRepository.findByBook(findBook.get());
    }
}
