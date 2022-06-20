package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("получен запрос на список пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("получен запрос на создание пользователя");
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("пользователь добавлен в базу");
        return users.get(user.getId());
    }

    @PutMapping("users")
    public User update(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("получен запрос на создание пользователя");
        if (!users.containsKey(user.getId())) {
            createUser(user);
            log.info("такого пользователя нет. новый пользователь добавлен в базу");
        } else {
            users.put(user.getId(), user);
            log.info("пользователь обновлен");
        }
        return users.get(user.getId());
    }
}
