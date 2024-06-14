package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class, UserDbStorage.class})
public class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    void shouldCreate() {
        FilmMpa filmMpa = filmStorage.getMpaById(3);
        assertEquals(filmMpa.getId(), 3);
        assertEquals(filmMpa.getName(), "PG-13");

        Film film = new Film();
        film.setName("FilmPracticum");
        film.setDescription("DescriptionPracticum");
        film.setDuration(222);
        film.setReleaseDate(LocalDate.of(2007, 1, 1));
        film.setMpa(filmMpa);

        filmStorage.create(film);
        List<Film> films = filmStorage.getAll();

        assertEquals(1, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "FilmPracticum");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "DescriptionPracticum");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 222);
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrProperty("mpa");


    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql"})
    void shouldUpdate() {
        FilmMpa filmMpa = filmStorage.getMpaById(3);
        assertEquals(filmMpa.getId(), 3);
        assertEquals(filmMpa.getName(), "PG-13");

        Film film = new Film();
        film.setId(1);
        film.setName("updateName");
        film.setDescription("updateDescription");
        film.setDuration(111);
        film.setReleaseDate(LocalDate.of(2007, 1, 1));
        film.setMpa(filmMpa);

        filmStorage.update(film);
        List<Film> films = filmStorage.getAll();

        assertEquals(2, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "updateName");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "updateDescription");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 111);
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrProperty("mpa");
    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql"})
    void shouldGetAll() {
        List<Film> films = filmStorage.getAll();

        assertEquals(2, films.size());

        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "name1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "description1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 200);
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrProperty("mpa");

        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "name2");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "description2");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", 250);
        assertThat(films.get(1)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(1)).hasFieldOrProperty("mpa");
    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql"})
    void shouldGetFilmById() {
        Film film = filmStorage.getFilmById(2);

        assertThat(film).hasFieldOrPropertyWithValue("id", 2);
        assertThat(film).hasFieldOrPropertyWithValue("name", "name2");
        assertThat(film).hasFieldOrPropertyWithValue("description", "description2");
        assertThat(film).hasFieldOrPropertyWithValue("duration", 250);
        assertThat(film).hasFieldOrProperty("releaseDate");
        assertThat(film).hasFieldOrProperty("mpa");
    }

    @Test
    void shouldGetMpaById() {
        FilmMpa mpa1 = filmStorage.getMpaById(1);
        FilmMpa mpa2 = filmStorage.getMpaById(2);
        FilmMpa mpa3 = filmStorage.getMpaById(3);
        FilmMpa mpa4 = filmStorage.getMpaById(4);
        FilmMpa mpa5 = filmStorage.getMpaById(5);

        assertEquals(mpa1.getName(), "G");
        assertEquals(mpa2.getName(), "PG");
        assertEquals(mpa3.getName(), "PG-13");
        assertEquals(mpa4.getName(), "R");
        assertEquals(mpa5.getName(), "NC-17");
    }

    @Test
    void shouldGetAllMpas() {
        List<FilmMpa> mpas = filmStorage.getAllMpas();

        assertEquals(mpas.size(), 5);
        assertEquals(mpas.get(0).getName(), "G");
        assertEquals(mpas.get(1).getName(), "PG");
        assertEquals(mpas.get(2).getName(), "PG-13");
        assertEquals(mpas.get(3).getName(), "R");
        assertEquals(mpas.get(4).getName(), "NC-17");
    }

    @Test
    void shouldGetAllGenres() {
        List<Genre> genres = filmStorage.getAllGenres();
        assertEquals(genres.size(), 6);
    }

    @Test
    void shouldGetGenreById() {
        Genre genre1 = filmStorage.getGenreById(1);
        Genre genre2 = filmStorage.getGenreById(2);
        Genre genre3 = filmStorage.getGenreById(3);
        Genre genre4 = filmStorage.getGenreById(4);
        Genre genre5 = filmStorage.getGenreById(5);
        Genre genre6 = filmStorage.getGenreById(6);

        assertEquals(genre1.getName(), "Комедия");
        assertEquals(genre2.getName(), "Драма");
        assertEquals(genre3.getName(), "Мультфильм");
        assertEquals(genre4.getName(), "Триллер");
        assertEquals(genre5.getName(), "Документальный");
        assertEquals(genre6.getName(), "Боевик");
    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql"})
    void shouldGetAllGenresByFilmId() {
        filmStorage.putGenreIdAndFilmId(1, 4);
        filmStorage.putGenreIdAndFilmId(1, 6);
        Set<Genre> films = filmStorage.getAllGenresByFilmId(1);

        assertEquals(films.size(), 2);
    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql", "/userDbStorageTest.sql"})
    void shouldAddLikeToFilmAndGetAllFilmLikes() {
        User user = userStorage.getUserById(0);
        User user2 = userStorage.getUserById(1);
        Film film = filmStorage.getFilmById(1);
        Film film2 = filmStorage.getFilmById(2);

        filmStorage.addLikeToFilm(user.getId(),film.getId());
        filmStorage.addLikeToFilm(user2.getId(),film.getId());
        filmStorage.addLikeToFilm(user.getId(),film2.getId());


        assertEquals(filmStorage.getAllFilmLikes(1).size(),2);
        assertEquals(filmStorage.getAllFilmLikes(2).size(),1);
    }

    @Test
    @Sql(scripts = {"/filmDbStorageTest.sql", "/userDbStorageTest.sql"})
    void shouldRemoveLikeFromFilm() {
        User user = userStorage.getUserById(0);
        User user2 = userStorage.getUserById(1);
        Film film = filmStorage.getFilmById(1);
        Film film2 = filmStorage.getFilmById(2);

        filmStorage.addLikeToFilm(user.getId(),film.getId());
        filmStorage.addLikeToFilm(user2.getId(),film.getId());
        filmStorage.addLikeToFilm(user.getId(),film2.getId());


        assertEquals(filmStorage.getAllFilmLikes(1).size(),2);
        assertEquals(filmStorage.getAllFilmLikes(2).size(),1);

        filmStorage.removeLikeFromFilm(user.getId(),film.getId());
        filmStorage.removeLikeFromFilm(user.getId(),film2.getId());

        assertEquals(filmStorage.getAllFilmLikes(1).size(),1);
        assertEquals(filmStorage.getAllFilmLikes(2).size(),0);
    }
}