package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author add(String name) {
        checkString(name);
        Optional<Author> findAuthor = findByName(name);
        return findAuthor.orElseGet(() -> authorRepository.save(new Author(name)));
    }

    @Override
    @Transactional
    public Author save(Author author) {
        Objects.requireNonNull(author);
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Author author) {
        Objects.requireNonNull(author);
        authorRepository.delete(author);
    }

    @Override
    public Optional<Author> findByName(String name) {
        checkString(name);
        return authorRepository.findByName(name);
    }

    @Override
    public Author findByNameStrict(String name) {
        Optional<Author> findAuthor = findByName(name);
        if (findAuthor.isEmpty()) {
            throw new IllegalArgumentException("Author is not in library, name = " + name);
        }
        return findAuthor.get();
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.getAll();
    }

    public boolean isNew(Author author) {
        return author.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
