package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;


public interface UserStorage {

    User updateUser(User user);

    User createUser(User user);

    Set<User> getAll();

    boolean isUserExist(Integer id);

    void deleteById(Integer userId);

    User getById(int userId);
}
