package ru.otus.spring.service;

import org.springframework.stereotype.Component;
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
    private final TransactionalService transactionalService;

    public LibraryServiceImpl(
            GenreRepository genreRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            BookCommentRepository bookCommentRepository,
            TransactionalService transactionalService
    ) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
        this.transactionalService = transactionalService;
    }

    @Override
    public Genre addGenre(String name) {
        checkString(name);
        Optional<Genre> findGenre = findGenreByName(name);
        if (findGenre.isPresent()) {
            return findGenre.get();
        }
        return transactionalService.doInTransaction(() -> genreRepository.save(new Genre(name)));
    }

    @Override
    public void deleteGenre(Genre genre) {
        Objects.requireNonNull(genre);
        Genre genreForDelete = genre;
        if (genreIsNew(genre)) {
            String name = genre.getName();
            checkString(name);
            Optional<Genre> findGenre = findGenreByName(name);
            if (findGenre.isEmpty()) {
                throw new IllegalArgumentException("Genre is not in library, name = " + name);
            }
            genreForDelete = findGenre.get();
        }
        long deleteId = genreForDelete.getId();
        transactionalService.doInTransaction(
                () -> {
                    genreRepository.deleteById(deleteId);
                    return null;
                });
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
    public Author addAuthor(String name) {
        checkString(name);
        Optional<Author> findAuthor = findAuthorByName(name);
        if (findAuthor.isPresent()) {
            return findAuthor.get();
        }
        return transactionalService.doInTransaction(() -> authorRepository.save(new Author(name)));
    }

    @Override
    public void deleteAuthor(Author author) {
        Objects.requireNonNull(author);

        Author authorForDelete = author;
        if (authorIsNew(author)) {
            String name = author.getName();
            checkString(name);
            Optional<Author> findAuthor = findAuthorByName(name);
            if (findAuthor.isEmpty()) {
                throw new IllegalArgumentException("Author is not in library, name = " + name);
            }
            authorForDelete = findAuthor.get();
        }
        long deleteId = authorForDelete.getId();

        transactionalService.doInTransaction(() -> {
            authorRepository.deleteById(deleteId);
            return null;
        });
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
    public Book addBook(Book book) {
        Objects.requireNonNull(book);
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        if (!bookIsNew(book)) {
            throw new IllegalArgumentException("Book already in library with id = " + book.getId());
        }

        return transactionalService.doInTransaction(() -> {
            if (authorIsNew(book.getAuthor())) {
                Author insertedAuthor = addAuthor(book.getAuthor().getName());
                book.setAuthor(insertedAuthor);
            }

            if (genreIsNew(book.getGenre())) {
                Genre insertedGenre = addGenre(book.getGenre().getName());
                book.setGenre(insertedGenre);
            }
            return bookRepository.save(book);
        });
    }

    @Override
    public Book updateBook(Book book) {
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        return transactionalService.doInTransaction(() -> {
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
        });
    }

    @Override
    public void deleteBook(Book book) {
        Objects.requireNonNull(book);

        Book bookForDelete = book;
        if (bookIsNew(book)) {
            Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookForDelete = findBook.get();
        }
        long deleteId = bookForDelete.getId();

        transactionalService.doInTransaction(() -> {
            bookRepository.deleteById(deleteId);
            return null;
        });
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
    public BookComment addBookComment(String comment, String bookName, String authorName) {
        return transactionalService.doInTransaction(() -> {
            Optional<Book> findBook = findBookByNameAndAuthor(bookName, new Author(authorName));
            return findBook.map(book -> addBookComment(comment, book)).orElse(null);
        });
    }

    @Override
    public BookComment addBookComment(String text, Book book) {
        checkString(text);
        Objects.requireNonNull(book);

        return transactionalService.doInTransaction(() -> bookCommentRepository.save(new BookComment(text, book)));
    }

    @Override
    public void deleteBookComment(String comment, String bookName, String authorName) {
        transactionalService.doInTransaction(() -> {
            Optional<Book> findBook = findBookByNameAndAuthor(bookName, new Author(authorName));
            findBook.ifPresent(book -> deleteBookComment(new BookComment(comment, book)));
            return null;
        });
    }

    @Override
    public void deleteBookComment(BookComment bookComment) {
        Objects.requireNonNull(bookComment);
        Objects.requireNonNull(bookComment.getBook());

        Book book = bookComment.getBook();

        BookComment bookCommentForDelete = bookComment;
        if (bookCommentIsNew(bookComment)) {
            Optional<BookComment> findBookComment = bookCommentRepository.findByTextAndBook(
                    bookComment.getText(), book);
            if (findBookComment.isEmpty()) {
                throw new IllegalArgumentException("Book comment is not exists, book name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookCommentForDelete = findBookComment.get();
        }
        long deleteId = bookCommentForDelete.getId();

        transactionalService.doInTransaction(() -> {
            bookCommentRepository.deleteById(deleteId);
            return null;
        });
    }

    @Override
    public List<BookComment> getBookComments(Book book) {
        Objects.requireNonNull(book);
        Book bookForQuery = book;
        if (bookIsNew(book)) {
            Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookForQuery = findBook.get();
        }
        return bookCommentRepository.getByBook(bookForQuery);
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
