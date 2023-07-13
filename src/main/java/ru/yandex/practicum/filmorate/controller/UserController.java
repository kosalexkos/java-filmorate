package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
    private final UserRepository usersStorage;

    @Autowired
    public UserController(UserRepository usersStorage) {
        this.usersStorage = usersStorage;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Request to get list of all users");
        return new ArrayList<>(usersStorage.getUsers().values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Request to create a new user");
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        this.usersStorage.save(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!(usersStorage.getUsers().containsKey(user.getId()))) {
            log.info("Failed to find user to update");
            throw new ValidationException("Failed to update userdata. User not found");
        }
        log.info("Request to update user");
        usersStorage.getUsers().put(user.getId(), user);
        return user;
    }
}