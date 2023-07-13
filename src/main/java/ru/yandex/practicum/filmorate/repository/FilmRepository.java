package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FilmRepository {
    private int id;
    @Getter
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
}