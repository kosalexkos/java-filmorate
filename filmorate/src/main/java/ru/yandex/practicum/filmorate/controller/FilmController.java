package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Getter
@RequestMapping("/films")

public class FilmController {
    private FilmRepository filmsStorage;

    public FilmController() {
        filmsStorage = new FilmRepository();
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Request to get list of all users");
        return new ArrayList<>(filmsStorage.getFilms().values());
    }

    @PostMapping
    public void createFilm(@Valid @RequestBody Film film) {
        log.info("Request to create a new film");
        film.setId(filmsStorage.generateId());
        filmsStorage.save(film);
    }

    @PutMapping("/{userId}")
    public void updateFilm(@Valid @RequestBody Film film) {
        if (!filmsStorage.getFilms().containsKey(film.getId())) {
            log.info("Failed to find film to update");
            throw new ValidationException("Failed to update film data. Film not found");
        }
        log.info("Request to update user");
        filmsStorage.getFilms().put(film.getId(), film);
    }
}