package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        if (user.getId() != null) {
            throw new CreationException("При создании у пользователя есть id");
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        User updatedUser = userStorage.update(user);
        if (updatedUser == null) {
            throw new UpdateException("Пользователь с id: " + user.getId() + " не найден");
        }
        return updatedUser;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addNewFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new CreationException("Пользователь(и) не найден(ы)");
        }
        return userStorage.addToFriends(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) {
        return userStorage.removeFriendById(userId, friendId);
    }

    public Collection<User> getUserFriends(int userId) {
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        List<User> userFriends = userStorage.getUserFriends(userId);
        List<User> secondsUserFriends = userStorage.getUserFriends(friendId);
        List<User> result = userFriends.stream()
                .distinct()
                .filter(secondsUserFriends::contains)
                .collect(Collectors.toList());
        return result;
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

}

