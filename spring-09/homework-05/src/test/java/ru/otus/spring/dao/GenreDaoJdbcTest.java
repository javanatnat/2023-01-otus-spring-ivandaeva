package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryDBException;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Test
    void initTest() {
        assertThat(genreDaoJdbc.count()).isEqualTo(0);
        assertThat(genreDaoJdbc.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Genre genre = new Genre("test-count");
        Genre insertedGenre = genreDaoJdbc.insert(genre);

        assertThat(genreDaoJdbc.count()).isEqualTo(1);
    }

    @Test
    void getAllTest() {
        Genre genreOne = new Genre("test-get-all-1");
        Genre insertedGenreOne = genreDaoJdbc.insert(genreOne);
        assertThat(genreDaoJdbc.getAll()).containsExactly(insertedGenreOne);

        Genre genreTwo = new Genre("test-get-all-2");
        Genre insertedGenreTwo = genreDaoJdbc.insert(genreTwo);
        assertThat(genreDaoJdbc.getAll()).containsExactlyInAnyOrder(insertedGenreOne, insertedGenreTwo);
    }

    @Test
    void insertTest() {
        Genre genre = new Genre("test-add");
        Genre insertedGenre = genreDaoJdbc.insert(genre);
        assertThat(insertedGenre.getName()).isEqualTo("test-add");
        assertThat(insertedGenre.getId()).isNotZero();
        assertThat(genreDaoJdbc.getAll()).containsExactly(insertedGenre);

        Genre insertedGenre2 = genreDaoJdbc.insert(genre);
        assertThat(insertedGenre2).isEqualTo(insertedGenre);
        assertThat(genreDaoJdbc.getAll()).containsExactly(insertedGenre);
    }

    @Test
    void getByIdTest() {
        Genre genre = new Genre("test-get-by-id");
        Genre insertedGenre = genreDaoJdbc.insert(genre);
        assertThat(insertedGenre.getId()).isNotZero();

        Optional<Genre> genreFindById = genreDaoJdbc.getById(insertedGenre.getId());
        assertThat(genreFindById).isPresent();
        assertThat(genreFindById.get()).isEqualTo(insertedGenre);

        assertThat(genreDaoJdbc.getById(0L)).isEmpty();
    }

    @Test
    void findByNameTest() {
        Genre genre = new Genre("test-find-by-name");
        Genre insertedGenre = genreDaoJdbc.insert(genre);

        Optional<Genre> findGenreByName = genreDaoJdbc.findByName("test-find-by-name");
        assertThat(findGenreByName).isPresent();
        assertThat(findGenreByName.get()).isEqualTo(insertedGenre);

        Optional<Genre> findGenreByNameUpper = genreDaoJdbc.findByName("test-find-by-name".toUpperCase(Locale.ROOT));
        assertThat(findGenreByNameUpper).isEmpty();

        Optional<Genre> findGenreByNameNotExist = genreDaoJdbc.findByName("test-find-by-");
        assertThat(findGenreByNameNotExist).isEmpty();

        namedParameterJdbcOperations.update(
                "insert into genres (id, name) values (1000, 'test-find-by-name')",
                new EmptySqlParameterSource());
        assertThatThrownBy(() -> genreDaoJdbc.findByName("test-find-by-name"))
                .isExactlyInstanceOf(LibraryDBException.class)
                .hasMessage("There are more than one genre in library with name = " + "test-find-by-name");
    }

    @Test
    void deleteByIdTest() {
        Genre genre = new Genre("test-delete-by-id");
        Genre insertedGenre = genreDaoJdbc.insert(genre);
        assertThat(genreDaoJdbc.getAll()).containsExactly(insertedGenre);

        genreDaoJdbc.deleteById(insertedGenre.getId());
        assertThat(genreDaoJdbc.getAll()).isEmpty();
    }
}
