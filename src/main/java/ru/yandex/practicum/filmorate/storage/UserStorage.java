package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void save(User u);

    List<User> getData();

    boolean validateId(int id);

    void update(User u);

    User getById(int id);

    void deleteById(int id);

    void deleteAll();
}