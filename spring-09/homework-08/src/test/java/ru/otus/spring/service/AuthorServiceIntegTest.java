package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@Import(AuthorServiceImpl.class)
public class AuthorServiceIntegTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    @Test
    public void addTest() {
        Author added = authorService.add("test-add");
        assertThat(added.getId()).isNotBlank();

        authorRepository.delete(added);
    }

    @Test
    public void getOrAddTest() {
        Optional<Author> fa = authorRepository.findByName("test-get-or-add");
        assertThat(fa).isEmpty();
        Author added = authorService.getOrAdd("test-get-or-add");
        assertThat(added.getId()).isNotBlank();
        Author added2 = authorService.getOrAdd("test-get-or-add");
        assertThat(added2).isEqualTo(added);

        List<Author> allAdded = authorRepository.findAll(Example.of(new Author("test-get-or-add")));
        assertThat(allAdded).containsExactly(added);

        authorRepository.delete(added);
    }

    @Test
    public void saveTest() {
        Author saved = authorService.save(new Author("test-save"));
        assertThat(saved.getId()).isNotBlank();

        saved.setName("test-save-new");
        Author savedNew = authorService.save(saved);
        assertThat(saved).isEqualTo(savedNew);

        List<Author> allSaved = authorRepository.findAll(Example.of(new Author("test-save")));
        assertThat(allSaved).isEmpty();

        List<Author> allSavedNew = authorRepository.findAll(Example.of(new Author("test-save-new")));
        assertThat(allSavedNew).containsExactly(savedNew);

        authorRepository.delete(savedNew);
    }

    @Test
    public void deleteTest() {
        Author added = authorRepository.save(new Author("test-delete"));
        assertThat(added.getId()).isNotBlank();

        authorService.delete(added);

        Optional<Author> fa = authorRepository.findById(added.getId());
        assertThat(fa).isEmpty();
    }

    @Test
    public void findByNameTest() {
        Author added = authorRepository.save(new Author("test-find"));
        assertThat(added.getId()).isNotBlank();

        Author added2 = authorRepository.save(new Author("test-find-2"));
        assertThat(added2.getId()).isNotBlank();

        Optional<Author> fa = authorService.findByName("test-find");
        assertThat(fa).isPresent();
        assertThat(fa.get()).isEqualTo(added);

        authorRepository.deleteAll(List.of(added, added2));
    }

    @Test
    public void findAllTest() {
        Author added = authorRepository.save(new Author("test-find-all"));
        assertThat(added.getId()).isNotBlank();

        Author added2 = authorRepository.save(new Author("test-find-all-2"));
        assertThat(added2.getId()).isNotBlank();

        List<Author> authors = authorService.findAll();
        assertThat(authors).contains(added, added2);

        authorRepository.deleteAll(List.of(added, added2));
    }
}
