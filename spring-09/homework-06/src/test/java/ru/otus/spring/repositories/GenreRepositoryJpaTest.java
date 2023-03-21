package ru.otus.spring.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
public class GenreRepositoryJpaTest {
    @Autowired
    GenreRepositoryJpa genreRepositoryJpa;

    @Test
    void initTest() {
        assertThat(genreRepositoryJpa.count()).isEqualTo(0);
        assertThat(genreRepositoryJpa.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Genre genre = new Genre("test-count");
        Genre insertedGenre = genreRepositoryJpa.save(genre);

        assertThat(genreRepositoryJpa.count()).isEqualTo(1);
    }

    @Test
    void getAllTest() {
        Genre genreOne = new Genre("test-get-all-1");
        Genre insertedGenreOne = genreRepositoryJpa.save(genreOne);
        assertThat(genreRepositoryJpa.getAll()).containsExactly(insertedGenreOne);

        Genre genreTwo = new Genre("test-get-all-2");
        Genre insertedGenreTwo = genreRepositoryJpa.save(genreTwo);
        assertThat(genreRepositoryJpa.getAll()).containsExactlyInAnyOrder(insertedGenreOne, insertedGenreTwo);
    }

    @Test
    void insertTest() {
        Genre genre = new Genre("test-add");
        Genre insertedGenre = genreRepositoryJpa.save(genre);
        assertThat(insertedGenre.getName()).isEqualTo("test-add");
        assertThat(insertedGenre.getId()).isNotZero();
        assertThat(genreRepositoryJpa.getAll()).containsExactly(insertedGenre);

        Genre insertedGenre2 = genreRepositoryJpa.save(insertedGenre);
        assertThat(insertedGenre2).isEqualTo(insertedGenre);
        assertThat(genreRepositoryJpa.getAll()).containsExactly(insertedGenre);
    }

    @Test
    void getByIdTest() {
        Genre genre = new Genre("test-get-by-id");
        Genre insertedGenre = genreRepositoryJpa.save(genre);
        assertThat(insertedGenre.getId()).isNotZero();

        Optional<Genre> genreFindById = genreRepositoryJpa.getById(insertedGenre.getId());
        assertThat(genreFindById).isPresent();
        assertThat(genreFindById.get()).isEqualTo(insertedGenre);

        assertThat(genreRepositoryJpa.getById(0L)).isEmpty();
    }

    @Test
    void findByNameTest() {
        Genre genre = new Genre("test-find-by-name");
        Genre insertedGenre = genreRepositoryJpa.save(genre);

        Optional<Genre> findGenreByName = genreRepositoryJpa.findByName("test-find-by-name");
        assertThat(findGenreByName).isPresent();
        assertThat(findGenreByName.get()).isEqualTo(insertedGenre);

        Optional<Genre> findGenreByNameUpper = genreRepositoryJpa.findByName("test-find-by-name".toUpperCase(Locale.ROOT));
        assertThat(findGenreByNameUpper).isEmpty();

        Optional<Genre> findGenreByNameNotExist = genreRepositoryJpa.findByName("test-find-by-");
        assertThat(findGenreByNameNotExist).isEmpty();
    }

    @Test
    void deleteByIdTest() {
        Genre genre = new Genre("test-delete-by-id");
        Genre insertedGenre = genreRepositoryJpa.save(genre);
        assertThat(genreRepositoryJpa.getAll()).containsExactly(insertedGenre);

        genreRepositoryJpa.delete(insertedGenre);
        assertThat(genreRepositoryJpa.getAll()).isEmpty();
    }
}
