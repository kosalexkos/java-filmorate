package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserRepository {
    @Getter
    private int id;
    @Getter
    private final Map<Integer, User> users;

    public UserRepository() {
        this.id = 0;
        this.users = new HashMap<>();
    }
    public void save(User u) {
        this.users.put(u.getId(), u);
        log.info("User was successfully saved");
    }
    public int generateId() {
        log.info("Generated an id for a user");
        return this.id++;
    }
}
