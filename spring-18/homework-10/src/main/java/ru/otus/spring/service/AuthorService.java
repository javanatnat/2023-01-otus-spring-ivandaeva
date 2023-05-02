package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author add(String name);
    Author getOrAdd(Author author);
    Author save(Author author);
    void delete(Author author);
    Optional<Author> findByName(String name);
    List<Author> findAll();
    boolean isNew(Author author);
}
