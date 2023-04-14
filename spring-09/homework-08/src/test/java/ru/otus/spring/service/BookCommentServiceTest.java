package ru.otus.spring.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookCommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookCommentServiceTest {
    @Mock
    private BookCommentRepository bookCommentRepository;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookCommentServiceImpl bookCommentService;

    @Test
    public void addTest() {
        Book book = Book.builder()
                .id("1")
                .name("test-add-comment")
                .yearOfRelease(2000)
                .author(new Author("test-add-comment"))
                .genre(new Genre("test-add-comment"))
                .build();
        BookComment bookComment = new BookComment("test-add-comment", book);

        when(bookService.findByNameAndAuthorName("test-add-comment", "test-add-comment"))
                .thenReturn(Optional.of(book));
        when(bookCommentRepository.insert(any(BookComment.class))).thenReturn(bookComment);

        assertThat(bookCommentService.add(
                "test-add-comment",
                "test-add-comment",
                "test-add-comment")).isEqualTo(bookComment);
        assertThat(bookCommentService.add("test-add-comment", book)).isEqualTo(bookComment);

        when(bookService.findByNameAndAuthorName("test-add-comment-2", "test-add-comment-2"))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookCommentService.add(
                "test-add-comment-2",
                "test-add-comment-2",
                "test-add-comment-2")).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void saveTest() {
        BookComment bookComment = new BookComment("test-save-comment", new Book());
        when(bookCommentRepository.save(any(BookComment.class))).thenReturn(bookComment);

        assertThat(bookCommentService.save(bookComment)).isEqualTo(bookComment);
    }

    @Test
    public void deleteTest() {
        doNothing().when(bookCommentRepository).delete(any(BookComment.class));
        bookCommentService.delete(new BookComment());
    }

    @Test
    public void findByBookAndAuthorTest() {
        Book book = Book.builder()
                .id("1")
                .name("test-find-comment")
                .yearOfRelease(2000)
                .author(new Author("test-find-comment"))
                .genre(new Genre("test-find-comment"))
                .build();
        BookComment bookComment = new BookComment("test-find-comment", book);

        when(bookService.findByNameAndAuthorName("test-find-comment", "test-find-comment"))
                .thenReturn(Optional.of(book));
        when(bookCommentRepository.findByBook(book)).thenReturn(List.of(bookComment));

        assertThat(bookCommentService.findByBookAndAuthor(
                "test-find-comment",
                "test-find-comment")).containsExactly(bookComment);

        when(bookService.findByNameAndAuthorName("test-find-comment-2", "test-find-comment-2"))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookCommentService.findByBookAndAuthor(
                "test-find-comment-2",
                "test-find-comment-2")).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
