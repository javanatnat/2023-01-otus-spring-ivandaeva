package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre add(String name) {
        checkString(name);
        if (genreRepository.existsByName(name)) {
            throw new IllegalArgumentException("Genre with name = " + name + " is already in library");
        }
        return save(new Genre(name));
    }

    @Override
    public Genre getOrAdd(Genre genre) {
        Objects.requireNonNull(genre);
        checkString(genre.getName());

        if (isNew(genre)) {
            Optional<Genre> findGenre = genreRepository.findByName(genre.getName());
            return findGenre.orElseGet(() -> save(new Genre(genre.getName())));
        } else {
            return genre;
        }
    }

    @Override
    public Genre save(Genre genre) {
        Objects.requireNonNull(genre);
        checkString(genre.getName());
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        Objects.requireNonNull(genre);
        if (isNew(genre)) {
            checkString(genre.getName());
            Optional<Genre> findGenre = genreRepository.findByName(genre.getName());
            findGenre.ifPresent(genreRepository::delete);
        } else {
            genreRepository.deleteById(genre.getId());
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        checkString(name);
        return genreRepository.findByName(name);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public boolean isNew(Genre genre) {
        Objects.requireNonNull(genre);
        return genre.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
