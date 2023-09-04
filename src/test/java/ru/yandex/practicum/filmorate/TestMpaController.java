package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(scripts = {"file:src/main/resources/schema.sql", "file:src/main/resources/data.sql"})
public class TestMpaController {
    private MpaController controller;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        controller = new MpaController(new MpaService(new MpaDbStorage(jdbcTemplate)));
    }

    @Test
    void shouldReturnListOfMPA() {
        MPA m1 = new MPA(1,"G");
        MPA m2 = new MPA(2,"PG");
        MPA m3 = new MPA(3,"PG-13");
        MPA m4 = new MPA(4,"R");
        MPA m5 = new MPA(5,"NC-17");
        MPA m6 = new MPA(25, "GG");
        List<MPA> l = new ArrayList<>(controller.getMpasList());
        assertEquals(5, l.size());
        assertTrue(l.contains(m1));
        assertTrue(l.contains(m2));
        assertTrue(l.contains(m3));
        assertTrue(l.contains(m4));
        assertTrue(l.contains(m5));
        assertFalse(l.contains(m6));
    }

    @Test
    void shouldReturnParticularMPA() {
        MPA m1 = new MPA(1,"G");
        MPA m2 = new MPA(2,"PG");
        MPA m3 = new MPA(3,"PG-13");
        MPA m4 = new MPA(4,"R");
        MPA m5 = new MPA(5,"NC-17");
        assertEquals(m1,controller.getSingleMpa(1));
        assertEquals(m2,controller.getSingleMpa(2));
        assertEquals(m3,controller.getSingleMpa(3));
        assertEquals(m4,controller.getSingleMpa(4));
        assertEquals(m5,controller.getSingleMpa(5));
    }
}