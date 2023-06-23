package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.validation.UserValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Getter
@NoArgsConstructor
@RequestMapping("/users")

public class UserController {
    private UserRepository usersStorage;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        try {
            User u = usersStorage.getById(userId);
            log.info("Request to get user by id");
            return u;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping
    public List<User> getAll () {
        log.info("Request to get list of all users");
        return new ArrayList<>(usersStorage.getUsers().values());
    }
    @PostMapping
    public void createUser (@RequestBody User user) {
        UserValidator.validateUser(user);
        log.info("Request to create a new user");
        user.setId(usersStorage.generateId());
        usersStorage.save(user);
    }
    @PutMapping("/{userId}")
    public void renewUser (@RequestBody User user) {
        UserValidator.validateUser(user);
        if (!usersStorage.getUsers().containsKey(user.getId())) {
            throw new RuntimeException("Failed to update userdata. User not found");
        }
        log.info("Request to update user");
        usersStorage.getUsers().put(user.getId(), user);
    }
}
