package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.database.MPADbStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MPADbStorage storage;

    @GetMapping
    List<MPARating> getAll() {
        return storage.getAllMpa();
    }

    @GetMapping("{id}")
    MPARating get(@PathVariable Long id) {
        MPARating ratingMPA = storage.getMpaById(id);
        if (ratingMPA == null) throw new NoSuchElementException();
        return ratingMPA;
    }
}