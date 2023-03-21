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
    void saveTest() {
        Author author = authorRepositoryJpa.save(new Author("test-save"));
        Genre genre = genreRepositoryJpa.save(new Genre("test-save"));
        Book book = bookRepositoryJpa.save(
                Book.builder()
                        .name("test-save")
                        .yearOfRelease(1999)
                        .author(author)
                        .genre(genre)
                        .build());

        BookComment bookComment = new BookComment("test-save", book);
        BookComment insertedBookComment = bookCommentRepositoryJpa.save(bookComment);
        Optional<BookComment> getBookComment = bookCommentRepositoryJpa.getById(insertedBookComment.getId());
        assertThat(getBookComment).isPresent();
        assertThat(getBookComment.get()).isEqualTo(insertedBookComment);
        assertThat(insertedBookComment.getBook()).isEqualTo(book);
    }
}
