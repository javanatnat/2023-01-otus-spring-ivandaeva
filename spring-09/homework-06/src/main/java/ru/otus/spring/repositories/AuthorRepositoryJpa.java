package ru.otus.spring.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.LibraryDBException;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Author a", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public void delete(Author author) {
        em.remove(author);
    }

    @Override
    public Optional<Author> findByName(String name) {
        TypedQuery<Author> query = em.createQuery("select a " +
                        "from Author a " +
                        "where a.name = :name",
                Author.class);
        query.setParameter("name", name);
        List<Author> authors = query.getResultList();
        if (authors.isEmpty()) {
            return Optional.empty();
        } else if (authors.size() == 1) {
            return Optional.of(authors.get(0));
        } else {
            throw new LibraryDBException("There are more than one author in library with name = " + name);
        }
    }
}
