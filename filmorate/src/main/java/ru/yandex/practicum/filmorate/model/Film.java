package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;
import ru.yandex.practicum.filmorate.validation.AfterDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
public class Film {
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

    public Film(@NotNull String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}