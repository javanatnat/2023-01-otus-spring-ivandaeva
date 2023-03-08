package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceJdbcTest {
    @Mock
    private AuthorDao authorDao;

    @Mock
    private GenreDao genreDao;

    @Mock
    private BookDao bookDao;

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryServiceJdbcImpl(genreDao, authorDao, bookDao);
    }

    @Test
    void addGenreTest() {
        assertThatThrownBy(() -> libraryService.addGenre(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Genre genre = new Genre(1L, "test-add-genre");
        when(genreDao.insert(any(Genre.class))).thenReturn(genre);

        Genre insertedGenre = libraryService.addGenre("test-add-genre");
        assertThat(insertedGenre).isEqualTo(genre);
        verify(genreDao, times(1)).insert(any(Genre.class));
    }

    @Test
    void deleteGenreTest() {
        assertThatThrownBy(() -> libraryService.deleteGenre(new Genre(null,null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Genre genreWithoutId = new Genre("test-delete-genre");
        Genre genreWithId = new Genre(1L,"test-delete-genre-with-id");

        doNothing().when(genreDao).deleteById(any(Long.class));

        libraryService.deleteGenre(genreWithId);
        verify(genreDao, times(1)).deleteById(1L);

        when(libraryService.findGenreByName(anyString()))
                .thenReturn(Optional.of(new Genre(2L,"test-delete-genre")));
        libraryService.deleteGenre(genreWithoutId);
        verify(genreDao, times(1)).findByName(anyString());
        verify(genreDao, times(1)).deleteById(2L);
    }

    @Test
    void findGenreByNameTest() {
        Genre genre = new Genre(1L, "test-find-genre-by-name");
        when(genreDao.findByName(anyString())).thenReturn(Optional.of(genre));

        Optional<Genre> findGenre = libraryService.findGenreByName("test-find-genre-by-name");
        verify(genreDao, times(1)).findByName(anyString());
        assertThat(findGenre).isEqualTo(Optional.of(genre));
    }

    @Test
    void getAllGenresTest() {
        when(genreDao.getAll()).thenReturn(
                List.of(
                        new Genre(1L, "test-all-genres"),
                        new Genre(2L, "test-all-genres")));

        List<Genre> genres = libraryService.getAllGenres();
        verify(genreDao,times(1)).getAll();
        assertThat(genres).containsExactlyInAnyOrder(
                new Genre(1L, "test-all-genres"),
                new Genre(2L, "test-all-genres"));
    }

    @Test
    void addAuthorTest() {
        assertThatThrownBy(() -> libraryService.addAuthor(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Author author = new Author(1L, "test-add-author");
        when(authorDao.insert(any(Author.class))).thenReturn(author);

        Author insertedAuthor = libraryService.addAuthor("test-add-author");
        assertThat(insertedAuthor).isEqualTo(author);
        verify(authorDao, times(1)).insert(any(Author.class));
    }

    @Test
    void deleteAuthorTest() {
        assertThatThrownBy(() -> libraryService.deleteAuthor(new Author(null,null)))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        Author authorWithoutId = new Author("test-delete-author");
        Author authorWithId = new Author(1L, "test-delete-author-with-id");

        doNothing().when(authorDao).deleteById(any(Long.class));

        libraryService.deleteAuthor(authorWithId);
        verify(authorDao, times(1)).deleteById(1L);

        when(libraryService.findAuthorByName(anyString()))
                .thenReturn(Optional.of(new Author(2L, "test-delete-author")));
        libraryService.deleteAuthor(authorWithoutId);
        verify(authorDao, times(1)).findByName(anyString());
        verify(authorDao, times(1)).deleteById(2L);
    }

    @Test
    void findAuthorByNameTest() {
        Author author = new Author(1L, "test-find-author-by-name");
        when(authorDao.findByName(anyString())).thenReturn(Optional.of(author));

        Optional<Author> findAuthor = libraryService.findAuthorByName("test-find-author-by-name");
        verify(authorDao, times(1)).findByName(anyString());
        assertThat(findAuthor).isEqualTo(Optional.of(author));
    }

    @Test
    void getAllAuthorsTest() {
        when(authorDao.getAll()).thenReturn(
                List.of(
                        new Author(1L, "test-all-authors"),
                        new Author(2L, "test-all-authors")));
        List<Author> authors = libraryService.getAllAuthors();
        verify(authorDao, times(1)).getAll();
        assertThat(authors).containsExactlyInAnyOrder(
                new Author(1L, "test-all-authors"),
                new Author(2L, "test-all-authors"));
    }

    @Test
    void addBookTest() {
        Book book = new Book(
                1L,
                "test-add-book",
                "",
                1990,
                new Author(1L, "test-add-book"),
                new Genre(1L, "test-add-book"));
        when(bookDao.insert(any(Book.class))).thenReturn(book);

        Book insertedBook = libraryService.addBook(
                new Book(
                        "test-add-book",
                        "",
                        1990,
                        new Author(1L, "test-add-book"),
                        new Genre(1L, "test-add-book")));
        verify(bookDao, times(1)).insert(any(Book.class));
        verify(authorDao, times(0)).insert(any(Author.class));
        verify(genreDao, times(0)).insert(any(Genre.class));
        assertThat(insertedBook).isEqualTo(book);

        when(authorDao.insert(any(Author.class))).thenReturn(new Author(1L, "test-add-book"));
        when(genreDao.insert(any(Genre.class))).thenReturn(new Genre(1L, "test-add-book"));
        Book insertedBookAddAuthorGenre = libraryService.addBook(
                new Book(
                        "test-add-book",
                        "",
                        1990,
                        new Author("test-add-book"),
                        new Genre("test-add-book")));
        verify(bookDao, times(2)).insert(any(Book.class));
        verify(authorDao, times(1)).insert(any(Author.class));
        verify(genreDao, times(1)).insert(any(Genre.class));
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
                new Book(1L, "null",null,1, new Author(""), new Genre(""))))
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
                new Genre(1L, "test-update-book"));
        when(bookDao.update(any(Book.class))).thenReturn(book);
        when(bookDao.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.of(book));

        Book updatedBook = libraryService.updateBook(
                new Book(
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre(1L, "test-update-book")));
        verify(bookDao, times(1)).update(any(Book.class));
        verify(bookDao,times(1)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookDao, times(0)).getById(anyLong());
        verify(authorDao, times(0)).insert(any(Author.class));
        verify(genreDao, times(0)).insert(any(Genre.class));
        assertThat(updatedBook).isEqualTo(book);

        Book updatedBookGenre = libraryService.updateBook(
                new Book(
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre("test-update-book-new")));
        verify(bookDao, times(2)).update(any(Book.class));
        verify(bookDao,times(2)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookDao, times(0)).getById(anyLong());
        verify(authorDao, times(0)).insert(any(Author.class));
        verify(genreDao, times(1)).insert(any(Genre.class));
        assertThat(updatedBookGenre).isEqualTo(book);

        assertThatThrownBy(() -> libraryService.updateBook(
                        new Book(2L,
                                "test-update-book",
                                "",
                                1990,
                                new Author(1L, "test-update-book"),
                                new Genre(1L, "test-update-book"))))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(1L,
                        "test-update-book-new",
                        "",
                        1990,
                        new Author(1L, "test-update-book"),
                        new Genre(1L, "test-update-book"))))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> libraryService.updateBook(
                new Book(1L,
                        "test-update-book",
                        "",
                        1990,
                        new Author(1L, "test-update-book-new"),
                        new Genre(1L, "test-update-book"))))
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
                new Book(1L, "null",null,1, new Author(""), new Genre(""))))
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
                new Genre(1L, "test-delete-book"));

        doNothing().when(bookDao).deleteById(anyLong());
        libraryService.deleteBook(book);
        verify(bookDao, times(1)).deleteById(anyLong());

        when(bookDao.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.of(book));
        libraryService.deleteBook(
                new Book(
                        "test-delete-book",
                        "",
                        1991,
                        new Author(1L, "test-delete-book"),
                        new Genre(1L, "test-delete-book")));
        verify(bookDao, times(1)).findByNameAndAuthor(anyString(), any(Author.class));
        verify(bookDao, times(2)).deleteById(anyLong());

        when(bookDao.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.empty());
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
                new Genre(1L, "test-find-name-author-book"));
        when(bookDao.findByNameAndAuthor(anyString(), any(Author.class))).thenReturn(Optional.of(book));

        Optional<Book> findBook = libraryService.findBookByNameAndAuthor(
                "test-find-name-author-book",
                new Author(1L, "test-find-name-author-book"));
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(book);
        verify(bookDao, times(1)).findByNameAndAuthor(anyString(), any(Author.class));

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
                new Genre(1L, "test-find-author-book"));
        when(bookDao.findBooksByAuthor(any(Author.class))).thenReturn(List.of(book));

        List<Book> books = libraryService.findBooksByAuthor(new Author(1L, "test-find-author-book"));
        assertThat(books).containsExactly(book);
        verify(bookDao, times(1)).findBooksByAuthor(any(Author.class));
    }

    @Test
    void findBooksByReleaseYearAndGenreTest() {
        Book book = new Book(
                1L,
                "test-find-year-genre-book",
                "",
                1991,
                new Author(1L, "test-find-year-genre-book"),
                new Genre(1L, "test-find-year-genre-book"));
        when(bookDao.findBooksByReleaseYearAndGenre(anyInt(), any(Genre.class))).thenReturn(List.of(book));

        List<Book> books = libraryService.findBooksByReleaseYearAndGenre(
                1991, new Genre(1L, "test-find-year-genre-book"));
        assertThat(books).containsExactly(book);
        verify(bookDao, times(1))
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
                new Genre(1L, "test-all-books-book"));
        when(bookDao.getAll()).thenReturn(List.of(book));

        List<Book> books = libraryService.getAllBooks();
        assertThat(books).containsExactly(book);
        verify(bookDao, times(1))
                .getAll();
    }
}
