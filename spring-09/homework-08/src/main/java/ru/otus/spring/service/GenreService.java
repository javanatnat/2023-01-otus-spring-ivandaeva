package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre add(String name);
    Genre getOrAdd(String name);
    Genre save(Genre genre);
    void delete(Genre genre);
    Optional<Genre> findByName(String name);
    List<Genre> findAll();
}
