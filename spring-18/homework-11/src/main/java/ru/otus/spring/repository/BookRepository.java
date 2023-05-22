package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByNameAndAuthorName(String name, String authorName);
    Flux<Book> findByYearOfReleaseAndGenreName(int yearOfRelease, String genreName);
    Flux<Book> findByAuthorName(String authorName);
    Mono<Boolean> existsByAuthorName(String authorName);
    Mono<Boolean> existsByGenreName(Genre genreName);
    Mono<Void> deleteByNameAndAuthorName(String name, String authorName);
}
