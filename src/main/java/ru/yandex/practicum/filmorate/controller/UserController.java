package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;

    @GetMapping
    public List<User> getAll() {
        log.info("Request to get list of all users");
        return service.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Request to create a new user");
        service.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Request to update user");
        service.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Request to get user by id");
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        log.info("Request to delete user by id");
        service.deleteUserById(id);
        return ResponseEntity.ok().body("User with id " + id + " was successfully removed");
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAll() {
        log.info("Request to delete all users");
        service.deleteAllUsers();
        return ResponseEntity.ok().body("All users were successfully removed");
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Request from user with id {} to add user with id {} to friends list", id, friendId);
        service.addFriend(id, friendId);
        return ResponseEntity.ok().body("User with id " + friendId + " was added to your friends list");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Request from user with id {} to delete user with id {} from friends list", id, friendId);
        service.deleteFriend(id, friendId);
        return ResponseEntity.ok().body("User with id " + friendId + " was removed from your friends list");
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable int id) {
        log.info("Request to get friends list of the user with id " + id);
        List<User> f = service.getFriendsList(id);
        return ResponseEntity.ok(f);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Request to show common friends with user with id " + friendId);
        List<User> list = service.getCommonFriends(id, friendId);
        return ResponseEntity.ok(list);
    }
}