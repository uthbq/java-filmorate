package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> getAll();

    User getUserById(int userId);

    User addToFriends(int userId, int friendId);

    User removeFriendById(int userId, int friendId);

    List<User> getUserFriends(int userId);

    void deleteUser(int id);
}
