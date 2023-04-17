package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@Import(GenreServiceImpl.class)
public class GenreServiceIntegTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreService genreService;

    @Test
    public void addTest() {
        Genre added = genreService.add("test-add");
        assertThat(added.getId()).isNotBlank();

        genreRepository.delete(added);
    }

    @Test
    public void getOrAddTest() {
        Optional<Genre> fa = genreRepository.findByName("test-get-or-add");
        assertThat(fa).isEmpty();
        Genre added = genreService.getOrAdd("test-get-or-add");
        assertThat(added.getId()).isNotBlank();
        Genre added2 = genreService.getOrAdd("test-get-or-add");
        assertThat(added2).isEqualTo(added);

        List<Genre> allAdded = genreRepository.findAll(Example.of(new Genre("test-get-or-add")));
        assertThat(allAdded).containsExactly(added);

        genreRepository.delete(added);
    }

    @Test
    public void saveTest() {
        Genre saved = genreService.save(new Genre("test-save"));
        assertThat(saved.getId()).isNotBlank();

        saved.setName("test-save-new");
        Genre savedNew = genreService.save(saved);
        assertThat(saved).isEqualTo(savedNew);

        List<Genre> allSaved = genreRepository.findAll(Example.of(new Genre("test-save")));
        assertThat(allSaved).isEmpty();

        List<Genre> allSavedNew = genreRepository.findAll(Example.of(new Genre("test-save-new")));
        assertThat(allSavedNew).containsExactly(savedNew);

        genreRepository.delete(savedNew);
    }

    @Test
    public void deleteTest() {
        Genre added = genreRepository.save(new Genre("test-delete"));
        assertThat(added.getId()).isNotBlank();

        genreService.delete(added);

        Optional<Genre> fa = genreRepository.findById(added.getId());
        assertThat(fa).isEmpty();
    }

    @Test
    public void findByNameTest() {
        Genre added = genreRepository.save(new Genre("test-find"));
        assertThat(added.getId()).isNotBlank();

        Genre added2 = genreRepository.save(new Genre("test-find-2"));
        assertThat(added2.getId()).isNotBlank();

        Optional<Genre> fa = genreService.findByName("test-find");
        assertThat(fa).isPresent();
        assertThat(fa.get()).isEqualTo(added);

        genreRepository.deleteAll(List.of(added, added2));
    }

    @Test
    public void findAllTest() {
        Genre added = genreRepository.save(new Genre("test-find-all"));
        assertThat(added.getId()).isNotBlank();

        Genre added2 = genreRepository.save(new Genre("test-find-all-2"));
        assertThat(added2.getId()).isNotBlank();

        List<Genre> authors = genreService.findAll();
        assertThat(authors).contains(added, added2);

        genreRepository.deleteAll(List.of(added, added2));
    }
}
