package ru.otus.spring.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        BookRepositoryJpa.class,
        GenreRepositoryJpa.class,
        AuthorRepositoryJpa.class
})
public class BookRepositoryJpaTest {
    @Autowired
    BookRepositoryJpa bookRepositoryJpa;
    @Autowired
    GenreRepositoryJpa genreRepositoryJpa;
    @Autowired
    AuthorRepositoryJpa authorRepositoryJpa;

    @Test
    void initTest() {
        assertThat(bookRepositoryJpa.count()).isEqualTo(0);
        assertThat(bookRepositoryJpa.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-count"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-count"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-count")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        Book insertedBook = bookRepositoryJpa.save(book);
        assertThat(bookRepositoryJpa.count()).isEqualTo(1);
    }

    @Test
    void saveTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-save"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-save"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-save")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        assertThat(book.getName()).isEqualTo("test-book-save");
        assertThat(book.getId()).isNotNull();
        assertThat(book.getDescription()).isNull();
        assertThat(book.getYearOfRelease()).isEqualTo(1999);
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getGenre()).isEqualTo(genre);
        assertThat(book.getComments()).isNull();
        assertThat(bookRepositoryJpa.getAll()).containsExactly(book);

        Book savedBook = bookRepositoryJpa.save(book);
        assertThat(savedBook).isEqualTo(book);
        assertThat(bookRepositoryJpa.getAll()).containsExactly(book);

        BookComment bookComment = new BookComment("test-book-save", book);
        List<BookComment> comments = new ArrayList<>(1);
        comments.add(bookComment);
        book.setComments(comments);
        book.setDescription("new-descr-test-book-save");
        savedBook = bookRepositoryJpa.save(book);
        assertThat(bookRepositoryJpa.count()).isEqualTo(1);
        assertThat(savedBook.getDescription()).isEqualTo("new-descr-test-book-save");

        List<BookComment> savedComments = savedBook.getComments();
        assertThat(savedComments.size()).isEqualTo(1);

        BookComment savedComment = savedComments.get(0);
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getText()).isEqualTo(bookComment.getText());
        assertThat(savedComment.getBook().getId()).isEqualTo(book.getId());
    }

    @Test
    void getByIdTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-get-by-id"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-get-by-id"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-get-by-id")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());

        assertThat(book.getId()).isNotNull();
        Optional<Book> findBook = bookRepositoryJpa.getById(book.getId());
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(book);

        assertThat(bookRepositoryJpa.getById(0L)).isEmpty();
    }

    @Test
    void getAllTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-get-all"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-get-all"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-get-all")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        assertThat(bookRepositoryJpa.getAll()).containsExactly(book);

        Book book2 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-get-all-2")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        assertThat(bookRepositoryJpa.getAll()).containsExactlyInAnyOrder(book, book2);
    }

    @Test
    void deleteByIdTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-delete"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-delete"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-delete")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        assertThat(bookRepositoryJpa.getAll()).containsExactly(book);

        bookRepositoryJpa.delete(book);
        assertThat(bookRepositoryJpa.getAll()).isEmpty();
    }

    @Test
    void findByNameAndAuthorTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-find-name-author"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-find-name-author"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-name-author")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());


        Book book2 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-name-author-2")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());

        Author author2 = authorRepositoryJpa.save(new Author("test-book-find-name-author-2"));
        Book book3 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-name-author")
                        .yearOfRelease(1999)
                        .author(author2)
                        .genre(genre)
                        .build());

        Optional<Book> findBook = bookRepositoryJpa.findByNameAndAuthor(
                "test-book-find-name-author", author);
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(book);

        Optional<Book> findBookNotExist = bookRepositoryJpa.findByNameAndAuthor(
                "b-test-book-find-name-author-0", author2);
        assertThat(findBookNotExist).isEmpty();
    }

    @Test
    void findBooksByAuthorTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-find-author"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-find-author"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-author")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());
        Book book2 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-author-2")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());

        Author author2 = authorRepositoryJpa.save(new Author("test-book-find-author-2"));

        Book book3 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-author")
                        .yearOfRelease(1999)
                        .author(author2)
                        .genre(genre)
                        .build());

        assertThat(bookRepositoryJpa.getAll()).containsExactly(book, book2, book3);

        List<Book> books = bookRepositoryJpa.findBooksByAuthor(author);
        assertThat(books).containsExactlyInAnyOrder(book, book2);

        Author author3 = authorRepositoryJpa.save(new Author("test-book-find-author-3"));
        List<Book> booksNotExists = bookRepositoryJpa.findBooksByAuthor(author3);
        assertThat(booksNotExists).isEmpty();
    }

    @Test
    void findBooksByReleaseYearAndGenreTest() {
        Author author = authorRepositoryJpa.save(new Author("test-book-find-year-genre"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-book-find-year-genre"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-year-genre")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());

        Book book2 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-year-genre-2")
                        .yearOfRelease(1991)
                        .author(author)
                        .genre(genre)
                        .build());

        Author author2 = authorRepositoryJpa.save(new Author("test-book-find-year-genre-2"));
        Book book3 = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-book-find-year-genre")
                        .yearOfRelease(1999)
                        .author(author2)
                        .genre(genre)
                        .build());

        List<Book> books = bookRepositoryJpa.findBooksByReleaseYearAndGenre(1991, genre);
        assertThat(books).containsExactlyInAnyOrder(book2);
        books = bookRepositoryJpa.findBooksByReleaseYearAndGenre(1999, genre);
        assertThat(books).containsExactlyInAnyOrder(book, book3);

        List<Book> booksNotExist = bookRepositoryJpa.findBooksByReleaseYearAndGenre(1220, genre);
        assertThat(booksNotExist).isEmpty();
    }
}
