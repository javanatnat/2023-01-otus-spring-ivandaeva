package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({
        BookCommentServiceImpl.class,
        BookServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class
})
public class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    @Test
    public void insertTest() {
        Book book = bookService.add(Book.builder()
                .name("insertTest")
                .author(new Author("insertTest"))
                .genre(new Genre("insertTest"))
                .yearOfRelease(1990)
                .build());
        assertThat(book.getId()).isNotNull();

        assertThat(bookRepository.findById(book.getId())).isPresent();
        assertThat(bookService.findByNameAndAuthorName("insertTest", "insertTest")).isPresent();
        assertThat(bookService.findByNameAndAuthor("insertTest", new Author("insertTest"))).isPresent();
        assertThat(bookService.findByAuthor(new Author("insertTest"))).containsExactly(book);
        assertThat(bookService.findByYearOfReleaseAndGenre(1990, new Genre("insertTest")))
                .containsExactly(book);
    }

    @Test
    public void updateTest() {
        Book book = bookService.add(Book.builder()
                .name("updateTest")
                .author(new Author("updateTest"))
                .genre(new Genre("updateTest"))
                .yearOfRelease(1990)
                .build());
        assertThat(book.getId()).isNotNull();

        book.setGenre(new Genre("updateTest update"));
        book.setYearOfRelease(1991);
        Book updatedBook = bookService.update(book);
        Optional<Book> findBook = bookRepository.findById(book.getId());
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(updatedBook);
    }

    @Test
    public void deleteTest() {
        Book book = bookService.add(Book.builder()
                .name("deleteTest")
                .author(new Author("deleteTest"))
                .genre(new Genre("deleteTest"))
                .yearOfRelease(1990)
                .build());
        assertThat(book.getId()).isNotNull();

        assertThat(bookRepository.findById(book.getId())).isPresent();
        bookService.delete(book);
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}
