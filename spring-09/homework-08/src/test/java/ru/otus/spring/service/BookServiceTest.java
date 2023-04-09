package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@Import({
        BookServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class
})
public class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;

    @Test
    public void addTest() {
        Book book = Book.builder()
                .name("test-add-book")
                .yearOfRelease(2000)
                .authorName("test-add-book")
                .genreName("test-add-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-add-book")).isPresent();
        assertThat(genreRepository.findByName("test-add-book")).isPresent();

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-add-book");
        genreRepository.deleteByName("test-add-book");
    }

    @Test
    public void updateTest() {
        Book book = Book.builder()
                .name("test-update-book")
                .yearOfRelease(2000)
                .authorName("test-update-book")
                .genreName("test-update-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-update-book")).isPresent();
        assertThat(genreRepository.findByName("test-update-book")).isPresent();

        addBook.setDescription("new description");
        Book updated = bookService.update(addBook);
        assertThat(updated).isEqualTo(addBook);
        assertThat(authorRepository.findByName("test-update-book")).isPresent();
        assertThat(genreRepository.findByName("test-update-book")).isPresent();

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-update-book");
        genreRepository.deleteByName("test-update-book");
    }

    @Test
    public void deleteTest() {
        Book book = Book.builder()
                .name("test-delete-book")
                .yearOfRelease(2000)
                .authorName("test-delete-book")
                .genreName("test-delete-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-delete-book")).isPresent();
        assertThat(genreRepository.findByName("test-delete-book")).isPresent();

        bookService.delete(addBook);
        assertThat(bookRepository.findById(addBook.getId())).isEmpty();
        assertThat(authorRepository.findByName("test-delete-book")).isPresent();
        assertThat(genreRepository.findByName("test-delete-book")).isPresent();

        authorRepository.deleteByName("test-delete-book");
        genreRepository.deleteByName("test-delete-book");
    }

    @Test
    public void findByNameAndAuthorNameTest() {
        Book book = Book.builder()
                .name("test-f1-book")
                .yearOfRelease(2000)
                .authorName("test-f1-book")
                .genreName("test-f1-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-f1-book")).isPresent();
        assertThat(genreRepository.findByName("test-f1-book")).isPresent();

        Optional<Book> findBook = bookService.findByNameAndAuthorName(
                "test-f1-book",
                "test-f1-book");
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(addBook);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-f1-book");
        genreRepository.deleteByName("test-f1-book");
    }

    @Test
    public void findByAuthorTest() {
        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .authorName("test-f2-book")
                .genreName("test-f2-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-f2-book")).isPresent();
        assertThat(genreRepository.findByName("test-f2-book")).isPresent();

        List<Book> books = bookService.findByAuthor(new Author("test-f2-book"));
        assertThat(books).containsExactly(addBook);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-f2-book");
        genreRepository.deleteByName("test-f2-book");
    }

    @Test
    public void findByYearOfReleaseAndGenreTest() {
        Book book = Book.builder()
                .name("test-f3-book")
                .yearOfRelease(2000)
                .authorName("test-f3-book")
                .genreName("test-f3-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-f3-book")).isPresent();
        assertThat(genreRepository.findByName("test-f3-book")).isPresent();

        List<Book> books = bookService.findByYearOfReleaseAndGenre(
                2000,
                new Genre("test-f3-book"));
        assertThat(books).containsExactly(addBook);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-f3-book");
        genreRepository.deleteByName("test-f3-book");
    }

    @Test
    public void findAllTest() {
        Book book = Book.builder()
                .name("test-f4-book")
                .yearOfRelease(2000)
                .authorName("test-f4-book")
                .genreName("test-f4-book")
                .build();

        Book addBook = bookService.add(book);
        assertThat(addBook.getId()).isNotBlank();
        assertThat(authorRepository.findByName("test-f4-book")).isPresent();
        assertThat(genreRepository.findByName("test-f4-book")).isPresent();

        List<Book> books = bookService.findAll();
        assertThat(books).contains(addBook);

        bookRepository.delete(addBook);
        authorRepository.deleteByName("test-f4-book");
        genreRepository.deleteByName("test-f4-book");
    }
}
