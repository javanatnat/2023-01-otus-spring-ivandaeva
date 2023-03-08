package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LibraryServiceJdbcImpl implements LibraryService{
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    public LibraryServiceJdbcImpl(
            GenreDao genreDao,
            AuthorDao authorDao,
            BookDao bookDao
    ) {
        this.genreDao = genreDao;
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    @Override
    public Genre addGenre(String name) {
        checkString(name);
        return genreDao.insert(new Genre(name));
    }

    @Override
    public void deleteGenre(Genre genre) {
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
        genreDao.deleteById(genreForDelete.getId());
    }

    @Override
    public Optional<Genre> findGenreByName(String name) {
        return genreDao.findByName(name);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    @Override
    public Author addAuthor(String name) {
        checkString(name);
        return authorDao.insert(new Author(name));
    }

    @Override
    public void deleteAuthor(Author author) {
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
        authorDao.deleteById(authorForDelete.getId());
    }

    @Override
    public Optional<Author> findAuthorByName(String name) {
        return authorDao.findByName(name);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    @Override
    public Book addBook(Book book) {
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        if (!bookIsNew(book)) {
            throw new IllegalArgumentException("Book already in library with id = " + book.getId());
        }

        Author insertedAuthor = null;
        if (authorIsNew(book.getAuthor())) {
            insertedAuthor = addAuthor(book.getAuthor().getName());
        }
        Genre insertedGenre = null;
        if (genreIsNew(book.getGenre())) {
            insertedGenre = addGenre(book.getGenre().getName());
        }

        Book bookForInsert = book;
        if (!(insertedAuthor == null) || !(insertedGenre == null)) {
            bookForInsert = new Book(
                    book.getName(),
                    book.getDescription(),
                    book.getYearOfRelease(),
                    (insertedAuthor == null) ? book.getAuthor() : insertedAuthor,
                    (insertedGenre == null) ? book.getGenre() : insertedGenre);
        }
        return bookDao.insert(bookForInsert);
    }

    @Override
    public Book updateBook(Book book) {
        checkString(book.getName());
        Objects.requireNonNull(book.getAuthor());
        Objects.requireNonNull(book.getGenre());

        Book bookInDb = null;
        if (bookIsNew(book)) {
            Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookInDb = findBook.get();
        } else {
            Optional<Book> findBook = bookDao.getById(book.getId());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            } else {
                Book oldBook = findBook.get();
                if (!book.getName().equals(oldBook.getName()) ||
                        !book.getAuthor().getName().equals(oldBook.getAuthor().getName())
                ) {
                    throw new IllegalArgumentException("Can't change name or author name for book in library, name = "
                            + book.getName()
                            + ", author = " + book.getAuthor().getName());
                }
            }
        }

        Genre insertedGenre = null;
        if (genreIsNew(book.getGenre())) {
            insertedGenre = addGenre(book.getGenre().getName());
        }

        Book bookForUpdate = book;
        if (!(insertedGenre == null) || !(bookInDb == null)) {
            bookForUpdate = new Book(
                    (bookInDb == null) ? book.getId() : bookInDb.getId(),
                    book.getName(),
                    book.getDescription(),
                    book.getYearOfRelease(),
                    book.getAuthor(),
                    (insertedGenre == null) ? book.getGenre() : insertedGenre);
        }
        return bookDao.update(bookForUpdate);
    }

    @Override
    public void deleteBook(Book book) {
        Book bookForDelete = book;
        if (bookIsNew(book)) {
            Optional<Book> findBook = findBookByNameAndAuthor(book.getName(), book.getAuthor());
            if (findBook.isEmpty()) {
                throw new IllegalArgumentException("Book is not in library, name = " + book.getName()
                        + ", author = " + book.getAuthor().getName());
            }
            bookForDelete = findBook.get();
        }
        bookDao.deleteById(bookForDelete.getId());
    }

    @Override
    public Optional<Book> findBookByNameAndAuthor(String name, Author author) {
        checkString(name);
        checkString(author.getName());
        return bookDao.findByNameAndAuthor(name, author);
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        checkString(author.getName());
        return bookDao.findBooksByAuthor(author);
    }

    @Override
    public List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre) {
        checkString(genre.getName());
        return bookDao.findBooksByReleaseYearAndGenre(yearOfRelease, genre);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
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
}
