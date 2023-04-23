package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    Optional<BookComment> findByTextAndBookNameAndBook_AuthorName(String text, String bookName, String authorName);
    List<BookComment> findByBookNameAndBook_AuthorName(String bookName, String authorName);
}
