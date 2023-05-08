package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.BookComment;


public interface BookCommentRepository extends ReactiveMongoRepository<BookComment, String> {
    Flux<BookComment> findByBookId(String bookId);
    Mono<BookComment> findByTextAndBookId(String bookId);
}
