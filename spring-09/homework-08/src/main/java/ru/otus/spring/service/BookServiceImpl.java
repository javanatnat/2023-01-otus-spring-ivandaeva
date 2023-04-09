package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Component
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorService authorService,
            GenreService genreService
    ) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public Book add(Book book) {
        Author author = authorService.getOrAdd(book.getAuthorName());
        Genre genre = genreService.getOrAdd(book.getGenreName());
        return bookRepository.insert(book);
    }

    @Override
    public Book update(Book book) {
        Author author = authorService.getOrAdd(book.getAuthorName());
        Genre genre = genreService.getOrAdd(book.getGenreName());
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findByNameAndAuthorName(String name, String authorName) {
        return bookRepository.findByNameAndAuthorName(name, authorName);
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthorName(author.getName());
    }

    @Override
    public List<Book> findByYearOfReleaseAndGenre(int yearOfRelease, Genre genre) {
        return bookRepository.findByYearOfReleaseAndGenreName(yearOfRelease, genre.getName());
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
