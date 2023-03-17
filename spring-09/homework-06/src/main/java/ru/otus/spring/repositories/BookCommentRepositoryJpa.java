package ru.otus.spring.repositories;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.exception.LibraryDBException;

import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {
    @PersistenceContext
    private final EntityManager em;

    public BookCommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public long countByBook(Book book) {
        TypedQuery<Long> query = em.createQuery("select count(*) " +
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
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from BookComment b " +
                "where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<BookComment> getByBook(Book book) {
        TypedQuery<BookComment> query = em.createQuery("select b " +
                        "from BookComment b " +
                        "where b.book.id = :book_id",
                BookComment.class);
        query.setParameter("book_id", book.getId());
        return query.getResultList();
    }

    @Override
    public Optional<BookComment> findByTextAndBook(String comment, Book book) {
        TypedQuery<BookComment> query = em.createQuery("select b " +
                        "from BookComment b " +
                        "where b.book.id = :book_id " +
                        "and b.text = :text",
                BookComment.class);
        query.setParameter("book_id", book.getId());
        query.setParameter("text", comment);
        List<BookComment> comments = query.getResultList();
        if (comments.isEmpty()) {
            return Optional.empty();
        } else if (comments.size() == 1) {
            return Optional.of(comments.get(0));
        } else {
            throw new LibraryDBException("There are more than one book comment in library with text = "
                    + comment + " and book name = " + book.getName()
                    + " and book author = " + book.getAuthor().getName());
        }
    }
}
