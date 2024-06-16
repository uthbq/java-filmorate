package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Такого пользователя не существует.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} обновлен.", user);
        return user;
    }

    @Override
    public User createUser(User user) {
        for (User existingUser : users.values()) {
            if (existingUser.equals(user)) {
                throw new ValidationException("Такой пользователь уже существует.");
            }
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }

        user.setId(generateId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен.", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }


    @Override
    public void deleteById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с таким id.");
        }
        users.remove(userId);
    }

    @Override
    public User getById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Нет пользователя с таким id.");
        }
        return user;
    }
}