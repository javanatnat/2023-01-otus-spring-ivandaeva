package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findByNameAndAuthorName(String name, String authorName);

    boolean existsByNameAndAuthorName(String name, String authorName);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findByAuthorName(String authorName);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findByYearOfReleaseAndGenreName(int yearOfRelease, String genreName);
}
