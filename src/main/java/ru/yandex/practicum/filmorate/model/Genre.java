package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Genre {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        String res = "id: " + id + ", name: " + name;
        return res;
    }
}