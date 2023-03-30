package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.BookRepository;

import java.util.*;

@Component
public class LibraryServiceImpl implements LibraryService {
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;

    public LibraryServiceImpl(
            GenreService genreService,
            AuthorService authorService,
            BookCommentService bookCommentService,
            BookRepository bookRepository
    ) {
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookCommentService = bookCommentService;

        this.bookRepository = bookRepository;
    }

    @Override
    public Genre addGenre(String name) {
        return genreService.add(name);
    }

    @Override
    public void deleteGenre(Genre genre) {
        genreService.delete(genre);
    }

    @Override
    public Optional<Genre> findGenreByName(String name) {
        return genreService.findByName(name);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreService.findAll();
    }

    @Override
    public Author addAuthor(String name) {
        return authorService.add(name);
    }

    @Override
    public void deleteAuthor(Author author) {
        authorService.delete(author);
    }

    @Override
    public Optional<Author> findAuthorByName(String name) {
        return authorService.findByName(name);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        checkBookForUpdate(book);

        if (!bookIsNew(book)) {
            throw new IllegalArgumentException("Book already in library with id = " + book.getId());
        }

        if (authorService.isNew(book.getAuthor())) {
            Author insertedAuthor = addAuthor(book.getAuthor().getName());
            book.setAuthor(insertedAuthor);
        }

        if (genreService.isNew(book.getGenre())) {
            Genre insertedGenre = addGenre(book.getGenre().getName());
            book.setGenre(insertedGenre);
        }
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        checkBookForUpdate(book);

        Book bookInDb;
        if (bookIsNew(book)) {
            bookInDb = findBookByNameAndAuthorStrict(book);
        } else {
            bookInDb = findBookByIdStrict(book);
            if (!book.getName().equals(bookInDb.getName()) ||
                    !book.getAuthor().getName().equals(bookInDb.getAuthor().getName())
            ) {
                throw new IllegalArgumentException("Can't change name or author name for book in library, name = "
                        + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
        }

        book.setId(bookInDb.getId());
        book.setAuthor(bookInDb.getAuthor());

        if (genreService.isNew(book.getGenre())) {
            if (bookInDb.getGenre().getName().equals(book.getGenre().getName())) {
                book.setGenre(bookInDb.getGenre());
            } else {
                Genre insertedGenre = addGenre(book.getGenre().getName());
                book.setGenre(insertedGenre);
            }
        }

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Book book) {
        Objects.requireNonNull(book);
        bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findBookByNameAndAuthor(String name, Author author) {
        Objects.requireNonNull(author);
        checkString(name);
        checkString(author.getName());

        Author findByAuthor = author;
        if (authorService.isNew(author)) {
            findByAuthor = authorService.findByNameStrict(author.getName());
        }
        return bookRepository.findByNameAndAuthor(name, findByAuthor);
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        Objects.requireNonNull(author);
        checkString(author.getName());

        Author findByAuthor = author;
        if (authorService.isNew(author)) {
            findByAuthor = authorService.findByNameStrict(author.getName());
        }
        return bookRepository.findBooksByAuthor(findByAuthor);
    }

    @Override
    public List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre) {
        Objects.requireNonNull(genre);
        checkString(genre.getName());

        Genre findByGenre = genre;
        if (genreService.isNew(genre)) {
            findByGenre = genreService.findByNameStrict(genre.getName());
        }
        return bookRepository.findBooksByReleaseYearAndGenre(yearOfRelease, findByGenre);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Override
    @Transactional
    public BookComment addBookComment(String comment, String bookName, String authorName) {
        Optional<Book> findBook = findBookByNameAndAuthor(bookName, new Author(authorName));
        return findBook.map(book -> addBookComment(comment, book)).orElse(null);
    }

    @Override
    public BookComment addBookComment(String text, Book book) {
        return bookCommentService.add(text, book);
    }

    @Override
    public void deleteBookComment(BookComment bookComment) {
        bookCommentService.delete(bookComment);
    }

    @Override
    @Transactional
    public List<BookComment> getBookComments(String bookName, String authorName) {
        Optional<Book> findBook = findBookByNameAndAuthor(bookName, new Author(authorName));
        return findBook
                .map(book -> List.copyOf(book.getComments()))
                .orElse(Collections.emptyList());
    }

    private void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkBookForUpdate(Book book) {
        Objects.requireNonNull(book);
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());
        checkString(book.getName());
        checkString(book.getAuthor().getName());
        checkString(book.getGenre().getName());
    }

    private boolean bookIsNew(Book book) {
        return book.getId() == null;
    }

    private Book findBookByNameAndAuthorStrict(Book book) {
        Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                    + ", author = " + book.getAuthor().getName());
        }
        return findBook.get();
    }

    private Book findBookByIdStrict(Book book) {
        Optional<Book> findBook = bookRepository.getById(book.getId());
        if (findBook.isEmpty()) {
            throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                    + ", author = " + book.getAuthor().getName());
        }
        return findBook.get();
    }
}
