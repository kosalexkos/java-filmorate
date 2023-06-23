package ru.yandex.practicum.filmorate.validation;

import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    public static void validateUser(User u) {
        if (StringUtils.isBlank(u.getEmail()) || !u.getEmail().contains("@")) {
            throw new ValidationException("Incorrect email");
        }
        if (StringUtils.isBlank(u.getLogin())) {
            throw new ValidationException("Incorrect login");
        }
        if (StringUtils.isBlank(u.getNickname())) {
            u.setNickname(u.getLogin());
        }
        if (u.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Incorrect birthdate");
        }
    }
}


