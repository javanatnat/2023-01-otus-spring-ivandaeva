package ru.otus.spring.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryDBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BOOK_NAME = "book_name";
    private static final String AUTHOR_NAME = "author_name";
    private static final String GENRE_NAME = "genre_name";
    private static final String DESCRIPTION = "description";
    private static final String RELEASE_YEAR = "release_year";
    private static final String AUTHOR_ID = "author_id";
    private static final String GENRE_ID = "genre_id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = namedParameterJdbcOperations.queryForObject(
                "select count(*) from books",
                new EmptySqlParameterSource(),
                Long.class);
        return count == null? 0: count;
    }

    @Override
    public Book insert(Book book) {
        Optional<Book> findBook = findByNameAndAuthor(book.getName(), book.getAuthor());
        if (findBook.isEmpty()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource(
                    Map.of(
                            BOOK_NAME, book.getName(),
                            DESCRIPTION, book.getDescription(),
                            RELEASE_YEAR, book.getYearOfRelease(),
                            AUTHOR_ID, book.getAuthor().getId(),
                            GENRE_ID, book.getGenre().getId())
                    );
            namedParameterJdbcOperations.update(
                    "insert into books (name, description, release_year, author_id, genre_id)" +
                            " values(:book_name, :description, :release_year, :author_id, :genre_id)",
                    params,
                    keyHolder,
                    new String[] {"id"});
            long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            return new Book(
                    newId,
                    book.getName(),
                    book.getDescription(),
                    book.getYearOfRelease(),
                    book.getAuthor(),
                    book.getGenre());
        } else {
            return findBook.get();
        }
    }

    @Override
    public Book update(Book book) {
        Optional<Book> findBook = getById(book.getId());
        if (findBook.isEmpty()) {
            throw new LibraryDBException("Can't find book for update in library with name = " + book.getName()
                    + " and author = " + book.getAuthor().getName());
        }

        Book oldBook = findBook.get();
        if (!oldBook.getDescription().equals(book.getDescription()) ||
                !(oldBook.getYearOfRelease() == book.getYearOfRelease()) ||
                !(oldBook.getGenre().getId().equals(book.getGenre().getId()))
        ) {
            namedParameterJdbcOperations.update(
                    "update books set description = :description," +
                            " release_year = :release_year," +
                            " genre_id = :genre_id" +
                            " where id = :id",
                    new MapSqlParameterSource(
                            Map.of(DESCRIPTION, book.getDescription(),
                                    RELEASE_YEAR, book.getYearOfRelease(),
                                    GENRE_ID, book.getGenre().getId(),
                                    ID, book.getId())
                    )
            );
            return new Book(
                    book.getId(),
                    book.getName(),
                    book.getDescription(),
                    book.getYearOfRelease(),
                    book.getAuthor(),
                    book.getGenre());
        } else {
            return book;
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        List<Book> books = namedParameterJdbcOperations.query(
                "select b.id as id," +
                        " b.name as book_name," +
                        " b.description as description," +
                        " b.release_year as release_year," +
                        " b.author_id as author_id," +
                        " b.genre_id as genre_id," +
                        " a.name as author_name," +
                        " g.name as genre_name" +
                        " from books b" +
                        " join authors a" +
                        " on b.author_id = a.id" +
                        " join genres g" +
                        " on b.genre_id = g.id" +
                        " where b.id = :id",
                Map.of(ID, id),
                new BookMapper()
        );

        if (books.isEmpty()) {
            return Optional.empty();
        } else if (books.size() == 1) {
            return Optional.of(books.get(0));
        } else {
            throw new LibraryDBException("There are more than one book in library with id = " + id);
        }
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query(
                "select b.id as id," +
                        " b.name as book_name," +
                        " b.description as description," +
                        " b.release_year as release_year," +
                        " b.author_id as author_id," +
                        " b.genre_id as genre_id," +
                        " a.name as author_name," +
                        " g.name as genre_name" +
                        " from books b" +
                        " join authors a" +
                        " on b.author_id = a.id" +
                        " join genres g" +
                        " on b.genre_id = g.id",
                new BookMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from books where id = :id",
                Map.of(ID, id)
        );
    }

    @Override
    public Optional<Book> findByNameAndAuthor(String name, Author author) {
        List<Book> books = namedParameterJdbcOperations.query(
                "select b.id as id," +
                        " b.name as book_name," +
                        " b.description as description," +
                        " b.release_year as release_year," +
                        " b.author_id as author_id," +
                        " b.genre_id as genre_id," +
                        " a.name as author_name," +
                        " g.name as genre_name" +
                        " from books b" +
                        " join authors a" +
                        " on b.author_id = a.id" +
                        " join genres g" +
                        " on b.genre_id = g.id" +
                        " where b.name = :book_name" +
                        " and a.name = :author_name",
                new MapSqlParameterSource(
                        Map.of(BOOK_NAME, name,
                                AUTHOR_NAME, author.getName())
                ),
                new BookMapper()
        );
        if (books.isEmpty()) {
            return Optional.empty();
        } else if (books.size() == 1) {
            return Optional.of(books.get(0));
        } else {
            throw new LibraryDBException("There are more than one book in library with name = " + name
                    + " and author = " + author.getName());
        }
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        return namedParameterJdbcOperations.query(
                "select b.id as id," +
                        " b.name as book_name," +
                        " b.description as description," +
                        " b.release_year as release_year," +
                        " b.author_id as author_id," +
                        " b.genre_id as genre_id," +
                        " a.name as author_name," +
                        " g.name as genre_name" +
                        " from books b" +
                        " join authors a" +
                        " on b.author_id = a.id" +
                        " join genres g" +
                        " on b.genre_id = g.id" +
                        " where a.name = :author_name",
                Map.of(AUTHOR_NAME, author.getName()),
                new BookMapper()
        );
    }

    @Override
    public List<Book> findBooksByReleaseYearAndGenre(int yearOfRelease, Genre genre) {
        return namedParameterJdbcOperations.query(
                "select b.id as id," +
                        " b.name as book_name," +
                        " b.description as description," +
                        " b.release_year as release_year," +
                        " b.author_id as author_id," +
                        " b.genre_id as genre_id," +
                        " a.name as author_name," +
                        " g.name as genre_name" +
                        " from books b" +
                        " join authors a" +
                        " on b.author_id = a.id" +
                        " join genres g" +
                        " on b.genre_id = g.id" +
                        " where b.release_year = :release_year" +
                        " and g.name = :genre_name",
                new MapSqlParameterSource(
                        Map.of(RELEASE_YEAR, yearOfRelease,
                                GENRE_NAME, genre.getName())
                ),
                new BookMapper()
        );
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong(ID);
            String bookName = resultSet.getString(BOOK_NAME);
            String description = resultSet.getString(DESCRIPTION);
            int yearOfRelease = resultSet.getInt(RELEASE_YEAR);

            long authorId = resultSet.getLong(AUTHOR_ID);
            String authorName = resultSet.getString(AUTHOR_NAME);

            long genreId = resultSet.getLong(GENRE_ID);
            String genreName = resultSet.getString(GENRE_NAME);

            return new Book(
                    id, bookName, description, yearOfRelease,
                    new Author(authorId, authorName),
                    new Genre(genreId, genreName));
        }
    }
}
