package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
    List<BookComment> findByBook(Book book);
    Optional<BookComment> findByTextAndBook(Book book);
}
