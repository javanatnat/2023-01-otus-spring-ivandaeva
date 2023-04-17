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
    public Book addWithRefs(Book book) {
        Book book4update = updateAuthorAndGenre(book);
        return add(book4update);
    }

    @Override
    public Book add(Book book) {
        return bookRepository.insert(book);
    }

    @Override
    public Book updateWithRefs(Book book) {
        Book book4update = updateAuthorAndGenre(book);
        return update(book4update);
    }

    @Override
    public Book update(Book book) {
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
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<Book> findByYearOfReleaseAndGenre(int yearOfRelease, Genre genre) {
        return bookRepository.findByYearOfReleaseAndGenreName(yearOfRelease, genre.getName());
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public boolean existsByAuthor(Author author) {
        return bookRepository.existsByAuthor(author);
    }

    @Override
    public boolean existsByGenre(Genre genre) {
        return bookRepository.existsByGenre(genre);
    }

    private Book updateAuthorAndGenre(Book book) {
        Author author = authorService.getOrAdd(book.getAuthorName());
        book.setAuthor(author);

        Genre genre = genreService.getOrAdd(book.getGenreName());
        book.setGenre(genre);

        return book;
    }
}
