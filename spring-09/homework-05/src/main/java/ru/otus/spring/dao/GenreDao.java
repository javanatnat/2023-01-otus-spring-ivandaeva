package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    long count();
    Genre insert(Genre genre);
    Optional<Genre> getById(long id);
    List<Genre> getAll();
    void deleteById(long id);
    Optional<Genre> findByName(String name);
}
