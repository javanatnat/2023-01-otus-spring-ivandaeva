package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    long count();
    Book insert(Book book);
    Book update(Book book);
    Optional<Book> getById(long id);
    List<Book> getAll();
    void deleteById(long id);
    Optional<Book> findByNameAndAuthor(String name, Author author);
    List<Book> findBooksByAuthor(Author author);
    List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre);
}
