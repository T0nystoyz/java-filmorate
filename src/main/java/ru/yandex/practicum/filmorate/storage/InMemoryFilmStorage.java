package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import javax.validation.Valid;
import java.util.*;

@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        if(film.getId() == null){
            film.setId(IdGenerator.nextFilmId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Long id){
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }


}

