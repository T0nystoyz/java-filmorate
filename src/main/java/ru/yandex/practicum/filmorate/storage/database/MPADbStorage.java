package ru.yandex.practicum.filmorate.storage.database;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@Data
public class MPADbStorage implements MPAStorage{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<MPARating> getAllMpa() {
        final String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs));
    }

    @Override
    public MPARating getMpaById(Long id) {
        final String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        final List<MPARating> mpaRating = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs), id);
        return mpaRating.size() > 0 ? mpaRating.get(0) : null;
    }

    private MPARating mapRowToMpa(ResultSet rs) throws SQLException {
        return MPARating.builder()
                .id(rs.getInt("mpa_id"))
                .title(rs.getString("title"))
                .build();
    }
}
