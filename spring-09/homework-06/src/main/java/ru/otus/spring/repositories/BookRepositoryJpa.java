package ru.otus.spring.repositories;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryDBException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Book b", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        Map<String, Object> properties = Map.of("javax.persistence.fetchgraph", getBookEntityGraph());
        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", getBookEntityGraph());
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from Book b " +
                "where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Optional<Book> findByNameAndAuthor(String name, Author author) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b " +
                        "where b.name = :name " +
                        "and b.author.id = :author_id",
                Book.class);
        query.setParameter("name", name);
        query.setParameter("author_id", author.getId());
        query.setHint("javax.persistence.fetchgraph", getBookEntityGraph());
        List<Book> books = query.getResultList();
        if (books.isEmpty()) {
            return Optional.empty();
        } else if (books.size() == 1) {
            return Optional.of(books.get(0));
        } else {
            throw new LibraryDBException("There are more than one book in library with name = " + name
                    + " and author = " + author.getName());
        }
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b " +
                        "where b.author.id = :author_id",
                Book.class);
        query.setParameter("author_id", author.getId());
        query.setHint("javax.persistence.fetchgraph", getBookEntityGraph());
        return query.getResultList();
    }

    @Override
    public List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b " +
                        "where b.yearOfRelease = :release_year " +
                        "and b.genre.id = :genre_id",
                Book.class);
        query.setParameter("release_year", yearOfRelease);
        query.setParameter("genre_id", genre.getId());
        query.setHint("javax.persistence.fetchgraph", getBookEntityGraph());
        return query.getResultList();
    }

    private EntityGraph<?> getBookEntityGraph() {
        return em.getEntityGraph("book-entity-graph");
    }
}
