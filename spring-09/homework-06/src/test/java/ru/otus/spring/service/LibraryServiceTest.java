package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
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
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookCommentRepository bookCommentRepository;
    @Mock
    private TransactionalService transactionalService;
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        transactionalService = new TransactionalServiceImpl();
        libraryService = new LibraryServiceImpl(
                genreRepository,
                authorRepository,
                bookRepository,
                bookCommentRepository,
                transactionalService
        );
    }

    @Test
    void addGenreTest() {
        assertThatThrownBy(() -> libraryService.addGenre(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Genre genre = new Genre(1L, "test-add-genre");
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        Genre insertedGenre = libraryService.addGenre("test-add-genre");
        assertThat(insertedGenre).isEqualTo(genre);
        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void deleteGenreTest() {
        assertThatThrownBy(() -> libraryService.deleteGenre(new Genre(null,null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Genre genreWithoutId = new Genre("test-delete-genre");
        Genre genreWithId = new Genre(1L,"test-delete-genre-with-id");

        doNothing().when(genreRepository).deleteById(any(Long.class));

        libraryService.deleteGenre(genreWithId);
        verify(genreRepository, times(1)).deleteById(1L);

        when(libraryService.findGenreByName("test-delete-genre"))
                .thenReturn(Optional.of(new Genre(2L,"test-delete-genre")));
        libraryService.deleteGenre(genreWithoutId);
        verify(genreRepository, times(1)).findByName(anyString());
        verify(genreRepository, times(1)).deleteById(2L);
    }

    @Test
    void findGenreByNameTest() {
        Genre genre = new Genre(1L, "test-find-genre-by-name");
        when(genreRepository.findByName("test-find-genre-by-name")).thenReturn(Optional.of(genre));

        Optional<Genre> findGenre = libraryService.findGenreByName("test-find-genre-by-name");
        verify(genreRepository, times(1)).findByName(anyString());
        assertThat(findGenre).isEqualTo(Optional.of(genre));
    }

    @Test
    void getAllGenresTest() {
        when(genreRepository.getAll()).thenReturn(
                List.of(
                        new Genre(1L, "test-all-genres"),
                        new Genre(2L, "test-all-genres")));

        List<Genre> genres = libraryService.getAllGenres();
        verify(genreRepository,times(1)).getAll();
        assertThat(genres).containsExactlyInAnyOrder(
                new Genre(1L, "test-all-genres"),
                new Genre(2L, "test-all-genres"));
    }

    @Test
    void addAuthorTest() {
        assertThatThrownBy(() -> libraryService.addAuthor(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Author author = new Author(1L, "test-add-author");
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author insertedAuthor = libraryService.addAuthor("test-add-author");
        assertThat(insertedAuthor).isEqualTo(author);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void deleteAuthorTest() {
        assertThatThrownBy(() -> libraryService.deleteAuthor(new Author(null,null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Author authorWithoutId = new Author("test-delete-author");
        Author authorWithId = new Author(1L, "test-delete-author-with-id");

        doNothing().when(authorRepository).deleteById(any(Long.class));

        libraryService.deleteAuthor(authorWithId);
        verify(authorRepository, times(1)).deleteById(1L);

        when(libraryService.findAuthorByName("test-delete-author"))
                .thenReturn(Optional.of(new Author(2L, "test-delete-author")));
        libraryService.deleteAuthor(authorWithoutId);
        verify(authorRepository, times(1)).findByName(anyString());
        verify(authorRepository, times(1)).deleteById(2L);
    }

    @Test
    void findAuthorByNameTest() {
        Author author = new Author(1L, "test-find-author-by-name");
        when(authorRepository.findByName("test-find-author-by-name")).thenReturn(Optional.of(author));

        Optional<Author> findAuthor = libraryService.findAuthorByName("test-find-author-by-name");
        verify(authorRepository, times(1)).findByName(anyString());
        assertThat(findAuthor).isEqualTo(Optional.of(author));
    }

    @Test
    void getAllAuthorsTest() {
        when(authorRepository.getAll()).thenReturn(
                List.of(
                        new Author(1L, "test-all-authors"),
                        new Author(2L, "test-all-authors")));
        List<Author> authors = libraryService.getAllAuthors();
        verify(authorRepository, times(1)).getAll();
        assertThat(authors).containsExactlyInAnyOrder(
                new Author(1L, "test-all-authors"),
                new Author(2L, "test-all-authors"));
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
                new Book(
                        "test-add-book",
                        "",
                        1990,
                        new Author(1L, "test-add-book"),
                        new Genre(1L, "test-add-book")));
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(0)).save(any(Author.class));
        verify(genreRepository, times(0)).save(any(Genre.class));
        assertThat(insertedBook).isEqualTo(book);

        when(authorRepository.save(any(Author.class)))
                .thenReturn(new Author(1L, "test-add-book"));
        when(genreRepository.save(any(Genre.class)))
                .thenReturn(new Genre(1L, "test-add-book"));
        Book insertedBookAddAuthorGenre = libraryService.addBook(
                new Book(
                        "test-add-book",
                        "",
                        1990,
                        new Author("test-add-book"),
                        new Genre("test-add-book")));
        verify(bookRepository, times(2)).save(any(Book.class));
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(genreRepository, times(1)).save(any(Genre.class));
        assertThat(insertedBookAddAuthorGenre).isEqualTo(book);

        assertThatThrownBy(() -> libraryService.addBook(
                new Book(null,null,1, null, null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.addBook(
                new Book("null",null,1, null, null)))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.addBook(
                new Book("null",null,1, new Author(""), null)))
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
                new Book(
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre(1L, "test-update-book")));
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookRepository,times(1)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookRepository, times(0)).getById(anyLong());
        verify(authorRepository, times(0)).save(any(Author.class));
        verify(genreRepository, times(0)).save(any(Genre.class));

        Book updatedBookGenre = libraryService.updateBook(
                new Book(
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre("test-update-book-new")));
        verify(bookRepository, times(2)).save(any(Book.class));
        verify(bookRepository,times(2)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookRepository, times(0)).getById(anyLong());
        verify(authorRepository, times(0)).save(any(Author.class));
        verify(genreRepository, times(1)).save(any(Genre.class));
        assertThat(updatedBookGenre).isEqualTo(book);

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
                new Book(null,null,1, null, null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book("null",null,1, null, null)))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book("null",null,1, new Author(""), null)))
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

        doNothing().when(bookRepository).deleteById(anyLong());
        libraryService.deleteBook(book);
        verify(bookRepository, times(1)).deleteById(anyLong());

        when(bookRepository.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.of(book));
        libraryService.deleteBook(
                new Book(
                        "test-delete-book",
                        "",
                        1991,
                        new Author(1L, "test-delete-book"),
                        new Genre(1L, "test-delete-book")));
        verify(bookRepository, times(1)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookRepository, times(2)).deleteById(anyLong());

        when(bookRepository.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> libraryService.deleteBook(
                new Book(
                        "test-delete-book",
                        "",
                        1991,
                        new Author(1L, "test-delete-book"),
                        new Genre(1L, "test-delete-book"))))
                .isExactlyInstanceOf(IllegalArgumentException.class);
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

    @Test
    void addBookCommentTest() {
        Book book = new Book(
                1L,
                "test-add-comment",
                "",
                1991,
                new Author(1L, "test-add-comment"),
                new Genre(1L, "test-add-comment"),
                new ArrayList<>());
        BookComment bookComment = new BookComment("test-add-comment", book);
        when(bookCommentRepository.save(any(BookComment.class))).thenReturn(bookComment);

        BookComment savedComment = libraryService.addBookComment("test-add-comment", book);
        verify(bookCommentRepository, times(1)).save(any(BookComment.class));

        assertThatThrownBy(() -> libraryService.addBookComment(null, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.addBookComment("null", null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.addBookComment("", new Book()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteBookCommentTest() {
        Book book = new Book(
                1L,
                "test-delete-comment",
                "",
                1991,
                new Author(1L, "test-delete-comment"),
                new Genre(1L, "test-delete-comment"),
                new ArrayList<>());
        BookComment bookComment = new BookComment(1L, "test-delete-comment", book);

        doNothing().when(bookCommentRepository).deleteById(anyLong());

        libraryService.deleteBookComment(bookComment);
        verify(bookCommentRepository, times(1)).deleteById(anyLong());

        assertThatThrownBy(() -> libraryService.deleteBookComment(new BookComment()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.deleteBookComment(new BookComment("", new Book())))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> libraryService.deleteBookComment(new BookComment("test", null)))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void getBookCommentsTest() {
        Book book = new Book(
                1L,
                "test-get-comments",
                "",
                1991,
                new Author(1L, "test-get-comments"),
                new Genre(1L, "test-get-comments"),
                new ArrayList<>());
        BookComment bookComment = new BookComment(1L, "test-get-comments", book);
        when(bookCommentRepository.getByBook(any(Book.class)))
                .thenReturn(new ArrayList<>(List.of(bookComment)));

        List<BookComment> comments = libraryService.getBookComments(book);
        verify(bookCommentRepository, times(1)).getByBook(any(Book.class));
    }
}
