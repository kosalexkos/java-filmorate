package ru.yandex.practicum.filmorate.validation;


import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;

public class FilmValidator {
    public static void validateFilm (Film f) throws ValidationException {
        if (StringUtils.isBlank(f.getName())) { throw new ValidationException("Incorrect name of the film");}
        if (f.getDescription().length() <= 200) {throw new ValidationException("Description is too long");}
        if (f.getReleaseDate() == null
                || f.getReleaseDate().isBefore(LocalDate.of(1895, 11, 28))) {
            throw new ValidationException("Incorrect release date");
        }
        if (f.getDuration() < 0) {throw new ValidationException("Incorrect duration");}
    }
}
