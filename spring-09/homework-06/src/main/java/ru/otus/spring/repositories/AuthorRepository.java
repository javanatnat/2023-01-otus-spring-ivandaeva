package ru.otus.spring.repositories;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    long count();
    Author save(Author author);
    Optional<Author> getById(long id);
    List<Author> getAll();
    void delete(Author author);
    Optional<Author> findByName(String name);
}
