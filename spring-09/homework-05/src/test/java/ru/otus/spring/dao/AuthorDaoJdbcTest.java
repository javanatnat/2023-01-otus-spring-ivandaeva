package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.LibraryDBException;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Test
    void initTest() {
        assertThat(authorDaoJdbc.count()).isEqualTo(0);
        assertThat(authorDaoJdbc.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Author author = new Author("test-count");
        Author insertedAuthor = authorDaoJdbc.insert(author);

        assertThat(authorDaoJdbc.count()).isEqualTo(1);
    }

    @Test
    void getAllTest() {
        Author authorOne = new Author("test-get-all-1");
        Author insertedAuthorOne = authorDaoJdbc.insert(authorOne);
        assertThat(authorDaoJdbc.getAll()).containsExactly(insertedAuthorOne);

        Author authorTwo = new Author("test-get-all-2");
        Author insertedAuthorTwo = authorDaoJdbc.insert(authorTwo);
        assertThat(authorDaoJdbc.getAll()).containsExactlyInAnyOrder(insertedAuthorOne, insertedAuthorTwo);
    }

    @Test
    void insertTest() {
        Author author = new Author("test-add");
        Author insertedAuthor = authorDaoJdbc.insert(author);
        assertThat(insertedAuthor.getName()).isEqualTo("test-add");
        assertThat(insertedAuthor.getId()).isNotZero();
        assertThat(authorDaoJdbc.getAll()).containsExactly(insertedAuthor);

        Author insertedAuthor2 = authorDaoJdbc.insert(author);
        assertThat(insertedAuthor2).isEqualTo(insertedAuthor);
        assertThat(authorDaoJdbc.getAll()).containsExactly(insertedAuthor);
    }

    @Test
    void getByIdTest() {
        Author author = new Author("test-get-by-id");
        Author insertedAuthor = authorDaoJdbc.insert(author);
        assertThat(insertedAuthor.getId()).isNotZero();

        Optional<Author> authorFindById = authorDaoJdbc.getById(insertedAuthor.getId());
        assertThat(authorFindById).isPresent();
        assertThat(authorFindById.get()).isEqualTo(insertedAuthor);

        assertThat(authorDaoJdbc.getById(0L)).isEmpty();
    }

    @Test
    void findByNameTest() {
        Author author = new Author("test-find-by-name");
        Author insertedAuthor = authorDaoJdbc.insert(author);

        Optional<Author> findAuthorByName = authorDaoJdbc.findByName("test-find-by-name");
        assertThat(findAuthorByName).isPresent();
        assertThat(findAuthorByName.get()).isEqualTo(insertedAuthor);

        Optional<Author> findAuthorByNameUpper = authorDaoJdbc.findByName("test-find-by-name".toUpperCase(Locale.ROOT));
        assertThat(findAuthorByNameUpper).isEmpty();

        Optional<Author> findAuthorByNameNotExist = authorDaoJdbc.findByName("test-find-by-");
        assertThat(findAuthorByNameNotExist).isEmpty();

        namedParameterJdbcOperations.update(
                "insert into authors (id, name) values (1000, 'test-find-by-name')",
                new EmptySqlParameterSource());
        assertThatThrownBy(() -> authorDaoJdbc.findByName("test-find-by-name"))
                .isExactlyInstanceOf(LibraryDBException.class)
                .hasMessage("There are more than one author in library with name = " + "test-find-by-name");
    }

    @Test
    void deleteByIdTest() {
        Author author = new Author("test-delete-by-id");
        Author insertedAuthor = authorDaoJdbc.insert(author);
        assertThat(authorDaoJdbc.getAll()).containsExactly(insertedAuthor);

        authorDaoJdbc.deleteById(insertedAuthor.getId());
        assertThat(authorDaoJdbc.getAll()).isEmpty();
    }
}
