package ru.otus.spring.repositories;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import java.util.Optional;

@Component
public class BookCommentRepositoryJpa implements BookCommentRepository {
    @PersistenceContext
    private final EntityManager em;

    public BookCommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public long countByBook(Book book) {
        TypedQuery<Long> query = em.createQuery(
                "select count(*) " +
                "from BookComment b " +
                "where b.book.id = :book_id", Long.class);
        query.setParameter("book_id", book.getId());
        return query.getSingleResult();
    }

    @Override
    public BookComment save(BookComment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Optional<BookComment> getById(long id) {
        return Optional.ofNullable(em.find(BookComment.class, id));
    }

    @Override
    public void delete(BookComment bookComment) {
        em.remove(bookComment);
    }

    @Override
    public Optional<BookComment> findByTextAndBook(String comment, Book book) {
        return book.getComments().stream().filter(c -> c.getText().equals(comment)).findAny();
    }
}
