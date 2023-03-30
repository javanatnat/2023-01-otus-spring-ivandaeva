package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {
    @Mock
    private AuthorService authorService;
    @Mock
    private GenreService genreService;
    @Mock
    private BookCommentService bookCommentService;
    @Mock
    private BookRepository bookRepository;
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryServiceImpl(
                genreService,
                authorService,
                bookCommentService,
                bookRepository
        );
    }

    @Test
    void addBookTest() {
        Book book = new Book(1L,
                "test-add-book",
                "",
                1990,
                new Author(1L, "test-add-book"),
                new Genre(1L, "test-add-book"),
                new ArrayList<>());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book insertedBook = libraryService.addBook(
                Book.builder()
                        .name("test-add-book")
                        .yearOfRelease(1990)
                        .author(new Author(1L, "test-add-book"))
                        .genre(new Genre(1L, "test-add-book"))
                        .build());
        verify(bookRepository, times(1)).save(any(Book.class));

        assertThatThrownBy(() -> libraryService.addBook(
                Book.builder().build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.addBook(
                Book.builder().name("null").build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.addBook(
                Book.builder().name("null").author(new Author("")).build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.addBook(
                new Book(1L, "null",null,1,
                        new Author(""), new Genre(""), new ArrayList<>())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateBookTest() {
        Book book = new Book(
                1L,
                "test-update-book",
                "",
                1991,
                new Author(1L, "test-update-book"),
                new Genre(1L, "test-update-book"),
                new ArrayList<>());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findByNameAndAuthor(anyString(), any(Author.class)))
                .thenReturn(Optional.of(book));

        Book updatedBook = libraryService.updateBook(
                Book.builder()
                        .name("test-update-book")
                        .yearOfRelease(1990)
                        .author(new Author(1L, "test-update-book"))
                        .genre(new Genre(1L, "test-update-book"))
                        .build());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookRepository,times(1)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookRepository, times(0)).getById(anyLong());

        Book updatedBookGenre = libraryService.updateBook(
                Book.builder()
                        .name("test-update-book")
                        .yearOfRelease(1990)
                        .author(new Author(1L, "test-update-book"))
                        .genre(new Genre("test-update-book-new"))
                        .build());
        verify(bookRepository, times(2)).save(any(Book.class));
        verify(bookRepository,times(2)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookRepository, times(0)).getById(anyLong());

        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(2L,
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre(1L, "test-update-book"),
                        new ArrayList<>())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(1L,
                        "test-update-book-new",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre(1L, "test-update-book"),
                        new ArrayList<>())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(1L,
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book-new"),
                        new Genre(1L, "test-update-book"),
                        new ArrayList<>())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                Book.builder().build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                Book.builder().name("null").build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                Book.builder().name("null").author(new Author("")).build()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(1L, "null",null,1,
                        new Author(""), new Genre(""), new ArrayList<>())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteBookTest() {
        Book book = new Book(
                1L,
                "test-delete-book",
                "",
                1991,
                new Author(1L, "test-delete-book"),
                new Genre(1L, "test-delete-book"),
                new ArrayList<>());

        doNothing().when(bookRepository).delete(any(Book.class));
        libraryService.deleteBook(book);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void findBookByNameAndAuthorTest() {
        Book book = new Book(
                1L,
                "test-find-name-author-book",
                "",
                1991,
                new Author(1L, "test-find-name-author-book"),
                new Genre(1L, "test-find-name-author-book"),
                new ArrayList<>());
        when(bookRepository.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.of(book));

        Optional<Book> findBook = libraryService.findBookByNameAndAuthor(
                "test-find-name-author-book",
                new Author(1L, "test-find-name-author-book"));
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(book);
        verify(bookRepository, times(1)).findByNameAndAuthor(anyString(), any(Author.class));

        assertThatThrownBy(() -> libraryService.findBookByNameAndAuthor(
                "", new Author(1L, "test-find-name-author-book")))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.findBookByNameAndAuthor(
                "test", new Author(1L, "")))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findBooksByAuthorTest() {
        Book book = new Book(
                1L,
                "test-find-author-book",
                "",
                1991,
                new Author(1L, "test-find-author-book"),
                new Genre(1L, "test-find-author-book"),
                new ArrayList<>());
        when(bookRepository.findBooksByAuthor(any(Author.class))).thenReturn(List.of(book));

        List<Book> books = libraryService.findBooksByAuthor(new Author(1L, "test-find-author-book"));
        assertThat(books).containsExactly(book);
        verify(bookRepository, times(1)).findBooksByAuthor(any(Author.class));
    }

    @Test
    void findBooksByReleaseYearAndGenreTest() {
        Book book = new Book(
                1L,
                "test-find-year-genre-book",
                "",
                1991,
                new Author(1L, "test-find-year-genre-book"),
                new Genre(1L, "test-find-year-genre-book"),
                new ArrayList<>());
        when(bookRepository.findBooksByReleaseYearAndGenre(anyInt(), any(Genre.class))).thenReturn(List.of(book));

        List<Book> books = libraryService.findBooksByReleaseYearAndGenre(
                1991, new Genre(1L, "test-find-year-genre-book"));
        assertThat(books).containsExactly(book);
        verify(bookRepository, times(1))
                .findBooksByReleaseYearAndGenre(anyInt(), any(Genre.class));
    }

    @Test
    void getAllBooksTest() {
        Book book = new Book(
                1L,
                "test-all-books-book",
                "",
                1991,
                new Author(1L, "test-all-books-book"),
                new Genre(1L, "test-all-books-book"),
                new ArrayList<>());
        when(bookRepository.getAll()).thenReturn(List.of(book));

        List<Book> books = libraryService.getAllBooks();
        assertThat(books).containsExactly(book);
        verify(bookRepository, times(1)).getAll();
    }
}
