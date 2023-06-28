package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class TestUserController {
    Validator validator;
    private UserController userController;
    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userController = new UserController();
    }
    @Test
    void shouldCreateUser() {
        User user = new User("aaa@gmail.com", "Nagibator3000", "vasya",
                LocalDate.of(1996,10,15));
        userController.createUser(user);
        assertEquals(0, user.getId());
    }
    @Test
    void shouldUpdateUser() {
        User user1 = new User("example1@gmail.com", "ddd", "sssss",
                LocalDate.of(2000,11,6));
        userController.createUser(user1);
        User user2 = new User("aaa@gmail.com", "aaaa",
                "eeeee", LocalDate.of(1595,11,28));
        user2.setId(user1.getId());
        userController.updateUser(user2);
        User user3 = userController.getUsersStorage().getUsers().get(0);
        assertEquals(1, userController.getUsersStorage().getUsers().size());
        assertEquals(user3, userController.getUsersStorage().getUsers().get(0));
    }
    @Test
    void shouldSetNameIfEmpty() {
        User user = new User("ex@gmail.com", "ddd", "",
                LocalDate.of(1986,4,26));
        userController.createUser(user);
        assertEquals(user.getLogin(), user.getNickname());
    }
    @Test
    void createWrongEmailUser() {
        User user = new User("0000",
                "rrrrr", "ggggg", LocalDate.of(2015,7,22));
        userController.createUser(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
    @Test
    void createWrongLoginUser() {
        User user = new User("ex@gmail.com",
                "00  000", "ffff", LocalDate.of(1000,12,4));
        userController.createUser(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
    @Test
    void createWrongBirthdayUser() {
        User user = new User("ex@gmail.com",
                "00000", "ffff", LocalDate.of(10000,12,4));
        userController.createUser(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
