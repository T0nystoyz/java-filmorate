package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistByIdException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.database.UserDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage, FriendshipStorage friends) {
        this.friendshipStorage = friends;
        this.userStorage = userStorage;
    }

    public User getById(Long id) throws UserDoesNotExistByIdException {
        return userStorage.getById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        log.info("создан пользователь {}", user.getName());
        return userStorage.createUser(user);
    }

    public User update(User user) {
        log.info("обновлен пользователь {}", user.getName());
        return userStorage.update(user);
    }

    public void addFriend(Long id, Long friendId) throws UserDoesNotExistByIdException {
        if (id < 1 || friendId < 1) {
            log.info("ошибка из-за неверного id");
            throw new UserDoesNotExistByIdException("пользователь не может существовать с таким айди");
        }
        friendshipStorage.create(Friendship
                .builder()
                .user(getById(id))
                .friend(getById(friendId))
                .build());
        /*log.info("получен запрос на добавление в друзья пользователей c id {} и {}",
                id, friendId);
        if (id < 1 || friendId < 1) {
            log.info("ошибка из-за неверного id");
            throw new UserDoesNotExistByIdException("пользователь не может существовать с таким айди");
        }

        if (!friends.containsKey(id)) {
            friends.put(id, new HashSet<>());
            log.info("это первый друг для пользователя {}", storage.getById(id).getName());
        }
        if (!friends.containsKey(friendId)) {
            friends.put(friendId, new HashSet<>());
            log.info("это первый друг для пользователя {}", storage.getById(friendId).getName());
        }
        friends.get(id).add(storage.getById(friendId));
        friends.get(friendId).add(storage.getById(id));
        log.info("{} и {} теперь друзья", storage.getById(friendId).getName(), storage.getById(id).getName());*/

    }


    public void deleteFriend(Long id, Long friendId) {
        if (id < 1 || friendId < 1) {
            log.info("ошибка из-за неверного id");
            throw new UserDoesNotExistByIdException("пользователь не может существовать с таким айди");
        }
        friendshipStorage.delete(Friendship
                .builder()
                .user(getById(id))
                .friend(getById(friendId))
                .build());
        /*log.info("получен запрос на удаление из друзей ползователей с id {} и {}", id, friendId);
        if (!friends.get(id).contains(storage.getById(friendId))) {
            log.info("у пользователя нет такого друга с id {}", friendId);
            throw new UserDoesNotExistByIdException("у пользователя " + storage.getById(id).getName()
                    + " нет друга с таким id: " + storage.getById(friendId));
        } else if (!friends.containsKey(id)) {
            throw new UserDoesNotExistByIdException("у пользователя " + storage.getById(id).getName()
                    + " нет друзей");
        } else {
            friends.get(id).remove(storage.getById(friendId));
            friends.get(friendId).remove(storage.getById(id));
            log.info("{} и {} больше не друзья", storage.getById(friendId).getName(), storage.getById(id).getName());
        }*/
    }

    public List<User> getFriendsOf(Long id) {
        if (id < 1) {
            log.info("ошибка из-за неверного id");
            throw new UserDoesNotExistByIdException("пользователь не может существовать с таким айди");
        }
        return friendshipStorage.getFriendsIds(getById(id).getId())
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
        /*log.info("получен запрос на список друзей пользователя {}", storage.getById(id).getName());
        if (!friends.containsKey(id)) {
            log.info("у пользователя пока нет друзей");
            return new HashSet<>();
        } else {
            return friends.get(id);
        }*/


    public List<User> getCommonFriends(Long id, Long otherId) throws UserDoesNotExistByIdException {
        Set<Long> common = new HashSet<>(friendshipStorage.getFriendsIds(getById(id).getId()));
        common.retainAll(friendshipStorage.getFriendsIds(otherId));

        return common
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
        /*if (storage.getById(id) == null) {
            log.info("UserDoesNotExistByIdException: пользователь c id {} не найден", id);
            throw new UserDoesNotExistByIdException("Пользователь не найден");
        }
        if (storage.getById(otherId) == null) {
            log.info("UserDoesNotExistByIdException: пользователь c id {} не найден", otherId);
            throw new UserDoesNotExistByIdException("Пользователь не найден");
        }
        Set<User> friendList = getFriendsOf(otherId);
        Set<User> userList = getFriendsOf(id);
        List<User> common = new ArrayList<>();
        for (User user : friendList) {
            if (userList.contains(user)) {
                common.add(user);
            }
        }
        return common;
    }*/
}
