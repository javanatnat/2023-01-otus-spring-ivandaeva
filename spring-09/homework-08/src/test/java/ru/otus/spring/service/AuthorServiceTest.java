package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void addTest() {
        Author author = new Author("1", "test-add");
        when(authorRepository.insert(any(Author.class))).thenReturn(author);

        Author added = authorService.add("test-add");
        assertThat(added).isEqualTo(author);
    }

    @Test
    public void getOrAddTest() {
        Author author = new Author("1", "test-get-or-add");
        when(authorRepository.findByName("test-get-or-add")).thenReturn(Optional.of(author));

        assertThat(authorService.getOrAdd("test-get-or-add")).isEqualTo(author);

        Author author2 = new Author("2", "test-get-or-add-2");
        when(authorRepository.insert(any(Author.class))).thenReturn(author2);
        when(authorRepository.findByName("test-get-or-add-2")).thenReturn(Optional.empty());

        assertThat(authorService.getOrAdd("test-get-or-add-2")).isEqualTo(author2);
    }

    @Test
    public void saveTest() {
        Author author = new Author("1","test-save");
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(bookRepository.findByAuthor(any(Author.class))).thenReturn(Collections.emptyList());

        assertThat(authorService.save(author)).isEqualTo(author);
    }

    @Test
    public void deleteTest() {
        Author author = new Author("1", "test-delete");
        when(bookRepository.existsByAuthor(author)).thenReturn(false);
        doNothing().when(authorRepository).delete(any(Author.class));

        authorService.delete(author);

        Author author2 = new Author("2", "test-delete-2");
        when(bookRepository.existsByAuthor(author2)).thenReturn(true);

        assertThatThrownBy(() -> authorService.delete(author2)).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByNameTest() {
        Author author = new Author("1", "test-find-name");
        when(authorRepository.findByName("test-find-name")).thenReturn(Optional.of(author));

        assertThat(authorService.findByName("test-find-name")).isEqualTo(Optional.of(author));

        when(authorRepository.findByName("test-not-find-name")).thenReturn(Optional.empty());
        assertThat(authorService.findByName("test-not-find-name")).isEmpty();
    }

    @Test
    public void findAllTest() {
        Author author = new Author("1", "test-find-all");
        when(authorRepository.findAll()).thenReturn(List.of(author));
        assertThat(authorService.findAll()).containsExactly(author);
    }
}
