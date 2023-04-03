package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookCommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({
        BookCommentServiceImpl.class,
        LibraryServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class
})
public class BookCommentServiceTest {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private LibraryService libraryService;

    @Test
    public void insertTest() {
        Book book = libraryService.add(Book.builder()
                .name("insertTest")
                .author(new Author("insertTest"))
                .genre(new Genre("insertTest"))
                .yearOfRelease(1990)
                .build());

        BookComment bookComment1 = bookCommentService.add("comment 1", book);
        assertThat(bookComment1.getId()).isNotNull();

        BookComment bookComment2 = bookCommentService.add(
                "comment 2", "insertTest", "insertTest");
        assertThat(bookComment2.getId()).isNotNull();

        Optional<BookComment> findComment = bookCommentRepository.findById(bookComment1.getId());
        assertThat(findComment).isPresent();
        assertThat(findComment.get()).isEqualTo(bookComment1);

        findComment = bookCommentRepository.findById(bookComment2.getId());
        assertThat(findComment).isPresent();
        assertThat(findComment.get()).isEqualTo(bookComment2);
    }

    @Test
    public void deleteTest() {
        Book book = libraryService.add(Book.builder()
                .name("deleteTest")
                .author(new Author("deleteTest"))
                .genre(new Genre("deleteTest"))
                .yearOfRelease(1990)
                .build());

        BookComment bookComment1 = bookCommentService.add("comment 1", book);
        assertThat(bookComment1.getId()).isNotNull();
        BookComment bookComment2 = bookCommentService.add("comment 2", book);
        assertThat(bookComment2.getId()).isNotNull();

        bookComment2.setText("comment 2 update");
        bookComment2 = bookCommentService.save(bookComment2);

        List<BookComment> bookComments = bookCommentService.findByBookAndAuthor(
                "deleteTest", "deleteTest");
        assertThat(bookComments).containsExactlyInAnyOrder(bookComment1, bookComment2);

        bookCommentService.delete(bookComment1);
        bookComments = bookCommentService.findByBookAndAuthor(
                "deleteTest", "deleteTest");
        assertThat(bookComments).containsExactly(bookComment2);
    }
}
