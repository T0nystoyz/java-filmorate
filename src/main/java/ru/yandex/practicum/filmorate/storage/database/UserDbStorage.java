package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        final String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public User getById(Long id) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (rowSet.next()) {
            User user = new User(rowSet.getString("login"),
                    rowSet.getString("name"),
                    rowSet.getString("email"),
                    Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate());
            user.setId(id);

            return user;
        } else {
            throw new UserDoesNotExistByIdException("Пользователь не найден");
        }
    }


    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(IdGenerator.nextUserId());
        }

        final String sql = "INSERT INTO users (user_id, login, name, email, birthday) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, user.getId(), user.getLogin(), user.getName(), user.getEmail(),
                user.getBirthday());

        return user;
    }

    @Override
    public User update(User user) {
        final String sql = "UPDATE USERS SET login = ?, name = ?, email = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }


    public void delete(User user) {
        final String sql = "DELETE FROM USERS WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("user_id"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate());
    }
}