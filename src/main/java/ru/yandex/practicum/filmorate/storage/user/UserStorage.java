package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void save(User u);

    List<User> getData();

    void update(User u);

    User getById(int id);

    void deleteById(int id);

    void deleteAll();

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getFriends(int id);
}