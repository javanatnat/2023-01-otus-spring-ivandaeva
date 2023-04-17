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
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class ShellCommands {
    private static final String COMMA = ", ";
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;
    private final BookCommentService bookCommentService;

    @Autowired
    public ShellCommands(
            AuthorService authorService,
            GenreService genreService,
            BookService bookService,
            BookCommentService bookCommentService
    ) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod(value = "Add author", key = {"add-author", "aa"})
    public void addAuthor(String authorName) {
        Optional<Author> findAuthor = authorService.findByName(authorName);
        if (findAuthor.isEmpty()) {
            authorService.add(authorName);
        }
    }

    @ShellMethod(value = "Find author by name", key = {"find-author", "fa"})
    public String findAuthor(String authorName) {
        Optional<Author> findAuthor = authorService.findByName(authorName);
        return findAuthor.map(author -> "Find author in library: " + author.getName())
                .orElseGet(() -> "Can't find in library author with name = " + authorName);
    }

    @ShellMethod(value = "Delete author by name", key = {"delete-author", "da"})
    public void deleteAuthor(String authorName) {
        Optional<Author> findAuthor = authorService.findByName(authorName);
        findAuthor.ifPresent(authorService::delete);
    }

    @ShellMethod(value = "Get all authors", key = {"get-authors", "alla"})
    public String getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return String.join(COMMA, authors.stream().map(Author::getName).toList());
    }

    @ShellMethod(value = "Add genre", key = {"add-genre", "ag"})
    public void addGenre(String genreName) {
        Optional<Genre> findGenre = genreService.findByName(genreName);
        if (findGenre.isEmpty()) {
            genreService.add(genreName);
        }
    }

    @ShellMethod(value = "Find genre by name", key = {"find-genre", "fg"})
    public String findGenre(String genreName) {
        Optional<Genre> findGenre = genreService.findByName(genreName);
        return findGenre.map(genre -> "Find genre in library: " + genre.getName())
                .orElseGet(() -> "Can't find in library genre with name = " + genreName);
    }

    @ShellMethod(value = "Delete genre by name", key = {"delete-genre", "dg"})
    public void deleteGenre(String genreName) {
        Optional<Genre> findGenre = genreService.findByName(genreName);
        findGenre.ifPresent(genreService::delete);
    }

    @ShellMethod(value = "Get all genres", key = {"get-genres", "allg"})
    public String getAllGenres() {
        List<Genre> genres = genreService.findAll();
        return String.join(COMMA, genres.stream().map(Genre::getName).toList());
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
                .yearOfRelease(yearOfRelease)
                .author(new Author(authorName))
                .genre(new Genre(genreName))
                .build();
        bookService.addWithRefs(book);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public void updateBook(
            String bookName,
            int yearOfRelease,
            String authorName,
            String genreName,
            @ShellOption(defaultValue = "Bestseller!") String description
    ) {
        Optional<Book> findBook = bookService.findByNameAndAuthorName(bookName, authorName);
        if (findBook.isPresent()) {
            Book book = findBook.get();
            book.setYearOfRelease(yearOfRelease);
            book.setDescription(description);
            book.setGenre(new Genre(genreName));
            bookService.updateWithRefs(book);
        }
    }

    @ShellMethod(value = "Delete book by name and author name", key = {"delete-book", "db"})
    public void deleteBook(String bookName, String authorName) {
        Optional<Book> findBook = bookService.findByNameAndAuthorName(bookName, authorName);
        findBook.ifPresent(bookService::delete);
    }

    @ShellMethod(value = "Get all books", key = {"get-books", "allb"})
    public String getAllBooks() {
        List<Book> books = bookService.findAll();
        return String.join(COMMA, books.stream()
                .map(this::getBookView).toList());
    }

    @ShellMethod(value = "Find book by name and author name",
            key = {"find-book-by-name-and-author", "fbna"})
    public String findBookByNameAndAuthor(String bookName, String authorName) {
        Optional<Book> findBook = bookService.findByNameAndAuthorName(bookName, authorName);
        if (findBook.isEmpty()) {
            return "Can't find in library book with name = " + bookName + " and author = " + authorName;
        }
        return getBookView(findBook.get());
    }

    @ShellMethod(value = "Find books by author name",
            key = {"find-books-by-author", "fbsa"})
    public String findBooksByAuthor(String authorName) {
        List<Book> books = bookService.findByAuthor(new Author(authorName));
        return String.join(COMMA, books.stream()
                .map(this::getBookView).toList());
    }

    @ShellMethod(value = "Find books by release year and genre name",
            key = {"find-books-by-year-and-genre", "fbsyg"})
    public String findBooksByReleaseYearAndGenre(int yearOfRelease, String genreName) {
        List<Book> books = bookService.findByYearOfReleaseAndGenre(yearOfRelease, new Genre(genreName));
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
        return "{name=" + book.getName() + ", author=" + book.getAuthorName()
                + ", genre=" + book.getGenreName()
                + ", release year=" + book.getYearOfRelease()
                + ((book.getDescription() == null) ? "" : ", description=" + book.getDescription()) + "}";
    }
}
