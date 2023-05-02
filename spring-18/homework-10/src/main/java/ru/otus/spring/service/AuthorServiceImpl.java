package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

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
    public Author add(String name) {
        checkString(name);
        if (authorRepository.existsByName(name)) {
            throw new IllegalArgumentException("Author with name = " + name + " is already in library");
        }
        return save(new Author(name));
    }

    @Override
    public Author getOrAdd(Author author) {
        Objects.requireNonNull(author);
        checkString(author.getName());

        if (isNew(author)) {
            Optional<Author> findAuthor = authorRepository.findByName(author.getName());
            return findAuthor.orElseGet(() -> save(new Author(author.getName())));
        } else {
            return author;
        }
    }

    @Override
    public Author save(Author author) {
        Objects.requireNonNull(author);
        checkString(author.getName());
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Author author) {
        Objects.requireNonNull(author);
        if (isNew(author)) {
            checkString(author.getName());
            Optional<Author> findAuthor = authorRepository.findByName(author.getName());
            findAuthor.ifPresent(authorRepository::delete);
        } else {
            authorRepository.deleteById(author.getId());
        }
    }

    @Override
    public Optional<Author> findByName(String name) {
        checkString(name);
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public boolean isNew(Author author) {
        Objects.requireNonNull(author);
        return author.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
