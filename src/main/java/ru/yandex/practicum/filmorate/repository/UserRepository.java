package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserRepository {
    private int id;
    @Getter
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
}