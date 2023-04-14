package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    public void addTest() {
        Genre genre = new Genre("1", "test-add");
        when(genreRepository.insert(any(Genre.class))).thenReturn(genre);

        assertThat(genreService.add("test-add")).isEqualTo(genre);
    }

    @Test
    public void getOrAddTest() {
        Genre genre = new Genre("1", "test-get-add");
        when(genreRepository.findByName("test-get-add")).thenReturn(Optional.empty());
        when(genreRepository.insert(any(Genre.class))).thenReturn(genre);
        assertThat(genreService.getOrAdd("test-get-add")).isEqualTo(genre);

        Genre genre2 = new Genre("2", "test-get-add-2");
        when(genreRepository.findByName("test-get-add-2")).thenReturn(Optional.of(genre2));
        assertThat(genreService.getOrAdd("test-get-add-2")).isEqualTo(genre2);
    }

    @Test
    public void saveTest() {
        Genre genre = new Genre("1", "test-save");
        when(genreRepository.save(genre)).thenReturn(genre);
        when(bookRepository.findByGenre(genre)).thenReturn(Collections.emptyList());

        assertThat(genreService.save(genre)).isEqualTo(genre);
    }

    @Test
    public void deleteTest() {
        Genre genre = new Genre("1", "test-delete");

        when(bookRepository.existsByGenre(genre)).thenReturn(false);
        doNothing().when(genreRepository).delete(any(Genre.class));

        genreService.delete(genre);

        Genre genre2 = new Genre("2", "test-delete-2");
        when(bookRepository.existsByGenre(genre2)).thenReturn(true);

        assertThatThrownBy(() -> genreService.delete(genre2)).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByNameTest() {
        Genre genre = new Genre("1", "test-find-name");
        when(genreRepository.findByName("test-find-name")).thenReturn(Optional.of(genre));
        assertThat(genreService.findByName("test-find-name")).isEqualTo(Optional.of(genre));

        when(genreRepository.findByName("test-not-find-name")).thenReturn(Optional.empty());
        assertThat(genreService.findByName("test-not-find-name")).isEmpty();
    }

    @Test
    public void findAllTest() {
        Genre genre = new Genre("1", "test-find-all");
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        assertThat(genreService.findAll()).containsExactly(genre);
    }
}
