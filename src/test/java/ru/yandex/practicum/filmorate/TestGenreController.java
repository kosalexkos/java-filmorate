package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(scripts = {"file:src/main/resources/schema.sql", "file:src/main/resources/data.sql"})
public class TestGenreController {
    private GenreController controller;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        controller = new GenreController(new GenreService(new GenreDbStorage(jdbcTemplate)));
    }

    @Test
    void shouldReturnListOfGenres() {
        Genre g1 = new Genre(1,"Комедия");
        Genre g2 = new Genre(2,"Драма");
        Genre g3 = new Genre(3,"Мультфильм");
        Genre g4 = new Genre(4,"Триллер");
        Genre g5 = new Genre(5,"Документальный");
        Genre g6 = new Genre(6,"Боевик");
        Genre g7 = new Genre(125, "Аниме");
        List<Genre> l = new ArrayList<>(controller.getAllGenres());
        assertEquals(6, l.size());
        assertTrue(l.contains(g1));
        assertTrue(l.contains(g2));
        assertTrue(l.contains(g3));
        assertTrue(l.contains(g4));
        assertTrue(l.contains(g5));
        assertTrue(l.contains(g6));
        assertFalse(l.contains(g7));
    }

    @Test
    void shouldReturnParticularGenre() {
        Genre g1 = new Genre(1,"Комедия");
        Genre g2 = new Genre(2,"Драма");
        Genre g3 = new Genre(3,"Мультфильм");
        Genre g4 = new Genre(4,"Триллер");
        Genre g5 = new Genre(5,"Документальный");
        Genre g6 = new Genre(6,"Боевик");
        assertEquals(g1,controller.getSingleGenre(1));
        assertEquals(g2,controller.getSingleGenre(2));
        assertEquals(g3,controller.getSingleGenre(3));
        assertEquals(g4,controller.getSingleGenre(4));
        assertEquals(g5,controller.getSingleGenre(5));
        assertEquals(g6,controller.getSingleGenre(6));
    }
}