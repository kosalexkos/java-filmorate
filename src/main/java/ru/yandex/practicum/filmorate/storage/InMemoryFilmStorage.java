package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private final Map<Integer, Film> films;

    public InMemoryFilmStorage() {
        this.id = 0;
        this.films = new HashMap<>();
    }

    @Override
    public void save(Film f) {
        f.setId(generateId());
        this.films.put(f.getId(), f);
        log.info("Film was successfully saved");
    }

    private int generateId() {
        log.info("An id for the film was generated");
        return ++id;
    }

    @Override
    public List<Film> getData() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean validateId(int id) {
        return films.containsKey(id);
    }

    @Override
    public void update(Film f) {
        films.put(f.getId(), f);
        log.info("Film was successfully updated");
    }

    @Override
    public Film getById(int id) {
        if (!films.containsKey(id)) {
            log.info("Film not found");
            throw new DataNotFoundException("Wrong film id");
        }
        return films.get(id);
    }

    @Override
    public void deleteById(int id) {
        if (!films.containsKey(id)) {
            log.info("Request to delete not existing film cannot be processed");
            throw new DataNotFoundException("Cannot delete film. Object  not found");
        }
        films.remove(id);
        log.info("Film was deleted");
    }

    @Override
    public void deleteAll() {
        films.clear();
        log.info("All films were successfully deleted");
    }

    @Override
    public List<Film> getTop(int count) {
        return films.values().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size())))
                .limit(count)
                .collect(Collectors.toList());
    }
}