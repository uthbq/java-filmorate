package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;


import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Set<User> users = new HashSet<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public User updateUser(User user) {
        if (!users.contains(user)) {
            throw new NotFoundException("Такого пользователя не существует.");
        }
        users.add(user);
        log.info("Пользователь {} обновлен.", user);
        return user;
    }

    @Override
    public User createUser(User user) {
        if (users.contains(user)) {
            throw new ValidationException("Такой пользователь уже существует.");
        }
        if (users.stream()
                .filter(existingUser -> !existingUser.equals(user))
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("Пользователь с таким email уже существует.");
        }
        user.setId(generateId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Пользователь {} добавлен.", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>(users);
        return allUsers;
    }

    @Override
    public boolean isUserExist(Integer id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(Integer userId) {
        User userToRemove = getById(userId);
        users.remove(userToRemove);
    }

    @Override
    public User getById(int userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id."));
    }

}