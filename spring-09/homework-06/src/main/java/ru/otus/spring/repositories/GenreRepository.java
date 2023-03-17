package ru.otus.spring.repositories;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    long count();
    Genre save(Genre genre);
    Optional<Genre> getById(long id);
    List<Genre> getAll();
    void deleteById(long id);
    Optional<Genre> findByName(String name);
}
