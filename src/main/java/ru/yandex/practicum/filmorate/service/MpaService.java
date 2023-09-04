package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpas;

    @Autowired
    public MpaService(MpaStorage mpas) {
        this.mpas = mpas;
    }

    public MPA getMpa(Integer id) {
        return mpas.getSingleMpa(id);
    }

    public List<MPA> getMpasList() {
        return mpas.getAllMpas();
    }
}