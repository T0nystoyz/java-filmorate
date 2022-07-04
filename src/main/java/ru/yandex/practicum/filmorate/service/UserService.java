package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final static Map<Long, Set<User>> friends = new HashMap<>();
    private final UserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public User getById(Long id) throws UserDoesNotExistByIdException {
        return storage.getById(id);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public String addFriend(Long id, Long friendId) throws UserDoesNotExistByIdException {
        if (id < 1 || friendId < 1) {
            throw new UserDoesNotExistByIdException("пользователь не может существовать с таким айди");
        }
        log.info("получен запрос на добавление в друзья");
        if (!friends.containsKey(id)) {
            friends.put(id, new HashSet<>());
        }
        if (!friends.containsKey(friendId)) {
            friends.put(friendId, new HashSet<>());
        } else {

            friends.get(id).add(storage.getById(friendId));
            friends.get(friendId).add(storage.getById(id));
            return "пользователи доавлены в друзья";
        }
        return "ok";
    }


    public void deleteFriend(Long id, Long friendId) {
        log.info("получен запрос удаление из друзей");
        if (!friends.get(id).contains(storage.getById(friendId))) {
        } else if (!friends.containsKey(id)) {
        } else {
            friends.get(id).remove(storage.getById(friendId));
            friends.get(friendId).remove(storage.getById(id));
        }
    }

    public List<User> getFriendsOf(Long id) {
        log.info("получен запрос на список друзей");
        if (!friends.containsKey(id)) {
            return new ArrayList<>();
        }
        Set<User> friendsOf = friends.get(id);
        return new ArrayList<>(friendsOf);
    }

    public List<User> getCommonFriends(Long id, Long otherId) throws UserDoesNotExistByIdException {

        if (storage.getById(id) == null) {
            log.info("UserDoesNotExistByIdException: пользователь c id = \"{}\" не найден", id);
            throw new UserDoesNotExistByIdException("Пользователь не найден");
        }
        if (storage.getById(otherId) == null) {
            log.info("UserDoesNotExistByIdException: пользователь c id = \"{}\" не найден", otherId);
            throw new UserDoesNotExistByIdException("Пользователь не найден");
        }
        List<User> friendList = getFriendsOf(otherId);
        List<User> userList = getFriendsOf(id);
        List<User> common = new ArrayList<>();
        for (User user : friendList) {
            if (userList.contains(user)) {
                common.add(user);
            }
        }
        return common;
    }
}
