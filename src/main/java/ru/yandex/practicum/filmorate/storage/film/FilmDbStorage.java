package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Film f) {
        if (containsFilm(f)) {
            log.info("This film has already been saved");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This film has already been saved");
        }
        Integer filmId = addFilmToDb(f);
        f.setId(filmId);
        if (f.getGenres() != null && f.getGenres().size() > 0) {
            String query = "INSERT INTO Films_Genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : f.getGenres()) {
                jdbcTemplate.update(query, filmId, genre.getId());
            }
        }
        log.info("Film was successfully saved");
    }

    @Override
    public Film update(Film f) {
        if (!containsFilm(f.getId())) {
            log.info("Wrong id. Film not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong id. Film not found");
        }
        try {
            String query1 = "DELETE FROM Films_Genres WHERE film_id = ?";
            jdbcTemplate.update(query1, f.getId());
        } catch (EmptyResultDataAccessException e) {
            log.info("Film didn't have genres");
        }
        String query2 = "UPDATE Films " +
                "SET name = ?, description = ?, " +
                "release_date = ?, duration = ?, mpa = ? WHERE film_id = ?";
        jdbcTemplate.update(query2, f.getName(), f.getDescription(), f.getReleaseDate(),
                f.getDuration(), f.getMpa().getId(), f.getId());
        if (f.getGenres() != null && f.getGenres().size() > 0) {
            String query3 = "INSERT INTO Films_Genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre g : f.getGenres()) {
                jdbcTemplate.update(query3, f.getId(), g.getId());
            }
        }
        Film f2 = getById(f.getId());
        log.info("Film was successfully updated");
        return f2;
    }

    @Override
    public List<Film> getData() {
        String query = "SELECT Films.*, MPA.mpa_name FROM Films JOIN MPA ON Films.mpa = MPA.mpa_id";
        return jdbcTemplate.query(query, this::getFilmFromDb);
    }

    @Override
    public Film getById(int id) {
        String query = "SELECT film_id, name, description, release_date, duration, " +
                "Films.mpa, MPA.mpa_name FROM Films " +
                "JOIN MPA ON Films.mpa = MPA.mpa_id WHERE Films.film_id = ?";
        try {
            Film f = jdbcTemplate.queryForObject(query, this::getFilmFromDb, id);
            return f;
        } catch (EmptyResultDataAccessException e) {
            log.info("Wrong id. Film not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong id. Film not found");
        }
    }

    @Override
    public void deleteById(int id) {
        if (!containsFilm(id)) {
            log.info("Film not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Film with the id = " + id + " not found");
        }
        String query = "DELETE FROM Films WHERE film_id = ?";
        jdbcTemplate.update(query, id);
        log.info("Film was deleted");
    }

    @Override
    public void deleteAll() {
        String deleteFilmsQuery = "DELETE FROM Films";
        jdbcTemplate.update(deleteFilmsQuery);
        log.info("All films were successfully deleted");
    }

    @Override
    public List<Film> getTop(int count) {
        String query = "SELECT f.film_id, f.name, f.description, " +
                "f.release_date, f.duration, f.mpa, m.mpa_name FROM Films f " +
                "LEFT JOIN Likes l ON f.film_id = l.film_id " +
                "LEFT JOIN MPA m ON f.mpa = m.mpa_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) DESC " +
                "LIMIT ?";
        return new ArrayList<>(jdbcTemplate.query(query, this::getFilmFromDb, count));
    }

    @Override
    public void addLike(int filmId, int userId) throws  ResponseStatusException {
        if (!containsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong user id. User not  found");
        }
        if (!containsFilm(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong film id. Film not found");
        }
        String query = "INSERT INTO Likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(query, filmId, userId);
            log.info("The film was liked");
        } catch (DuplicateKeyException e) {
            log.info("User has already liked this film");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already liked this film");
        } catch (DataIntegrityViolationException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "It is not allowed to like not existing films");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (!containsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong user id. User not  found");
        }
        if (!containsFilm(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong film id. Film not found");
        }
        String query = "DELETE FROM Likes WHERE user_id = ? AND film_id = ?";
        if (jdbcTemplate.update(query, userId, filmId) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User has never liked this film");
        }
    }

    private int addFilmToDb(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("film_id");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
    }

    private boolean containsFilm(Film film) {
        String query = "SELECT f.*, MPA.mpa_name FROM Films AS f " +
                "JOIN MPA ON f.mpa = MPA.mpa_id " +
                "WHERE f.name = ? AND  f.description = ? " +
                "AND f.release_date = ? AND f.duration = ? AND f.mpa = ?";
        try {
            jdbcTemplate.queryForObject(query, this::getFilmFromDb, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean containsUser(Integer userId) {
        String query = "SELECT * FROM Users WHERE user_id = ?";
        try {
            jdbcTemplate.queryForObject(query, this::getUserFromDb, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean containsFilm(Integer filmId) {
        String sqlQuery = "SELECT f.*, MPA.mpa_name FROM Films AS f " +
                "JOIN MPA ON f.mpa = MPA.mpa_id WHERE f.film_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::getFilmFromDb, filmId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private User getUserFromDb(ResultSet resultSet, int rowNum) throws SQLException {
        User u = new User(resultSet.getString("email"),
                resultSet.getString("login"), resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
        u.setId(resultSet.getInt("user_id"));
        return u;
    }

    private Film getFilmFromDb(ResultSet resultSet, int rowSum) throws SQLException {
        Film f = new Film(resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                new MPA(resultSet.getInt("mpa"), resultSet.getString("mpa_name")));
        f.setId(resultSet.getInt("film_id"));
        String query = "SELECT Users.* FROM Likes " +
                "JOIN Users ON Likes.user_id = Users.user_id WHERE Likes.film_id=?";
        f.getLikes().addAll(jdbcTemplate.query(query, this::getUserFromDb, f.getId()));
        String queryForG = "SELECT fg.genre_id, g.genre_name FROM Films_Genres AS fg " +
                "JOIN Genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        f.getGenres().addAll(jdbcTemplate.query(queryForG,
                this::getGenreFromDb, f.getId()));
        return f;
    }

    private Genre getGenreFromDb(ResultSet rs, int rowSum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}