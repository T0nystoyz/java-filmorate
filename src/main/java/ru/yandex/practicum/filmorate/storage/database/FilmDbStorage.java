package ru.yandex.practicum.filmorate.storage.database;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Data
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        if (film.getId() == null) {
            film.setId(IdGenerator.nextFilmId());
        }

        final String sql = "INSERT INTO films (film_id, name, description, release_date, duration, mpa_id)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());

        final Set<Genre> filmGenres = film.getGenres();

        if (filmGenres != null) {
            final String genreSaveSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            Film finalFilm = film;
            filmGenres.forEach(x -> jdbcTemplate.update(genreSaveSql, finalFilm.getId(), x.getId()));
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?"
                + " WHERE film_id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        final String deleteGenres = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());

        final Set<Genre> filmGenres = film.getGenres();

        if (filmGenres != null) {
            final String genreSaveSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(genreSaveSql, film.getId(), x.getId()));
        }

        return film;
    }

    @Override
    public Film getById(Long id) {
        final String sql = "SELECT * FROM FILMS LEFT JOIN MPA ON FILMS.mpa_id = MPA.mpa_id WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, numRow) -> mapRowToFilm(rs, getFilmGenresById(id)), id);
        return films.size() > 0 ? films.get(0) : null;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();

        List<Long> filmsId =
                jdbcTemplate.query("SELECT film_id FROM FILMS", ((rs, rowNum) -> rs.getLong("film_id")));

        for (Long filmId : filmsId) {
            films.add(getById(filmId));
        }

        return films;
    }

    private Set<Genre> getFilmGenresById(Long id) {
        final String sql = "SELECT * FROM FILM_GENRES INNER JOIN GENRES ON GENRES.genre_id = FILM_GENRES.genre_id"
                + " WHERE film_id = ?";

        return new HashSet<>(jdbcTemplate.query(sql, (rs, getNum) -> Genre.builder().id(rs.getInt("genre_id"))
                .title(rs.getString("title")).build(), id));
    }

    public void deleteFilm(Film film) {
        final String sql = "DELETE FROM FILMS WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private Film mapRowToFilm(ResultSet rs, Set<Genre> genres) throws SQLException {
        return new Film(rs.getLong("film_id"), rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new MPARating(rs.getInt("mpa_id"), rs.getString("title")),
                genres != null && genres.isEmpty() ? null : genres);
    }
}
