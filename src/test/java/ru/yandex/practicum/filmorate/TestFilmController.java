package ru.yandex.practicum.filmorate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class TestFilmController {
    Validator validator;
    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        filmController = new FilmController(new FilmRepository());
    }

    @Test
    void shouldCreateFilm() {
        Film film = filmController.createFilm(new Film("007", "James Bond film",
                LocalDate.of(1968,12,16), 244));
        Film f = filmController.getAll().get(0);
        assertEquals(1, f.getId());
        assertEquals(film.getName(), f.getName());
        assertEquals(film.getDescription(), f.getDescription());
        assertEquals(film.getReleaseDate(), f.getReleaseDate());
        assertEquals(film.getDuration(), f.getDuration());
    }

    @Test
    void shouldUpdateFilm() {
        Film f1 = (new Film("AAA", "A",
                LocalDate.of(1999,12,9), 400));
        filmController.createFilm(f1);
        Film f2 = new Film("А", "AAA",
                LocalDate.of(2000,8,15), 350);
        f2.setId(f1.getId());
        filmController.updateFilm(f2);
        Film f3 = filmController.getAll().get(0);
        assertEquals(1, filmController.getAll().size());
        assertEquals(f3, filmController.getAll().get(0));
    }

    @Test
    void shouldReturnListOfFilms() {
        List<Film> l = List.of(
                filmController.createFilm(new Film("AAA", "AAAAAAAAA",
                LocalDate.of(1986,4,26), 598)),
                filmController.createFilm(new Film("AA", "AAAAAA",
                LocalDate.of(1996,8,15), 441)));
        assertEquals(2, filmController.getAll().size());
        assertEquals(l, filmController.getAll());
    }

    @Test
    void createEmptyNameFilm() {
        Film f = new Film("","AAAAA",
                LocalDate.of(1995,12,27), 259);
        filmController.createFilm(f);
        Set<ConstraintViolation<Film>> violations = validator.validate(f);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDescriptionFilm() {
        Film f = new Film("AAAAAAA", new String(new char[205]),
                LocalDate.of(2005,2,8), 456);
        filmController.createFilm(f);
        Set<ConstraintViolation<Film>> violations = validator.validate(f);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDateFilm() {
        Film film = new Film("aaaa", "bbbbbb",
                LocalDate.of(1895, 11, 25), 233);
        filmController.createFilm(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createWrongDurationFilm() {
        Film film = new Film("SSSSSSS", "sssssssssss",
                LocalDate.of(2015, 9, 10), -10);
        filmController.createFilm(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}