package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private AuthorService authorService;
    @Mock
    private GenreService genreService;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void addTest() {
        Book book = Book.builder()
                .id("1")
                .name("test-add-book")
                .yearOfRelease(2000)
                .author(new Author("test-add-book"))
                .genre(new Genre("test-add-book"))
                .build();

        when(bookRepository.insert(any(Book.class))).thenReturn(book);
        assertThat(bookService.add(new Book())).isEqualTo(book);
    }

    @Test
    public void addWithRefsTest() {
        Book book = Book.builder()
                .id("1")
                .name("test-add-book")
                .yearOfRelease(2000)
                .author(new Author("1", "test-add-book"))
                .genre(new Genre("1", "test-add-book"))
                .build();

        when(authorService.getOrAdd("test-add-book"))
                .thenReturn(new Author("1", "test-add-book"));
        when(genreService.getOrAdd("test-add-book"))
                .thenReturn(new Genre("1", "test-add-book"));
        when(bookRepository.insert(any(Book.class))).thenReturn(book);
        assertThat(bookService.addWithRefs(book)).isEqualTo(book);
    }

    @Test
    public void updateTest() {
        Book book = Book.builder()
                .id("1")
                .name("test-update-book")
                .yearOfRelease(2000)
                .author(new Author("1", "test-update-book"))
                .genre(new Genre("1", "test-update-book"))
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        assertThat(bookService.update(book)).isEqualTo(book);
    }

    @Test
    public void deleteTest() {
        doNothing().when(bookRepository).delete(any(Book.class));
        bookService.delete(new Book());
    }

    @Test
    public void findByNameAndAuthorNameTest() {
        Book book = Book.builder()
                .name("test-f1-book")
                .yearOfRelease(2000)
                .author(new Author("test-f1-book"))
                .genre(new Genre("test-f1-book"))
                .build();

        when(bookRepository.findByNameAndAuthorName("test-f1-book", "test-f1-book"))
                .thenReturn(Optional.of(book));
        assertThat(bookService.findByNameAndAuthorName("test-f1-book", "test-f1-book"))
                .isEqualTo(Optional.of(book));
    }

    @Test
    public void findByAuthorTest() {
        Author author = new Author("test-f2-book");
        Genre genre = new Genre("test-f2-book");

        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .author(author)
                .genre(genre)
                .build();
        when(bookRepository.findByAuthor(author)).thenReturn(List.of(book));
        assertThat(bookService.findByAuthor(author)).containsExactly(book);
    }

    @Test
    public void findByAuthorNameTest() {
        Author author = new Author("test-f2-book");
        Genre genre = new Genre("test-f2-book");

        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .author(author)
                .genre(genre)
                .build();
        when(bookRepository.findByAuthorName("test-f2-book")).thenReturn(List.of(book));
        assertThat(bookService.findByAuthorName("test-f2-book")).containsExactly(book);
    }

    @Test
    public void findByGenreTest() {
        Author author = new Author("test-f2-book");
        Genre genre = new Genre("test-f2-book");

        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .author(author)
                .genre(genre)
                .build();
        when(bookRepository.findByGenre(genre)).thenReturn(List.of(book));
        assertThat(bookService.findByGenre(genre)).containsExactly(book);
    }

    //List<Book> findByYearOfReleaseAndGenre(int yearOfRelease, Genre genre) {
    //        return bookRepository.findByYearOfReleaseAndGenreName(yearOfRelease, genre.getName());
    @Test
    public void findByYearOfReleaseAndGenreTest() {
        Author author = new Author("test-f2-book");
        Genre genre = new Genre("test-f2-book");

        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .author(author)
                .genre(genre)
                .build();

        when(bookRepository.findByYearOfReleaseAndGenreName(2000, "test-f2-book"))
                .thenReturn(List.of(book));
        assertThat(bookService.findByYearOfReleaseAndGenre(2000, genre)).containsExactly(book);
    }

    @Test
    public void findAllTest() {
        Author author = new Author("test-f2-book");
        Genre genre = new Genre("test-f2-book");

        Book book = Book.builder()
                .name("test-f2-book")
                .yearOfRelease(2000)
                .author(author)
                .genre(genre)
                .build();

        when(bookRepository.findAll()).thenReturn(List.of(book));
        assertThat(bookService.findAll()).containsExactly(book);
    }

    @Test
    public void existsByAuthorTest() {
        Author author = new Author("test-exists-author");
        when(bookRepository.existsByAuthor(author)).thenReturn(true);
        assertThat(bookService.existsByAuthor(author)).isTrue();

        Author author2 = new Author("test-exists-author-2");
        when(bookRepository.existsByAuthor(author2)).thenReturn(false);
        assertThat(bookService.existsByAuthor(author2)).isFalse();
    }

    @Test
    public void existsByGenreTest() {
        Genre genre = new Genre("test-exists-genre");
        when(bookRepository.existsByGenre(genre)).thenReturn(true);
        assertThat(bookService.existsByGenre(genre)).isTrue();

        Genre genre2 = new Genre("test-exists-genre-2");
        when(bookRepository.existsByGenre(genre2)).thenReturn(false);
        assertThat(bookService.existsByGenre(genre2)).isFalse();
    }
}
