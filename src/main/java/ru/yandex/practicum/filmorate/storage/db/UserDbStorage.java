package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
        Integer newId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(newId);
        log.info("Пользователю " + user.getLogin() + " присвоен id: " + newId);
        return user;

    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET EMAIL= ?, NAME= ?, LOGIN= ? , BIRTHDAY= ? WHERE ID= ?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(int userId) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public User addToFriends(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(friendId);
    }

    @Override
    public User removeFriendById(int userId, int friendId) {
        if (getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(friendId);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        if (getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<User> result = new ArrayList<>();
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        jdbcTemplate.query(sqlQuery, rs -> {
            result.add(getUserById(rs.getInt("friend_id")));
        }, userId);
        return result;
    }

    @Override
    public void deleteUser(int id) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        int update = jdbcTemplate.update(sqlQuery,id);
        if (update == 0) {
            throw new UpdateException("Пользователь с id =  " + id + " не найден");
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }

}
