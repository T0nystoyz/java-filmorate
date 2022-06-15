package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @PutMapping("films")
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            createFilm(film);
        } else {
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }
}
