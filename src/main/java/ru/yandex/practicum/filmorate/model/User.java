package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @Email
    private String email;
    @Pattern(regexp = "^[^\\s]+$", message = "String should not contain spaces")
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends;

    public User(@NotBlank String email, @NotBlank String login, String nickname, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = nickname;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}