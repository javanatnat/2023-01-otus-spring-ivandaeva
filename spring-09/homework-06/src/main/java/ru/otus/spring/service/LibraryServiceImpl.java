package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookCommentRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LibraryServiceImpl implements LibraryService {
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;

    public LibraryServiceImpl(
            GenreRepository genreRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            BookCommentRepository bookCommentRepository
    ) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    @Transactional
    public Genre addGenre(String name) {
        checkString(name);
        Optional<Genre> findGenre = findGenreByName(name);
        return findGenre.orElseGet(() -> genreRepository.save(new Genre(name)));
    }

    @Override
    @Transactional
    public void deleteGenre(Genre genre) {
        Objects.requireNonNull(genre);
        genreRepository.delete(genre);
    }

    @Override
    public Optional<Genre> findGenreByName(String name) {
        checkString(name);
        return genreRepository.findByName(name);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.getAll();
    }

    @Override
    @Transactional
    public Author addAuthor(String name) {
        checkString(name);
        Optional<Author> findAuthor = findAuthorByName(name);
        return findAuthor.orElseGet(() -> authorRepository.save(new Author(name)));
    }

    @Override
    @Transactional
    public void deleteAuthor(Author author) {
        Objects.requireNonNull(author);
        authorRepository.delete(author);
    }

    @Override
    public Optional<Author> findAuthorByName(String name) {
        checkString(name);
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.getAll();
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        Objects.requireNonNull(book);
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        if (!bookIsNew(book)) {
            throw new IllegalArgumentException("Book already in library with id = " + book.getId());
        }

        if (authorIsNew(book.getAuthor())) {
            Author insertedAuthor = addAuthor(book.getAuthor().getName());
            book.setAuthor(insertedAuthor);
        }

        if (genreIsNew(book.getGenre())) {
            Genre insertedGenre = addGenre(book.getGenre().getName());
            book.setGenre(insertedGenre);
        }
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        Book bookInDb;
        if (bookIsNew(book)) {
            Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookInDb = findBook.get();
        } else {
            Optional<Book> findBook = bookRepository.getById(book.getId());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            } else {
                bookInDb = findBook.get();
                if (!book.getName().equals(bookInDb.getName()) ||
                        !book.getAuthor().getName().equals(bookInDb.getAuthor().getName())
                ) {
                    throw new IllegalArgumentException("Can't change name or author name for book in library, name = "
                            + book.getName()
                            + ", author = " + book.getAuthor().getName());
                }

            }
        }

        book.setId(bookInDb.getId());

        if (bookInDb.getGenre() != null &&
                bookInDb.getGenre().getName().equals(book.getGenre().getName())
        ) {
            book.setGenre(bookInDb.getGenre());
        }

        if (bookInDb.getAuthor() != null &&
                bookInDb.getAuthor().getName().equals(book.getAuthor().getName())
        ) {
            book.setAuthor(bookInDb.getAuthor());
        }

        if (genreIsNew(book.getGenre())) {
            Genre insertedGenre = addGenre(book.getGenre().getName());
            book.setGenre(insertedGenre);
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

        String authorName = author.getName();

        checkString(name);
        checkString(authorName);

        Author findByAuthor = author;
        if (authorIsNew(author)) {
            Optional<Author> findAuthor = findAuthorByName(authorName);
            if (findAuthor.isEmpty()) {
                throw new IllegalArgumentException("Author is not in library, name = " + authorName);
            }
            findByAuthor = findAuthor.get();
        }
        return bookRepository.findByNameAndAuthor(name, findByAuthor);
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        Objects.requireNonNull(author);
        checkString(author.getName());

        String authorName = author.getName();
        Author findByAuthor = author;

        if (authorIsNew(author)) {
            Optional<Author> findAuthor = findAuthorByName(authorName);
            if (findAuthor.isEmpty()) {
                throw new IllegalArgumentException("Author is not in library, name = " + authorName);
            }
            findByAuthor = findAuthor.get();
        }
        return bookRepository.findBooksByAuthor(findByAuthor);
    }

    @Override
    public List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre) {
        Objects.requireNonNull(genre);

        String genreName = genre.getName();

        checkString(genreName);

        Genre findByGenre = genre;
        if (genreIsNew(genre)) {
            Optional<Genre> findGenre = findGenreByName(genreName);
            if (findGenre.isEmpty()) {
                throw new IllegalArgumentException("Genre is not in library, name = " + genreName);
            }
            findByGenre = findGenre.get();
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
    @Transactional
    public BookComment addBookComment(String text, Book book) {
        checkString(text);
        Objects.requireNonNull(book);
        return bookCommentRepository.save(new BookComment(text, book));
    }

    @Override
    @Transactional
    public void deleteBookComment(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        Objects.requireNonNull(bookComment.getBook());
        bookCommentRepository.delete(bookComment);
    }

    private static void checkString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    private static boolean bookIsNew(Book book) {
        return book.getId() == null;
    }

    private static boolean authorIsNew(Author author) {
        return author.getId() == null;
    }

    private static boolean genreIsNew(Genre genre) {
        return genre.getId() == null;
    }

    private static boolean bookCommentIsNew(BookComment bookComment) {
        return bookComment.getId() == null;
    }
}
