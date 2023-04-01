package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(GenreServiceImpl.class)
public class GenreServiceTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @Test
    public void insertTest() {
        Genre genre = genreService.add("insertTest");
        assertThat(genre.getId()).isNotNull();

        Optional<Genre> findGenre = genreService.findByName("insertTest");
        assertThat(findGenre).isPresent();
        assertThat(findGenre.get()).isEqualTo(genre);
    }

    @Test
    public void deleteTest() {
        Genre genre = genreService.add("deleteTest");
        Long id = genre.getId();

        Optional<Genre> findGenre = genreRepository.findById(id);
        assertThat(findGenre).isPresent();
        assertThat(findGenre.get()).isEqualTo(genre);

        genreService.delete(genre);
        assertThat(genreRepository.existsById(id)).isFalse();
    }

    @Test
    public void deleteByNameTest() {
        Genre genre = genreService.add("deleteTest");
        Long id = genre.getId();

        Optional<Genre> findGenre = genreRepository.findById(id);
        assertThat(findGenre).isPresent();
        assertThat(findGenre.get()).isEqualTo(genre);

        genreService.delete(new Genre("deleteTest"));
        assertThat(genreRepository.existsById(id)).isFalse();
    }
}
