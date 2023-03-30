package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl (GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public Genre add(String name) {
        checkString(name);
        Optional<Genre> findGenre = findByName(name);
        return findGenre.orElseGet(() -> genreRepository.save(new Genre(name)));
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        Objects.requireNonNull(genre);
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        Objects.requireNonNull(genre);
        genreRepository.delete(genre);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        checkString(name);
        return genreRepository.findByName(name);
    }

    @Override
    public Genre findByNameStrict(String name) {
        Optional<Genre> findGenre = findByName(name);
        if (findGenre.isEmpty()) {
            throw new IllegalArgumentException("Genre is not in library, name = " + name);
        }
        return findGenre.get();
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.getAll();
    }

    @Override
    public boolean isNew(Genre genre) {
        return genre.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
