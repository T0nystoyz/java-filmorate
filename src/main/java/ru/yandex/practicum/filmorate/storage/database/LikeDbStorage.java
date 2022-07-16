package ru.yandex.practicum.filmorate.storage.database;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Data
public class LikeDbStorage implements LikeStorage{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public Collection<Film> getPopularFilms(int limit) {
        final String sql = "SELECT * FROM films f LEFT JOIN (SELECT film_id, COUNT(*) likes_count FROM likes"
                + " GROUP BY film_id) l ON f.film_id = l.film_id LEFT JOIN mpa ON f.mpa_id = mpa.mpa_id"
                + " ORDER BY l.likes_count DESC LIMIT ?";

        final Map<Long, Set<Genre>> filmsGenres = getAllFilmsGenres();

        return jdbcTemplate.query(sql, (rs, numRow) -> {
            final Long filmId = rs.getLong("film_id");
            return mapRowToFilm(rs, filmsGenres.get(filmId));
        }, limit);
    }

    @Override
    public void saveLike(Like like) {
        final String sql = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, like.getUser().getId(), like.getFilm().getId());
    }

    @Override
    public void deleteLike(Like like) {
        final String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, like.getUser().getId(), like.getFilm().getId());
    }

    private Map<Long, Set<Genre>> getAllFilmsGenres() {
        final String sql = "SELECT * FROM film_genres INNER JOIN genres ON genres.genre_id = film_genres.genre_id";

        final Map<Long, Set<Genre>> filmsGenres = new HashMap<>();

        jdbcTemplate.query(sql, (RowCallbackHandler) rs -> {
            final Long filmId = rs.getLong("film_id");
            filmsGenres.getOrDefault(filmId, new HashSet<>()).add(Genre.builder().id(rs.getInt("genre_id"))
                    .title(rs.getString("title")).build());
        });

        return filmsGenres;
    }

    private Film mapRowToFilm(ResultSet rs, Set<Genre> genres) throws SQLException {
        return new Film(rs.getLong("film_id"), rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new MPARating(rs.getInt("mpa_id"),
                        rs.getString("title")),
                genres != null && genres.isEmpty() ? null : genres);
    }
}
