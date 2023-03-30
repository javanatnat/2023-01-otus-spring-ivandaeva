package ru.otus.spring.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {
    @Autowired
    AuthorRepositoryJpa authorRepositoryJpa;

    @Test
    void initTest() {
        assertThat(authorRepositoryJpa.count()).isEqualTo(0);
        assertThat(authorRepositoryJpa.getAll()).isEmpty();
    }

    @Test
    void countTest() {
        Author author = new Author("test-count");
        Author insertedAuthor = authorRepositoryJpa.save(author);

        assertThat(authorRepositoryJpa.count()).isEqualTo(1);
    }

    @Test
    void getAllTest() {
        Author authorOne = new Author("test-get-all-1");
        Author insertedAuthorOne = authorRepositoryJpa.save(authorOne);
        assertThat(authorRepositoryJpa.getAll()).containsExactly(insertedAuthorOne);

        Author authorTwo = new Author("test-get-all-2");
        Author insertedAuthorTwo = authorRepositoryJpa.save(authorTwo);
        assertThat(authorRepositoryJpa.getAll()).containsExactlyInAnyOrder(insertedAuthorOne, insertedAuthorTwo);
    }

    @Test
    void saveTest() {
        Author author = new Author("test-add");
        Author insertedAuthor = authorRepositoryJpa.save(author);
        assertThat(insertedAuthor.getName()).isEqualTo("test-add");
        assertThat(insertedAuthor.getId()).isNotZero();
        assertThat(authorRepositoryJpa.getAll()).containsExactly(insertedAuthor);

        Author insertedAuthor2 = authorRepositoryJpa.save(insertedAuthor);
        assertThat(insertedAuthor2).isEqualTo(insertedAuthor);
        assertThat(authorRepositoryJpa.getAll()).containsExactly(insertedAuthor);
    }

    @Test
    void getByIdTest() {
        Author author = new Author("test-get-by-id");
        Author insertedAuthor = authorRepositoryJpa.save(author);
        assertThat(insertedAuthor.getId()).isNotZero();

        Optional<Author> authorFindById = authorRepositoryJpa.getById(insertedAuthor.getId());
        assertThat(authorFindById).isPresent();
        assertThat(authorFindById.get()).isEqualTo(insertedAuthor);

        assertThat(authorRepositoryJpa.getById(0L)).isEmpty();
    }

    @Test
    void findByNameTest() {
        Author author = new Author("test-find-by-name");
        Author insertedAuthor = authorRepositoryJpa.save(author);

        Optional<Author> findAuthorByName = authorRepositoryJpa.findByName("test-find-by-name");
        assertThat(findAuthorByName).isPresent();
        assertThat(findAuthorByName.get()).isEqualTo(insertedAuthor);

        Optional<Author> findAuthorByNameUpper = authorRepositoryJpa.findByName(
                "test-find-by-name".toUpperCase(Locale.ROOT));
        assertThat(findAuthorByNameUpper).isEmpty();

        Optional<Author> findAuthorByNameNotExist = authorRepositoryJpa.findByName("test-find-by-");
        assertThat(findAuthorByNameNotExist).isEmpty();
    }

    @Test
    void deleteByIdTest() {
        Author author = new Author("test-delete-by-id");
        Author insertedAuthor = authorRepositoryJpa.save(author);
        assertThat(authorRepositoryJpa.getAll()).containsExactly(insertedAuthor);

        authorRepositoryJpa.delete(insertedAuthor);
        assertThat(authorRepositoryJpa.getAll()).isEmpty();
    }
}
