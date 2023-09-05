package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    @Email
    String email;
    @Pattern(regexp = "^[^\\s]+$", message = "String should not contain spaces")
    @NotBlank
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;
    Set<User> friends;

    public User(@NotBlank String email, @NotBlank String login, String nickname, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = nickname;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}