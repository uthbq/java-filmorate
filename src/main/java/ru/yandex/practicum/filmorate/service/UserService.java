package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.NotFoundException;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Integer id) {
        return userStorage.getById(id);
    }

    public void deleteById(Integer id) {
        userStorage.deleteById(id);
    }

    public User addFriendship(Integer id, Integer friendId) {
        if (!userStorage.isUserExist(id) && !userStorage.isUserExist(friendId)) {
            throw new IllegalArgumentException("Некорректные id пользователя или друга.");
        }
        User user = getById(id);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        return user;
    }

    public User removeFriend(Integer userId, Integer friendId) {
        if (!userStorage.isUserExist(userId) && !userStorage.isUserExist(friendId)) {
            throw new IllegalArgumentException("Некорректные id пользователя или друга.");
        }
        getById(userId).getFriends().remove(friendId);
        getById(friendId).getFriends().remove(userId);
        return getById(userId);
    }

    public List<User> getUserFriends(Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new NotFoundException("Некорректный id пользователя.");
        }
        return getById(userId).getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer userId, Integer friendId) {
        if (!userStorage.isUserExist(userId) && !userStorage.isUserExist(friendId)) {
            throw new IllegalArgumentException("Некорректные id пользователя или друга.");
        }
        Set<Integer> mutualFriends = new HashSet<>(getById(userId).getFriends());
        mutualFriends.retainAll(getById(friendId).getFriends());
        return mutualFriends.stream().map(this::getById).collect(Collectors.toList());
    }
}