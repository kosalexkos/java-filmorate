package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void save(Film f);

    List<Film> getData();

    Film update(Film f);

    Film getById(int id);

    void deleteById(int id);

    void deleteAll();

    List<Film> getTop(int count);

    void addLike(int userId, int filmId) throws ResponseStatusException;

    void deleteLike(int userId, int filmId);
}