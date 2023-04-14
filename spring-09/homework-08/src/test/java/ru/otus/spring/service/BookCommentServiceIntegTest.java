package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookCommentRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@Import({
        BookCommentServiceImpl.class,
        BookServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class
})
public class BookCommentServiceIntegTest {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private BookService bookService;

    @Test
    public void addTest() {
        Book book = Book.builder()
                .name("test-add-comment")
                .yearOfRelease(2000)
                .author(new Author("test-add-comment"))
                .genre(new Genre("test-add-comment"))
                .build();

        Book addBook = bookService.addWithRefs(book);
        BookComment bookComment1 = bookCommentService.add("test-add-comment", addBook);
        assertThat(bookComment1.getId()).isNotBlank();

        BookComment bookComment2 = bookCommentService.add(
                "test-add-comment-2",
                book.getName(),
                book.getAuthorName());
        assertThat(bookComment2.getId()).isNotBlank();

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-add-comment");
        genreRepository.deleteByName("test-add-comment");
        bookCommentRepository.deleteAll(List.of(bookComment1, bookComment2));
    }

    @Test
    public void saveTest() {
        Book book = Book.builder()
                .name("test-save-comment")
                .yearOfRelease(2000)
                .author(new Author("test-save-comment"))
                .genre(new Genre("test-save-comment"))
                .build();

        Book addBook = bookService.addWithRefs(book);
        BookComment bookComment = bookCommentService.save(new BookComment("test-save-comment", addBook));
        assertThat(bookComment.getId()).isNotBlank();

        bookComment.setText("test-save-comment-new");
        BookComment updated = bookCommentService.save(bookComment);
        assertThat(updated).isEqualTo(bookComment);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-save-comment");
        genreRepository.deleteByName("test-save-comment");
        bookCommentRepository.deleteAll(List.of(bookComment));
    }

    @Test
    public void deleteTest() {
        Book book = Book.builder()
                .name("test-delete-comment")
                .yearOfRelease(2000)
                .author(new Author("test-delete-comment"))
                .genre(new Genre("test-delete-comment"))
                .build();

        Book addBook = bookService.addWithRefs(book);
        BookComment bookComment = bookCommentService.save(new BookComment("test-delete-comment", addBook));
        assertThat(bookComment.getId()).isNotBlank();

        bookCommentService.delete(bookComment);
        Optional<BookComment> findBookComment = bookCommentRepository.findById(bookComment.getId());
        assertThat(findBookComment).isEmpty();

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-delete-comment");
        genreRepository.deleteByName("test-delete-comment");
        bookCommentRepository.deleteAll(List.of(bookComment));
    }

    @Test
    public void findByBookAndAuthorTest() {
        Book book = Book.builder()
                .name("test-find-comment")
                .yearOfRelease(2000)
                .author(new Author("test-find-comment"))
                .genre(new Genre("test-find-comment"))
                .build();

        Book addBook = bookService.addWithRefs(book);
        BookComment bookComment = bookCommentService.save(new BookComment("test-find-comment", addBook));
        assertThat(bookComment.getId()).isNotBlank();

        List<BookComment> bookComments = bookCommentService.findByBookAndAuthor(
                "test-find-comment",
                "test-find-comment");
        assertThat(bookComments).containsExactly(bookComment);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-find-comment");
        genreRepository.deleteByName("test-find-comment");
        bookCommentRepository.deleteAll(List.of(bookComment));
    }
}
