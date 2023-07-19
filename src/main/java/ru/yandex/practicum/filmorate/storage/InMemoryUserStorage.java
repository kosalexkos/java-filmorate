package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id;
    private final Map<Integer, User> users;

    public InMemoryUserStorage() {
        this.id = 0;
        this.users = new HashMap<>();
    }

    @Override
    public void save(User u) {
        u.setId(generateId());
        this.users.put(u.getId(), u);
        log.info("User was successfully saved");
    }

    private int generateId() {
        log.info("An id for a user was successfully generated");
        return ++id;
    }

    @Override
    public List<User> getData() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean validateId(int id) {
        return users.containsKey(id);
    }

    @Override
    public void update(User u) {
        users.put(u.getId(), u);
        log.info("User was successfully updated");
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            log.info("User not found");
            throw new DataNotFoundException("Wrong user id");
        }
        return users.get(id);
    }

    @Override
    public void deleteById(int id) {
        if (!users.containsKey(id)) {
            log.info("User not found");
            throw new DataNotFoundException("Wrong user id");
        }
        users.remove(id);
        log.info("User was successfully deleted");
    }

    @Override
    public void deleteAll() {
        users.clear();
        log.info("All users were successfully removed");
    }
}