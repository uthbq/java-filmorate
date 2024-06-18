package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class})
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void shouldCreate() {
        User user = new User();
        user.setName("george");
        user.setEmail("george@email.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setLogin("george");

        userStorage.create(user);
        List<User> users = userStorage.getAll();

        assertEquals(1, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "george");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "george@email.ru");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "george");
    }

    @Test
    @Sql(scripts = {"/userDbStorageTest.sql"})
    void shouldUpdate() {
        User user = new User();
        user.setId(1);
        user.setName("updateName");
        user.setEmail("updateTest@email.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setLogin("update_login");

        userStorage.update(user);
        List<User> users = userStorage.getAll();
        assertEquals(users.size(),2);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "updateName");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "updateTest@email.ru");
        assertThat(users.get(1)).hasFieldOrProperty("birthday");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "update_login");
    }

    @Test
    @Sql(scripts = {"/userDbStorageTest.sql"})
    void shouldGetAll() {
        List<User> users = userStorage.getAll();

        assertEquals(2, users.size());

        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 0);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "george");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "george@email.ru");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "george");

        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "george2");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "george2@email.ru");
        assertThat(users.get(1)).hasFieldOrProperty("birthday");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "george2");
    }

    @Test
    @Sql(scripts = {"/userDbStorageTest.sql"})
    void shouldGetUserById() {
        User user = userStorage.getUserById(1);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user).hasFieldOrPropertyWithValue("name", "george2");
        assertThat(user).hasFieldOrPropertyWithValue("email", "george2@email.ru");
        assertThat(user).hasFieldOrProperty("birthday");
        assertThat(user).hasFieldOrPropertyWithValue("login", "george2");
    }

    @Test
    @Sql(scripts = {"/userDbStorageTest.sql"})
    void shouldAddToFriendsAndGetUserFriends() {
        userStorage.addToFriends(0, 1);

        List<User> friendsList = userStorage.getUserFriends(0);
        assertEquals(1, friendsList.size());
        assertThat(friendsList.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(friendsList.get(0)).hasFieldOrPropertyWithValue("name", "george2");
        assertThat(friendsList.get(0)).hasFieldOrPropertyWithValue("email", "george2@email.ru");
        assertThat(friendsList.get(0)).hasFieldOrProperty("birthday");
        assertThat(friendsList.get(0)).hasFieldOrPropertyWithValue("login", "george2");
    }

    @Test
    @Sql(scripts = {"/userDbStorageTest.sql"})
    void shouldRemoveFriendById() {
        userStorage.addToFriends(0, 1);

        List<User> friendsList = userStorage.getUserFriends(0);
        assertEquals(1, friendsList.size());

        userStorage.removeFriendById(0, 1);

        List<User> friendsListAfterDelete = userStorage.getUserFriends(0);
        assertEquals(0, friendsListAfterDelete.size());

    }
}



