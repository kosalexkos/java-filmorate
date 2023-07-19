package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@Getter
@RequestMapping(value = "/films", produces = "application/json")
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private final FilmService service;

    @GetMapping
    public List<Film> getAll() {
        log.info("Request to get list of all films");
        return service.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Request to create a new film");
        return service.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Request to update film");
        service.updateFilm(film);
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Request to get film by id: {}", id);
        return service.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        log.info("Request to delete film by id: {}", id);
        service.deleteFilmById(id);
        return ResponseEntity.ok().body("Film with id : " + id + " was successfully deleted");
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAll() {
        log.info("Request to delete all films");
        service.deleteAllFilms();
        return ResponseEntity.ok().body("All films were deleted");
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Request from user with id {} to like film with id {}", userId, id);
        int i = service.addLikeToFilm(id, userId);
        return ResponseEntity.ok().body("Like was successfully added. " +
                "The amount of likes is " + i);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Request from user with id {} to delete like from film with id {}", userId, id);
        int i = service.deleteLike(id, userId);
        return ResponseEntity.ok().body("Like was successfully deleted. " +
                "The amount of likes is " + i);
    }

    @GetMapping("/popular")
    public ResponseEntity<Set<Film>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Request to get top ten films");
        Set<Film> topTen = service.getTopTen(count);
        return ResponseEntity.ok(topTen);
    }
}