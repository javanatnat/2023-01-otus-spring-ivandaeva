package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre add(String name) {
        return genreRepository.insert(new Genre(name));
    }

    @Override
    public Genre getOrAdd(String name) {
        Optional<Genre> findGenre = findByName(name);
        return findGenre.orElseGet(() -> add(name));
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
