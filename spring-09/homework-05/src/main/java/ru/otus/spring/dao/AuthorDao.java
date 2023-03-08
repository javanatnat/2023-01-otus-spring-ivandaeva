package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    long count();
    Author insert(Author author);
    Optional<Author> getById(long id);
    List<Author> getAll();
    void deleteById(long id);
    Optional<Author> findByName(String name);
}
