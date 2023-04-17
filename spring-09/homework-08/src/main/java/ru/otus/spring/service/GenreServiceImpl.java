package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Component
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreServiceImpl(
            GenreRepository genreRepository,
            BookRepository bookRepository
    ) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
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
        Genre updatedGenre = genreRepository.save(genre);
        if (genre.getId() != null) {
            List<Book> books = bookRepository.findByGenre(genre);
            books.forEach( book -> {
                book.setGenre(updatedGenre);
                bookRepository.save(book);
            });
        }
        return updatedGenre;
    }

    @Override
    public void delete(Genre genre) {
        if (genre.getId() != null) {
            if (bookRepository.existsByGenre(genre)) {
                throw new RuntimeException("Genre can't be deleted due to the presence of books with the genre = "
                        + genre.getName());
            }
            genreRepository.delete(genre);
        }
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
