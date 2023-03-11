package ru.otus.spring.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryDBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private static final String ID = "id";
    private static final String NAME = "name";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = namedParameterJdbcOperations.queryForObject(
                "select count(*) from genres",
                new EmptySqlParameterSource(),
                Long.class);
        return count == null? 0: count;
    }

    @Override
    public Genre insert(Genre genre) {
        String name = genre.getName();
        Optional<Genre> findGenre = findByName(name);
        if (findGenre.isEmpty()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource(NAME, name);
            namedParameterJdbcOperations.update(
                    "insert into genres (name) values(:name)",
                    params,
                    keyHolder,
                    new String[] {"id"});
            long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            return new Genre(newId, name);
        } else {
            return findGenre.get();
        }
    }

    @Override
    public Optional<Genre> getById(long id) {
        List<Genre> genres = namedParameterJdbcOperations.query(
                "select id, name from genres where id = :id",
                Map.of(ID, id),
                new GenreMapper());

        if (genres.isEmpty()) {
            return Optional.empty();
        } else if (genres.size() == 1) {
            return Optional.of(genres.get(0));
        } else {
            throw new LibraryDBException("There are more than one genre in library with id = " + id);
        }
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query(
                "select id, name from genres",
                new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from genres where id = :id",
                Map.of(ID, id)
        );
    }

    @Override
    public Optional<Genre> findByName(String name) {
        List<Genre> genres = namedParameterJdbcOperations.query(
                "select id, name from genres where name = :name",
                Map.of(NAME, name),
                new GenreMapper());

        if (genres.isEmpty()) {
            return Optional.empty();
        } else if (genres.size() == 1) {
            return Optional.of(genres.get(0));
        } else {
            throw new LibraryDBException("There are more than one genre in library with name = " + name);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong(ID);
            String name = resultSet.getString(NAME);
            return new Genre(id, name);
        }
    }
}
