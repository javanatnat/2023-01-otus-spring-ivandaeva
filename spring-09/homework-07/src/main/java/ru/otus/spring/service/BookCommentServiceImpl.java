package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.repository.BookCommentRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;
    private final LibraryService libraryService;

    public BookCommentServiceImpl(
            BookCommentRepository bookCommentRepository,
            LibraryService libraryService
    ) {
        this.bookCommentRepository = bookCommentRepository;
        this.libraryService = libraryService;
    }

    @Override
    @Transactional
    public BookComment add(String text, String bookName, String authorName) {
        checkString(text);
        checkString(bookName);
        checkString(authorName);

        Optional<Book> findBook = libraryService.findByNameAndAuthorName(bookName, authorName);
        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("Book with name = " + bookName
                    + " and author.name = " + authorName
                    + " has not found in library");
        }
        return add(text, findBook.get());
    }

    @Override
    public BookComment add(String text, Book book) {
        Objects.requireNonNull(book);
        checkString(text);
        return save(new BookComment(text, book));
    }

    @Override
    public BookComment save(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        return bookCommentRepository.save(bookComment);
    }

    @Override
    @Transactional
    public void delete(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        if (isNew(bookComment)) {
            Objects.requireNonNull(bookComment.getBook());
            Objects.requireNonNull(bookComment.getBook().getAuthor());
            checkString(bookComment.getText());
            Optional<BookComment> findBookComment = bookCommentRepository
                    .findByTextAndBookNameAndBook_AuthorName(
                            bookComment.getText(),
                            bookComment.getBook().getName(),
                            bookComment.getBook().getAuthor().getName()
                    );
            findBookComment.ifPresent(bookCommentRepository::delete);
        } else {
            bookCommentRepository.deleteById(bookComment.getId());
        }
    }

    @Override
    public List<BookComment> findByBookAndAuthor(String bookName, String authorName) {
        return bookCommentRepository.findByBookNameAndBook_AuthorName(bookName, authorName);
    }

    @Override
    public boolean isNew(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        return bookComment.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
