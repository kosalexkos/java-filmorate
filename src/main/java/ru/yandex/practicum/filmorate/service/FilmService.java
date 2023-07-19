package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
        if (!storage.validateId(film.getId())) {
            log.info("Failed to find film to update");
            throw new DataNotFoundException("Failed to update film data. Film not found");
        }
        Set<Integer> likes = storage.getById(film.getId()).getLikes();
        film.setLikes(likes);
        storage.update(film);
        return film;
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

    public int addLikeToFilm(int filmId, int userId) {
        log.info("Processing request to like film");
        Film f = storage.getById(filmId);
        f.getLikes().add(userId);
        log.info("Film with id " + filmId + " was liked by user with id " + userId);
        return f.getLikes().size();
    }

    public int deleteLike(int filmId, int userId) {
        log.info("Processing like deleting");
        Film f = storage.getById(filmId);
        if (!f.getLikes().contains(userId)) {
            throw new DataNotFoundException("User didn't like this film");
        }
        f.getLikes().remove(userId);
        log.info("Like was deleted");
        return f.getLikes().size();
    }

    public TreeSet<Film> getTopTen(int count) {
        return storage.getData().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}