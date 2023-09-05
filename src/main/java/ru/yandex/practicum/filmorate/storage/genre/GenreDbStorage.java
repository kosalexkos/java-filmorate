package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresList() {
        String sqlQuery = "SELECT * FROM Genres";
        return jdbcTemplate.query(sqlQuery, this::getGenreFromDb);
    }

    @Override
    public Genre getGenre(Integer id) throws ResponseStatusException {
        Genre genre;
        String sqlQuery = "SELECT * FROM Genres WHERE genre_id = ?";
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::getGenreFromDb, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Genre with an id = %s not found", id));
        }
        return genre;
    }

    private Genre getGenreFromDb(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}