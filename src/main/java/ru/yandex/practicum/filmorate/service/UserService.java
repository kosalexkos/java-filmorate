package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (!(storage.validateId(user.getId()))) {
            log.info("Failed to find user to update");
            throw new DataNotFoundException("Failed to update userdata. User not found");
        }
        Set<Integer> friends = storage.getById(user.getId()).getFriends();
        user.setFriends(friends);
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
        User u1 = storage.getById(id);
        User u2 = storage.getById(friendId);
        u1.getFriends().add(friendId);
        u2.getFriends().add(id);
        log.info("User with id " + friendId + " was added to friends list of user with id " + id);
    }

    public void deleteFriend(int id, int friendId) {
        User u1 = storage.getById(id);
        User u2 = storage.getById(friendId);
        if (!u1.getFriends().contains(friendId)) {
            throw new DataNotFoundException("User with id " + id + " doesn't have friend with id " + friendId);
        }
        if (!u2.getFriends().contains(friendId)) {
            throw new DataNotFoundException("User with id " + friendId + " doesn't have friend with id " + id);
        }
        u1.getFriends().remove(friendId);
        u2.getFriends().remove(id);
        log.info("User with id " + friendId + " was removed from friends list of user with id " + id);
    }

    public List<User> getFriendsList(int id) {
        log.info("Processing request to get friends list of user with id " + id);
        User u = storage.getById(id);
        return u.getFriends().stream()
                .map(storage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int friendId) {
        log.info("Processing request to get common friends with user with id " + friendId);
        List<User> list = getFriendsList(id);
        list.retainAll(getFriendsList(friendId));
        return list;
    }
}