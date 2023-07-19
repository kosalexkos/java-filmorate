package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void save(Film f);

    List<Film> getData();

    boolean validateId(int id);

    void update(Film f);

    Film getById(int id);

    void deleteById(int id);

    void deleteAll();
}