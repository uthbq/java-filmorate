package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей: {}", users.size());
        return List.copyOf(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        isUserValid(user);
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с таким ID уже существует");
            throw new ValidationException("Пользователь с таким ID уже существует");
        }

        user.setId(generatorId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        isUserValid(user);

        if (!users.containsKey(user.getId())) {
            log.warn("Невозможно обновить пользователя");
            throw new ValidationException("Невозможно обновить пользователя");
        }

        users.put(user.getId(), user);

        log.info("Пользователь c id {} обновлён", user.getId());
        return user;
    }

    private void isUserValid(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            setDefaultName(user);
        }
    }

    private void setDefaultName(User user) {
        user.setName(user.getLogin());
    }

    private int generatorId() {
        return userId++;
    }

}

