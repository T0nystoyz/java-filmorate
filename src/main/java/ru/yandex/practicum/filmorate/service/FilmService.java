package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
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
        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }
        likes.get(filmId).add(userId);
    }

    public void deleteLike(Long filmId, Long userId) throws UserDoesNotExistByIdException {
        if (!likes.containsKey(filmId)) {
            System.out.println("у фильма нет лайков!");
        }
        try {
            likes.get(filmId).remove(userId);
        } catch (NullPointerException e) {
            throw new UserDoesNotExistByIdException("пользователь не ставил лайк к фильму");
        }
    }


    public Collection<Film> getPopularFilms(Integer limit) {
        final Comparator<Film> comparator = Comparator
                .comparingInt(x -> likes.getOrDefault(x.getId(), new HashSet<>()).size());
        return storage.getFilms().values().stream().sorted(comparator.reversed())
                .limit(limit).collect(Collectors.toList());
    }

}
