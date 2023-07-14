package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@Getter
@RequestMapping(value = "/films", produces = "application/json")
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private final FilmRepository filmsStorage;

    @GetMapping
    public List<Film> getAll() {
        log.info("Request to get list of all users");
        return filmsStorage.getData();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Request to create a new film");
        filmsStorage.save(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!filmsStorage.validateId(film.getId())) {
            log.info("Failed to find film to update");
            throw new ValidationException("Failed to update film data. Film not found");
        }
        log.info("Request to update user");
        filmsStorage.update(film);
        return film;
    }
}