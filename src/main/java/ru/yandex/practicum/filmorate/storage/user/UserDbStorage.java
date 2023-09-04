package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User u) throws ResponseStatusException {
        if (containsUser(u)) {
            log.info("This user has already been created");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Wrong data. This user has already been created");
        }
        Integer userId = addToDb(u);
        u.setId(userId);
        if (u.getFriends().size() > 0) {
            String query = "INSERT INTO Friends (user_id, friend_id) VALUES (?, ?)";
            u.getFriends().stream().map(friend -> jdbcTemplate.update(query, userId, friend.getId()));
        }
        log.info("User was successfully saved");
    }

    @Override
    public List<User> getData() {
        String sqlQuery = "SELECT * FROM Users";
        return jdbcTemplate.query(sqlQuery, this::getUserFromDb);
    }

    @Override
    public void update(User u) throws ResponseStatusException {
        if (!containsUser(u.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User for update not found");
        }
        u.getFriends().addAll(getById(u.getId()).getFriends());
        String query = "UPDATE Users " +
                "SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(query, u.getEmail(), u.getLogin(), u.getName(),
                u.getBirthday(), u.getId());
        if (u.getFriends().size() > 0) {
            query = "INSERT INTO Friends (user_id, friend_id)" +
                    "VALUES (?, ?)";
            for (User us : u.getFriends()) {
                jdbcTemplate.update(query, u.getId(), us.getId());
            }
        }
        log.info("User was successfully updated");
    }

    @Override
    public User getById(int id) throws ResponseStatusException {
        if (!containsUser(id)) {
            log.info("User not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with the id = " + id + " not found");
        }
        String query = "SELECT * FROM Users WHERE user_id = ?";
        User u = jdbcTemplate.queryForObject(query, this::getUserFromDb, id);
        return u;
    }

    @Override
    public void deleteById(int id) throws ResponseStatusException {
        if (!containsUser(id)) {
            log.info("User not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with the id = " + id + " not found");
        }
        String query = "DELETE FROM Users WHERE user_id = ?";
        jdbcTemplate.update(query, id);
        log.info("User was successfully deleted");
    }

    @Override
    public void deleteAll() {
        String deleteLikesSql = "DELETE FROM Likes";
        jdbcTemplate.update(deleteLikesSql);
        String deleteFriendsSql = "DELETE FROM Friends";
        jdbcTemplate.update(deleteFriendsSql);
        String deleteUsersSql = "DELETE FROM Users";
        jdbcTemplate.update(deleteUsersSql);
        log.info("All users were successfully removed");
    }

    @Override
    public void addFriend(int id, int friendId) throws ResponseStatusException {
        if (!containsUser(id)) {
            log.info("Wrong user id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with the id= "
                    + id + " doesn't exist");
        }
        if (!containsUser(friendId)) {
            log.info("Wrong friend id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User with the id= "
                    + id + " doesn't exist and cannot be added to friends" );
        }
        String query = "INSERT INTO Friends (user_id, friend_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(query, id, friendId);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with the id= " + friendId + " is already your friend");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User is not allowed to add oneself to friends");
        }
        log.info("User with the id = " + id + " successfully added friend with id = " + friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) throws ResponseStatusException {
        if (!containsUser(id)) {
            log.info("Wrong user id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with the id= "
                    + id + " doesn't exist");
        }
        if (!containsUser(friendId)) {
            log.info("Wrong friend id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with the id= " + id + " doesn't exist and  cannot be deleted from friends" );
        }
        String query = "DELETE FROM Friends WHERE user_id = ? AND friend_id = ?";
        try {
            jdbcTemplate.update(query, id, friendId);
            log.info("User with id " + friendId + " was removed from friends list of user with id " + id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with the id = " + id + " doesn't have a friend with id = " + friendId);
        }
    }

    @Override
    public List<User> getFriends(int id) {
        if (!containsUser(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User whose friends list you want to get doesn't exist");
        }
        String query = "SELECT * FROM Users " +
                "WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(query, this::getFriendFromDb, id);
    }

    private boolean containsUser(User user) {
        String query = "SELECT * FROM Users WHERE email = ? AND login = ? " +
                "AND name = ? AND birthday = ?";
        try {
            jdbcTemplate.queryForObject(query, this::getUserFromDb, user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean containsUser(Integer userId) {
        String sqlQuery = "SELECT * FROM Users WHERE user_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::getUserFromDb, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private User getUserFromDb(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
                user.setId(resultSet.getInt("user_id"));
        String query = "SELECT * FROM Users " +
                "WHERE user_id IN (SELECT friend_id  FROM Friends WHERE user_id = ?)";
        user.getFriends().addAll(jdbcTemplate.query(query, this::getFriendFromDb, user.getId()));
        return user;
    }

    private User getFriendFromDb(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
        user.setId(resultSet.getInt("user_id"));
        return user;
    }

    private Integer addToDb (User u) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Users")
                .usingGeneratedKeyColumns("user_id");
        return simpleJdbcInsert.executeAndReturnKey(u.toMap()).intValue();
    }
}