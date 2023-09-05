package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @EqualsAndHashCode.Exclude
    int id;
    @NotBlank
    @NonNull
    String name;
    @Length(max = 200, message = "Description should not be longer than 200 symbols")
    String description;
    @AfterDate
    @PastOrPresent
    LocalDate releaseDate;
    @PositiveOrZero(message = "Film duration cannot be less than zero")
    int duration;
    @EqualsAndHashCode.Exclude
    Set<User> likes;
    @JsonProperty("mpa")
    @NotNull
    MPA mpa;
    @JsonProperty("genres")
    Set<Genre> genres;

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