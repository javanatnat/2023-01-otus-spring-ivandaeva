package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LibraryServiceImpl implements LibraryService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public LibraryServiceImpl(
            BookRepository bookRepository,
            AuthorService authorService,
            GenreService genreService
    ) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    @Transactional
    public Book add(Book book) {
        checkBookForUpdate(book);
        if (bookRepository.existsByNameAndAuthorName(book.getName(), book.getAuthor().getName())) {
            throw new IllegalArgumentException("Book with name = " + book.getName()
                    + " and auhtor.name = " + book.getAuthor().getName()
                    + " is already in library");
        }
        book.setAuthor(authorService.getOrAdd(book.getAuthor()));
        book.setGenre(genreService.getOrAdd(book.getGenre()));
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        checkBookForUpdate(book);
        Optional<Book> findBook;
        if (isNew(book)) {
            findBook = bookRepository.findByNameAndAuthorName(book.getName(), book.getAuthor().getName());
        } else {
            findBook = bookRepository.findById(book.getId());
        }

        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("Book with name = " + book.getName()
                    + " and auhtor.name = " + book.getAuthor().getName()
                    + " and id = " + book.getId()
                    + " has not found in library");
        }

        Book bookInDb = findBook.get();
        if (!book.getName().equals(bookInDb.getName()) ||
                !book.getAuthor().getName().equals(bookInDb.getAuthor().getName())) {
            throw new IllegalArgumentException("Book with name = " + book.getName()
                    + " and auhtor.name = " + book.getAuthor().getName()
                    + " and id = " + book.getId()
                    + " can't be update");
        }
        if (isNew(book)) {
            book.setId(bookInDb.getId());
        }
        if (genreService.isNew(book.getGenre())) {
            if (book.getGenre().getName().equals(bookInDb.getGenre().getName())) {
                book.setGenre(bookInDb.getGenre());
            } else {
                book.setGenre(genreService.getOrAdd(book.getGenre()));
            }
        }
        if (authorService.isNew(book.getAuthor())) {
            book.setAuthor(bookInDb.getAuthor());
        }
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        Objects.requireNonNull(book);
        if (isNew(book)) {
            Optional<Book> findBook = findByNameAndAuthor(book.getName(), book.getAuthor());
            findBook.ifPresent(bookRepository::delete);
        } else {
            bookRepository.deleteById(book.getId());
        }
    }

    @Override
    public Optional<Book> findByNameAndAuthorName(String name, String authorName) {
        checkString(name);
        checkString(authorName);
        return bookRepository.findByNameAndAuthorName(name, authorName);
    }

    @Override
    public Optional<Book> findByNameAndAuthor(String name, Author author) {
        checkString(name);
        Objects.requireNonNull(author);
        return findByNameAndAuthorName(name, author.getName());
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        Objects.requireNonNull(author);
        checkString(author.getName());
        return bookRepository.findByAuthorName(author.getName());
    }

    @Override
    public List<Book> findByYearOfReleaseAndGenre(int yearOfRelease, Genre genre) {
        Objects.requireNonNull(genre);
        checkString(genre.getName());
        return bookRepository.findByYearOfReleaseAndGenreName(yearOfRelease, genre.getName());
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    private void checkBookForUpdate(Book book) {
        Objects.requireNonNull(book);
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());
        checkString(book.getName());
        checkString(book.getAuthor().getName());
        checkString(book.getGenre().getName());
        if (book.getYearOfRelease() <= 0) {
            throw new IllegalArgumentException("Year of book release can't be less than zero value, " +
                    "current value = " + book.getYearOfRelease());
        }
    }

    private boolean isNew(Book book) {
        Objects.requireNonNull(book);
        return book.getId() == null;
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
