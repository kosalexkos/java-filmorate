package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email
    @NonNull
    private String email;
    @Pattern(regexp = "^[^\\s]+$", message = "String should not contain spaces")
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}