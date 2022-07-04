package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final static Map<Long, User> users = new HashMap<>();


    @Override
    public List<User> getUsers() {
        log.info("получен запрос на список пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long id){
        return users.get(id);
    }
    @Override
    public User createUser(User user) {
        log.info("получен запрос на создание пользователя");
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(IdGenerator.nextUserId());
        }
        users.put(user.getId(), user);
        log.info("пользователь добавлен в базу");
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        log.info("получен запрос на обновление пользователя");
        if (!users.containsKey(user.getId()) && user.getId() >= 1) {
            users.put(user.getId(), user);
        } else {
            users.put(user.getId(), user);
            log.info("пользователь обновлен");
        }
        return users.get(user.getId());
    }
}
