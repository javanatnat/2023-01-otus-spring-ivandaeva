package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author add(String name) {
        return authorRepository.insert(new Author(name));
    }

    @Override
    public Author getOrAdd(String name) {
        Optional<Author> findAuthor = findByName(name);
        return findAuthor.orElseGet(() -> add(name));
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
