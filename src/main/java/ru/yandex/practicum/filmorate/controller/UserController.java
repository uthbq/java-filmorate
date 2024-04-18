package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriendsList(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addFriendship(id, friendId);
    }

    @DeleteMapping("/{userId1}/friends/{friendId}")
    public User removeFriendship(@PathVariable Integer userId1, @PathVariable Integer friendId) {
        return userService.removeFriend(userId1, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable Integer userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> findCommonFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return userService.findCommonFriends(userId, friendId);
    }
}

