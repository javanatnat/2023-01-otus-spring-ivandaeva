package ru.otus.spring.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookComment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookCommentService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.service.LibraryService;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class ShellCommands {
    private static final String COMMA = ", ";
    private final LibraryService libraryService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;

    @Autowired
    public ShellCommands(
            LibraryService libraryService,
            AuthorService authorService,
            GenreService genreService,
            BookCommentService bookCommentService
    ) {
        this.libraryService = libraryService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod(value = "Add genre", key = {"add-genre", "ag"})
    public void addGenre(String genreName) {
        genreService.add(genreName);
    }

    @ShellMethod(value = "Find genre by name", key = {"find-genre", "fg"})
    public String findGenre(String genreName) {
        Optional<Genre> findGenre = genreService.findByName(genreName);
        return findGenre.map(genre -> "Find genre in library: " + genre.getName())
                .orElseGet(() -> "Can't find in library genre with name = " + genreName);
    }

    @ShellMethod(value = "Delete genre by name", key = {"delete-genre", "dg"})
    public void deleteGenre(String genreName) {
        genreService.delete(new Genre(genreName));
    }

    @ShellMethod(value = "Get all genres", key = {"get-genres", "allg"})
    public String getAllGenres() {
        List<Genre> genres = genreService.findAll();
        return String.join(COMMA, genres.stream().map(Genre::getName).toList());
    }

    @ShellMethod(value = "Add author", key = {"add-author", "aa"})
    public void addAuthor(String authorName) {
        authorService.add(authorName);
    }

    @ShellMethod(value = "Find author by name", key = {"find-author", "fa"})
    public String findAuthor(String authorName) {
        Optional<Author> findAuthor = authorService.findByName(authorName);
        return findAuthor.map(author -> "Find author in library: " + author.getName())
                .orElseGet(() -> "Can't find in library author with name = " + authorName);
    }

    @ShellMethod(value = "Delete author by name", key = {"delete-author", "da"})
    public void deleteAuthor(String authorName) {
        authorService.delete(new Author(authorName));
    }

    @ShellMethod(value = "Get all authors", key = {"get-authors", "alla"})
    public String getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return String.join(COMMA, authors.stream().map(Author::getName).toList());
    }

    @ShellMethod(value = "Add book", key = {"add-book", "ab"})
    public void addBook(
            String bookName,
            int yearOfRelease,
            String authorName,
            String genreName,
            @ShellOption(defaultValue = "Bestseller!") String description
    ) {
        Book book = Book.builder()
                .name(bookName)
                .description(description)
                .yearOfRelease(yearOfRelease).author(new Author(authorName))
                .genre(new Genre(genreName))
                .build();
        libraryService.add(book);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public void updateBook(
            String bookName,
            int yearOfRelease,
            String authorName,
            String genreName,
            @ShellOption(defaultValue = "Bestseller!") String description
    ) {
        Book book = Book.builder()
                .name(bookName)
                .description(description)
                .yearOfRelease(yearOfRelease)
                .author(new Author(authorName))
                .genre(new Genre(genreName))
                .build();
        libraryService.update(book);
    }

    @ShellMethod(value = "Delete book by name and author name", key = {"delete-book", "db"})
    public void deleteBook(String bookName, String authorName) {
        Book book = Book.builder()
                .name(bookName)
                .author(new Author(authorName))
                .build();
        libraryService.delete(book);
    }

    @ShellMethod(value = "Get all books", key = {"get-books", "allb"})
    public String getAllBooks() {
        List<Book> books = libraryService.findAll();
        return String.join(COMMA, books.stream()
                .map(this::getBookView).toList());
    }

    @ShellMethod(value = "Find book by name and author name",
            key = {"find-book-by-name-and-author", "fbna"})
    public String findBookByNameAndAuthor(String bookName, String authorName) {
        Optional<Book> findBook = libraryService.findByNameAndAuthor(bookName, new Author(authorName));
        if (findBook.isEmpty()) {
            return "Can't find in library book with name = " + bookName + " and author = " + authorName;
        }
        return getBookView(findBook.get());
    }

    @ShellMethod(value = "Find books by author name",
            key = {"find-books-by-author", "fbsa"})
    public String findBooksByAuthor(String authorName) {
        List<Book> books = libraryService.findByAuthor(new Author(authorName));
        return String.join(COMMA, books.stream()
                .map(this::getBookView).toList());
    }

    @ShellMethod(value = "Find books by release year and genre name",
            key = {"find-books-by-year-and-genre", "fbsyg"})
    public String findBooksByReleaseYearAndGenre(int yearOfRelease, String genreName) {
        List<Book> books = libraryService.findByYearOfReleaseAndGenre(yearOfRelease, new Genre(genreName));
        return String.join(COMMA, books.stream()
                .map(this::getBookView).toList());
    }

    @ShellMethod(value = "Add book's comment by book name and author name",
            key = {"add-book-comment", "abc"})
    public void addBookComment(String comment, String bookName, String authorName) {
        bookCommentService.add(comment, bookName, authorName);
    }

    @ShellMethod(value = "Find book's comments by book name and author name",
            key = {"get-books-comments", "gbc"})
    public String getBookComments(String bookName, String authorName) {
        List<BookComment> comments = bookCommentService.findByBookAndAuthor(bookName, authorName);
        return String.join(
                COMMA,
                comments.stream()
                        .map(bc -> "{id=" + bc.getId() + ", text=" + bc.getText() + "}")
                        .toList());
    }

    private String getBookView(Book book) {
        if (book == null) {
            return "";
        }
        return "{name=" + book.getName() + ", author=" + book.getAuthor().getName()
                + ", genre=" + book.getGenre().getName()
                + ", release year=" + book.getYearOfRelease()
                + ((book.getDescription() == null) ? "" : ", description=" + book.getDescription()) + "}";
    }
}
