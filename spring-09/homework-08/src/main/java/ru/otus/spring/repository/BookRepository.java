package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByNameAndAuthorName(String name, String authorName);
    List<Book> findByYearOfReleaseAndGenreName(int yearOfRelease, String genreName);
    List<Book> findByAuthorName(String authorName);
    void deleteByNameAndAuthorName(String name, String authorName);
}
