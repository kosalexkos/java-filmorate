package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    private final FilmStorage storage;

    public Film createFilm(Film film) {
        log.info("Processing film creation");
        storage.save(film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Processing request to update film");
        Film f = storage.update(film);
        return f;
    }

    public List<Film> getAllFilms() {
        log.info("Processing request to get all films");
        return storage.getData();
    }

    public void deleteFilmById(int id) {
        log.info("Processing request to delete film by id");
        storage.deleteById(id);
    }

    public void deleteAllFilms() {
        log.info("Processing request to delete all films");
        storage.deleteAll();
    }

    public Film getFilmById(int id) {
        log.info("Processing request to get film by id");
        return storage.getById(id);
    }

    public void addLikeToFilm(int filmId, int userId) {
        log.info("Processing request to like film");
        storage.addLike(filmId,userId);
    }

    public void deleteLike(int filmId, int userId) {
        log.info("Processing like deleting");
        storage.deleteLike(userId,filmId);
        log.info("Like was deleted");
    }

    public List<Film> getTopFilms(int count) {
        return new ArrayList<>(storage.getTop(count));
    }
}