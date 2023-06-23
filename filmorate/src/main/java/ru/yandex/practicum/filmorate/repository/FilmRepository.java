package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmRepository {
    @Getter
    private int id;
    @Getter
    private final Map<Integer, Film> films;

    public FilmRepository () {
        this.id = 0;
        this.films = new HashMap<>();
    }
    public void save (Film f) {
        this.films.put(f.getId(), f);
        log.info("Film was successfully saved");
    }
    public int generateId() {
        log.info("Generated an id for film");
        return this.id++;
    }
    public Film getById(int id) throws IOException {
        if (!films.containsKey(id)) {
            throw new IOException("Incorrect id");
        }
        log.info("Returned film by id");
        return films.get(id);
    }
}
