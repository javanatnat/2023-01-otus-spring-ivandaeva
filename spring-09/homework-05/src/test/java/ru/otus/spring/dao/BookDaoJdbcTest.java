package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
public class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @Test
    void initTest() {
        assertThat(bookDaoJdbc.count()).isEqualTo(0);
        assertThat(bookDaoJdbc.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Book book = new Book(
                "b-test-book-count",
                "",
                1991,
                new Author(1L, "b-test-book-count"),
                new Genre(1L, "b-test-book-count"));
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.count()).isEqualTo(1);
    }

    @Test
    void insertTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-insert"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-insert"));
        Book book = new Book(
                "b-test-book-insert",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(insertedBook.getName()).isEqualTo(book.getName());
        assertThat(insertedBook.getId()).isNotZero();
        assertThat(insertedBook.getDescription()).isEqualTo("");
        assertThat(insertedBook.getYearOfRelease()).isEqualTo(1991);
        assertThat(insertedBook.getAuthor()).isEqualTo(insertedAuthor);
        assertThat(insertedBook.getGenre()).isEqualTo(insertedGenre);
        assertThat(bookDaoJdbc.getAll()).containsExactly(insertedBook);

        Book insertedBook2 = bookDaoJdbc.insert(book);
        assertThat(insertedBook2).isEqualTo(insertedBook);
        assertThat(bookDaoJdbc.getAll()).containsExactly(insertedBook);
    }

    @Test
    void updateTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-update"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-update"));
        Book book = new Book(
                "b-test-book-update",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.getAll()).containsExactly(insertedBook);

        Book bookForUpdate = new Book(
                insertedBook.getId(),
                "b-test-book-update",
                "test",
                1993,
                insertedAuthor,
                insertedGenre);
        Book updatedBoook = bookDaoJdbc.update(bookForUpdate);
        assertThat(updatedBoook.getYearOfRelease()).isEqualTo(1993);
        assertThat(updatedBoook.getId()).isEqualTo(insertedBook.getId());
        assertThat(updatedBoook.getName()).isEqualTo(insertedBook.getName());
        assertThat(updatedBoook.getDescription()).isEqualTo("test");
        assertThat(updatedBoook.getAuthor()).isEqualTo(insertedBook.getAuthor());
        assertThat(updatedBoook.getGenre()).isEqualTo(insertedBook.getGenre());
        assertThat(bookDaoJdbc.getAll()).containsExactly(updatedBoook);

        Genre insertedGenre2 = genreDaoJdbc.insert(new Genre("b-test-book-update-2"));
        Book bookForUpdate2 = new Book(
                insertedBook.getId(),
                "b-test-book-update",
                "test",
                1993,
                insertedAuthor,
                insertedGenre2);
        Book updatedBoook2 = bookDaoJdbc.update(bookForUpdate2);
        assertThat(updatedBoook2.getYearOfRelease()).isEqualTo(1993);
        assertThat(updatedBoook2.getId()).isEqualTo(insertedBook.getId());
        assertThat(updatedBoook2.getName()).isEqualTo(insertedBook.getName());
        assertThat(updatedBoook2.getDescription()).isEqualTo("test");
        assertThat(updatedBoook2.getAuthor()).isEqualTo(insertedBook.getAuthor());
        assertThat(updatedBoook2.getGenre()).isEqualTo(insertedGenre2);
        assertThat(bookDaoJdbc.getAll()).containsExactly(updatedBoook2);
    }

    @Test
    void getByIdTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-get-by-id"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-get-by-id"));
        Book book = new Book(
                "b-test-book-get-by-id",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(insertedBook.getId()).isNotZero();

        Optional<Book> findBook = bookDaoJdbc.getById(insertedBook.getId());
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(insertedBook);

        assertThat(bookDaoJdbc.getById(0L)).isEmpty();
    }

    @Test
    void getAllTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-get-all"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-get-all"));
        Book book = new Book(
                "b-test-book-get-all",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.getAll()).containsExactly(insertedBook);

        Book book2 = new Book(
                "b-test-book-get-all-2",
                "",
                1992,
                insertedAuthor,
                insertedGenre);
        Book insertedBook2 = bookDaoJdbc.insert(book2);
        assertThat(bookDaoJdbc.getAll()).containsExactlyInAnyOrder(insertedBook, insertedBook2);
    }

    @Test
    void deleteByIdTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-delete"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-delete"));
        Book book = new Book(
                "b-test-book-delete",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.getAll()).containsExactly(insertedBook);

        bookDaoJdbc.deleteById(insertedBook.getId());
        assertThat(bookDaoJdbc.getAll()).isEmpty();
    }

    @Test
    void findByNameAndAuthorTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-find-name-author"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-find-name-author"));
        Book book = new Book(
                "b-test-book-find-name-author",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);

        Book book2 = new Book(
                "b-test-book-find-name-author-2",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook2 = bookDaoJdbc.insert(book2);

        Author insertedAuthor2 = authorDaoJdbc.insert(new Author("b-test-book-find-name-author-2"));
        Book book3 = new Book(
                "b-test-book-find-name-author",
                "",
                1991,
                insertedAuthor2,
                insertedGenre);
        Book insertedBook3 = bookDaoJdbc.insert(book3);

        Optional<Book> findBook = bookDaoJdbc.findByNameAndAuthor(
                "b-test-book-find-name-author", new Author("b-test-book-find-name-author"));
        assertThat(findBook).isPresent();
        assertThat(findBook.get()).isEqualTo(insertedBook);

        Optional<Book> findBookNotExist = bookDaoJdbc.findByNameAndAuthor(
                "b-test-book-find-name-author-0", new Author("b-test-book-find-name-author"));
        assertThat(findBookNotExist).isEmpty();
    }

    @Test
    void findBooksByAuthorTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author( "b-test-book-find-author"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-find-author"));
        Book book = new Book(
                "b-test-book-find-author",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);

        Book book2 = new Book(
                "b-test-book-find-author-2",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook2 = bookDaoJdbc.insert(book2);

        Author insertedAuthor2 = authorDaoJdbc.insert(new Author("b-test-book-find-authorr-2"));
        Book book3 = new Book(
                "b-test-book-find-author",
                "",
                1991,
                insertedAuthor2,
                insertedGenre);
        Book insertedBook3 = bookDaoJdbc.insert(book3);

        List<Book> books = bookDaoJdbc.findBooksByAuthor(insertedAuthor);
        assertThat(books).containsExactlyInAnyOrder(insertedBook, insertedBook2);

        List<Book> booksNotExists = bookDaoJdbc.findBooksByAuthor(new Author("b-test-book-find-author-0"));
        assertThat(booksNotExists).isEmpty();
    }

    @Test
    void findBooksByReleaseYearAndGenreTest() {
        Author insertedAuthor = authorDaoJdbc.insert(new Author("b-test-book-find-year-genre"));
        Genre insertedGenre = genreDaoJdbc.insert(new Genre("b-test-book-find-year-genre"));
        Book book = new Book(
                "b-test-book-find-year-genre",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook = bookDaoJdbc.insert(book);

        Book book2 = new Book(
                "b-test-book-find-year-genre-2",
                "",
                1991,
                insertedAuthor,
                insertedGenre);
        Book insertedBook2 = bookDaoJdbc.insert(book2);

        Author insertedAuthor2 = authorDaoJdbc.insert(new Author("b-test-book-find-year-genre-2"));
        Book book3 = new Book(
                "b-test-book-find-year-genre",
                "",
                1992,
                insertedAuthor2,
                insertedGenre);
        Book insertedBook3 = bookDaoJdbc.insert(book3);

        List<Book> books = bookDaoJdbc.findBooksByReleaseYearAndGenre(1991, insertedGenre);
        assertThat(books).containsExactlyInAnyOrder(insertedBook, insertedBook2);

        List<Book> booksNotExist = bookDaoJdbc.findBooksByReleaseYearAndGenre(1220, insertedGenre);
        assertThat(booksNotExist).isEmpty();
    }
}
