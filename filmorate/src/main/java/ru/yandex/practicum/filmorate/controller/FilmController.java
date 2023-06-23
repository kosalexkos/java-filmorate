package ru.yandex.practicum.filmorate.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Getter
@NoArgsConstructor
@RequestMapping("/films")

public class FilmController {
    private FilmRepository filmsStorage;
    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        try {
            Film f = filmsStorage.getById(id);
            log.info("Request to get film by id");
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping
    public List<Film> getAll () {
        log.info("Request to get list of all users");
        return new ArrayList<>(filmsStorage.getFilms().values());
    }

    @PostMapping
    public void createFilm (@RequestBody Film film) {
        FilmValidator.validateFilm(film);
        log.info("Request to create a new film");
        film.setId(filmsStorage.generateId());
        filmsStorage.save(film);
    }
    @PutMapping("/{userId}")
    public void renewFilm (@RequestBody Film film) {
        FilmValidator.validateFilm(film);
        if (!filmsStorage.getFilms().containsKey(film.getId())) {
            throw new RuntimeException("Failed to update film data. Film not found");
        }
        log.info("Request to update user");
        filmsStorage.getFilms().put(film.getId(), film);
    }
}
