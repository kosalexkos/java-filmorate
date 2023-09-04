package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserStorage storage;

    public User createUser(User user) {
        log.info("Processing user creation");
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        storage.save(user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Processing user update");
        storage.update(user);
        return user;
    }

    public List<User> getAll() {
        log.info("Processing request to get all users");
        return storage.getData();
    }

    public User getUserById(int id) {
        log.info("Processing request to get user by id");
        return storage.getById(id);
    }

    public void deleteUserById(int id) {
        log.info("Processing request to delete user");
        storage.deleteById(id);
    }

    public void deleteAllUsers() {
        log.info("Processing request to delete all users");
        storage.deleteAll();
    }

    public void addFriend(int id, int friendId) {
        log.info("Processing request to add user");
        storage.addFriend(id, friendId);
        log.info("User with id " + friendId + " was added to friends list of user with id " + id);
    }

    public void deleteFriend(int id, int friendId) {
        log.info("Processing deleting of friend");
        storage.deleteFriend(id, friendId);
    }

    public List<User> getFriendsList(int id) {
        log.info("Processing request to get friends list of user with id " + id);
        User u = storage.getById(id);
        return new ArrayList<>(u.getFriends());
    }

    public List<User> getCommonFriends(int id, int friendId) {
        log.info("Processing request to get common friends with user with id " + friendId);
        List<User> list = storage.getFriends(id);
        list.retainAll(storage.getFriends(friendId));
        return list;
    }
}