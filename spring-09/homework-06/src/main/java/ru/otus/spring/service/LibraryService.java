package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface LibraryService {
    Genre addGenre(String name);
    void deleteGenre(Genre genre);
    Optional<Genre> findGenreByName(String name);
    List<Genre> getAllGenres();
    Author addAuthor(String name);
    void deleteAuthor(Author author);
    Optional<Author> findAuthorByName(String name);
    List<Author> getAllAuthors();
    Book addBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Book book);
    Optional<Book> findBookByNameAndAuthor(String name, Author author);
    List<Book> findBooksByAuthor(Author author);
    List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre);
    List<Book> getAllBooks();
    BookComment addBookComment(String comment, String bookName, String authorName);
    BookComment addBookComment(String text, Book book);
    void deleteBookComment(BookComment bookComment);
}
