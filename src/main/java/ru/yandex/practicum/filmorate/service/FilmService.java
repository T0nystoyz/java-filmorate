package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage storage;
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public void createFilm(Film film) {
        storage.createFilm(film);
    }

    public Film update(Film film) {
        storage.update(film);
        return film;
    }

    public Film getById(Long id) {
        return storage.getById(id);
    }

    public void saveLike(Long filmId, Long userId) {
        log.info("попытка поставить лайк фильму");
        if (!likes.containsKey(filmId)) {
            log.info("у фильма появился первый лайк!");
            likes.put(filmId, new HashSet<>());
        }
        likes.get(filmId).add(userId);
        log.info("пользователь с id " + userId + " поставил лайк фильму " + storage.getById(filmId).getName());
    }

    public void deleteLike(Long filmId, Long userId) throws UserDoesNotExistByIdException {
        if (!likes.containsKey(filmId)) {
            log.info("у фильма " + storage.getById(filmId).getName() + " нет лайков!");
            throw new UserDoesNotExistByIdException("у фильма " + storage.getById(filmId).getName() + " нет лайков!");
        }
        likes.get(filmId).remove(userId);
    }


    public Collection<Film> getPopularFilms(Integer limit) {
        log.info("получен запрос на популярные фильмы");
        final Comparator<Film> comparator = Comparator
                .comparingInt(x -> likes.getOrDefault(x.getId(), new HashSet<>()).size());
        return storage.getFilms().values().stream().sorted(comparator.reversed())
                .limit(limit).collect(Collectors.toList());
    }

}
