package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(AuthorServiceImpl.class)
public class AuthorServiceTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Test
    public void insertTest() {
        Author author = authorService.add("insertTest");
        assertThat(author.getId()).isNotNull();

        Optional<Author> findAuthor = authorService.findByName("insertTest");
        assertThat(findAuthor).isPresent();
        assertThat(findAuthor.get()).isEqualTo(author);
    }

    @Test
    public void deleteTest() {
        Author author = authorService.add("deleteTest");
        Long id = author.getId();

        Optional<Author> findAuthor = authorRepository.findById(id);
        assertThat(findAuthor).isPresent();
        assertThat(findAuthor.get()).isEqualTo(author);

        authorService.delete(author);
        assertThat(authorRepository.existsById(id)).isFalse();
    }

    @Test
    public void deleteByNameTest() {
        Author author = authorService.add("deleteTest");
        Long id = author.getId();

        Optional<Author> findAuthor = authorRepository.findById(id);
        assertThat(findAuthor).isPresent();
        assertThat(findAuthor.get()).isEqualTo(author);

        authorService.delete(new Author("deleteTest"));
        assertThat(authorRepository.existsById(id)).isFalse();
    }
}
