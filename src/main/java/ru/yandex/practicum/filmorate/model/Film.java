package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;
import ru.yandex.practicum.filmorate.validation.AfterDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotBlank
    @NonNull
    private String name;
    @Length(max = 200, message = "Description should not be longer than 200 symbols")
    private String description;
    @AfterDate
    @PastOrPresent
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Film duration cannot be less than zero")
    private int duration;
    @EqualsAndHashCode.Exclude
    private Set<User> likes;
    @EqualsAndHashCode.Exclude
    @NotNull
    private MPA mpa;
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres;

    public Film(@NotNull String name, String description, LocalDate releaseDate, int duration, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genres = new HashSet<>();
        this.mpa = mpa;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.getId());
        values.put("genres", genres);
        return values;
    }
}