package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilmRepository {
    private int id;
    private final Map<Integer, Film> films;

    public FilmRepository() {
        this.id = 0;
        this.films = new HashMap<>();
    }

    public void save(Film f) {
        f.setId(generateId());
        this.films.put(f.getId(), f);
        log.info("Film was successfully saved");
    }

    private int generateId() {
        log.info("An id for the film was generated");
        return ++id;
    }

    public List<Film> getData() {
        return new ArrayList<>(films.values());
    }

    public boolean validateId(int id) {
        return films.containsKey(id);
    }

    public void update(Film f) {
        films.put(f.getId(), f);
        log.info("Film was successfully updated");
    }
}