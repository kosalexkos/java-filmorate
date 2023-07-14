package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserRepository {
    private int id;
    private final Map<Integer, User> users;

    public UserRepository() {
        this.id = 0;
        this.users = new HashMap<>();
    }

    public void save(User u) {
        u.setId(generateId());
        this.users.put(u.getId(), u);
        log.info("User was successfully saved");
    }

    private int generateId() {
        log.info("An id for a user was successfully generated");
        return ++id;
    }

    public List<User> getData() {
        return new ArrayList<>(users.values());
    }

    public boolean validateId(int id) {
        return users.containsKey(id);
    }

    public void update(User u) {
        users.put(u.getId(), u);
        log.info("User was successfully updated");
    }
}