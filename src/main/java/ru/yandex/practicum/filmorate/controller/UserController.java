package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на создание нового пользователя");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя с id " + user.getId());
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Запрос на получение списка всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Запрос на получение пользователя с id " + id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("Пользователь с id: " + id + " отправил запрос на добавление в друзья пользователю с id: " + friendId);
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id: " + id + " удалил из друзей пользователя с id: " + friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        log.info("Запрос на получение списка друзей пользователя с id: " + id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable("otherId") int friendId) {
        log.info("Запрос на получение списка общих друзей пользователя с id: " + id + " с пользователем с id: " +
                friendId);
        return userService.getCommonFriends(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") int id) {
        log.info("Запрос на удаление пользователя с id {}", id);
        userService.deleteUser(id);
    }

}