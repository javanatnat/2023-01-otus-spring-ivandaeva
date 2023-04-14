package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByNameAndAuthorName(String name, String authorName);
    List<Book> findByYearOfReleaseAndGenreName(int yearOfRelease, String genreName);
    List<Book> findByGenre(Genre genre);
    List<Book> findByAuthor(Author author);
    List<Book> findByAuthorName(String authorName);
    boolean existsByAuthor(Author author);
    boolean existsByGenre(Genre genre);
    void deleteByNameAndAuthorName(String name, String authorName);
}
