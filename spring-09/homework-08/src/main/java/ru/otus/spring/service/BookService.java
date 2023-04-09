package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book add(Book book);
    Book update(Book book);
    void delete(Book book);
    Optional<Book> findByNameAndAuthorName(String name, String authorName);
    List<Book> findByAuthor(Author author);
    List<Book> findByYearOfReleaseAndGenre(int yearOfRelease, Genre genre);
    List<Book> findAll();
}
