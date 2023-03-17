package ru.otus.spring.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        BookCommentRepositoryJpa.class,
        BookRepositoryJpa.class,
        GenreRepositoryJpa.class,
        AuthorRepositoryJpa.class
})
public class BookCommentRepositoryJpaTest {
    @Autowired
    BookCommentRepositoryJpa bookCommentRepositoryJpa;
    @Autowired
    BookRepositoryJpa bookRepositoryJpa;
    @Autowired
    GenreRepositoryJpa genreRepositoryJpa;
    @Autowired
    AuthorRepositoryJpa authorRepositoryJpa;

    @Test
    void countByBookTest() {
        Author author = authorRepositoryJpa.save(new Author("test-count"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-count"));
        Book book = bookRepositoryJpa.save(
                new Book("test-count", null, 1999, author, genre));
        BookComment bookComment = new BookComment("test-count", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);

        assertThat(bookCommentRepositoryJpa.countByBook(book)).isEqualTo(1);
    }

    @Test
    void getByBookTest() {
        Author author = authorRepositoryJpa.save(new Author("test-get-by-book"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-get-by-book"));
        Book book = bookRepositoryJpa.save(
                new Book("test-get-by-book", null, 1999, author, genre));

        BookComment bookComment = new BookComment("test-get-by-book", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);
        assertThat(bookCommentRepositoryJpa.getByBook(book)).containsExactly(insertedBookComment);

        BookComment bookComment2 = new BookComment("test-get-by-book-2", book);
        BookComment insertedBookComment2 = bookCommentRepositoryJpa.save(bookComment2);
        assertThat(bookCommentRepositoryJpa.getByBook(book))
                .containsExactlyInAnyOrder(insertedBookComment, insertedBookComment2);
    }

    @Test
    void saveTest() {
        Author author = authorRepositoryJpa.save(new Author("test-save"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-save"));
        Book book = bookRepositoryJpa.save(
                new Book("test-save", null, 1999, author, genre));

        BookComment bookComment = new BookComment("test-save", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);
        assertThat(bookCommentRepositoryJpa.getByBook(book)).containsExactly(insertedBookComment);

        assertThat(insertedBookComment.getBook()).isEqualTo(book);
    }

    @Test
    void getByIdTest() {
        Author author = authorRepositoryJpa.save(new Author("test-get-by-id"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-get-by-id"));
        Book book = bookRepositoryJpa.save(
                new Book("test-get-by-id", null, 1999, author, genre));

        BookComment bookComment = new BookComment("test-get-by-id", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);
        assertThat(insertedBookComment.getId()).isNotZero();

        Optional<BookComment> findBookComment = bookCommentRepositoryJpa.getById(insertedBookComment.getId());
        assertThat(findBookComment).isPresent();
        assertThat(findBookComment.get()).isEqualTo(insertedBookComment);
        assertThat(findBookComment.get().getBook()).isEqualTo(book);

        assertThat(bookCommentRepositoryJpa.getById(0L)).isEmpty();
    }

    @Test
    void deleteByIdTest() {
        Author author = authorRepositoryJpa.save(new Author("test-delete-by-id"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-delete-by-id"));
        Book book = bookRepositoryJpa.save(
                new Book("test-delete-by-id", null, 1999, author, genre));

        BookComment bookComment = new BookComment("test-delete-by-id", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);
        assertThat(bookCommentRepositoryJpa.getByBook(book)).containsExactly(insertedBookComment);

        bookCommentRepositoryJpa.deleteById(insertedBookComment.getId());
        assertThat(bookCommentRepositoryJpa.getByBook(book)).isEmpty();
    }
}
