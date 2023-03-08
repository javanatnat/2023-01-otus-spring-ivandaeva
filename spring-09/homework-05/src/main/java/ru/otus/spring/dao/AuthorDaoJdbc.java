package ru.otus.spring.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.LibraryDBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private static final String ID = "id";
    private static final String NAME = "name";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = namedParameterJdbcOperations.queryForObject(
                "select count(*) from authors",
                new EmptySqlParameterSource(),
                Long.class);
        return count == null? 0: count;
    }

    @Override
    public Author insert(Author author) {
        String name = author.getName();
        Optional<Author> findAuthor = findByName(name);
        if (findAuthor.isEmpty()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource(NAME, name);
            namedParameterJdbcOperations.update(
                    "insert into authors (name) values(:name)",
                    params,
                    keyHolder,
                    new String[] {"id"});
            long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            return new Author(newId, name);
        } else {
            return findAuthor.get();
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        List<Author> authors = namedParameterJdbcOperations.query(
                "select id, name from authors where id = :id",
                Map.of(ID, id),
                new AuthorMapper());

        if (authors.isEmpty()) {
            return Optional.empty();
        } else if (authors.size() == 1) {
            return Optional.of(authors.get(0));
        } else {
            throw new LibraryDBException("There are more than one author in library with id = " + id);
        }
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query(
                "select id, name from authors",
                new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from authors where id = :id",
                Map.of(ID, id)
        );
    }

    @Override
    public Optional<Author> findByName(String name) {
        List<Author> authors = namedParameterJdbcOperations.query(
                "select id, name from authors where name = :name",
                Map.of(NAME, name),
                new AuthorMapper());

        if (authors.isEmpty()) {
            return Optional.empty();
        } else if (authors.size() == 1) {
            return Optional.of(authors.get(0));
        } else {
            throw new LibraryDBException("There are more than one author in library with name = " + name);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong(ID);
            String name = resultSet.getString(NAME);
            return new Author(id, name);
        }
    }
}
