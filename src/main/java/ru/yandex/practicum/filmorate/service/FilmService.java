package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.ElementIsNullException;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public Film create(Film film) {
        validateFilm(film);
        if (film.getId() == null) {
            genreAndMpaValidation(film);
            Integer newId = filmStorage.create(film).getId();
            film.setId(newId);
            addFilmGenresToDb(film);
            return film;
        }
        if (!(filmStorage.getFilmById(film.getId()) == null)) {
            throw new CreationException("Фильм с id: " + film.getId() + " уже существует");
        }
        return filmStorage.create(film);
    }


    public Film update(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new UpdateException("Фильм с id: " + film.getId() + " не найден");
        }
        return filmStorage.update(film);
    }


    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        genreAndMpaValidation(film);
        film.setGenres(getAllGenresByFilmId(id));
        return film;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            throw new ElementIsNullException("Фильм и/или пользователь не найден(ы)");
        }
        return filmStorage.addLikeToFilm(userId, filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            throw new ElementIsNullException("Фильм и/или пользователь не найден(ы)");
        }
        return filmStorage.removeLikeFromFilm(userId, filmId);
    }

    public List<Film> mostLikeFilms(Integer count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(o -> (filmStorage.getAllFilmLikes(((Film) o).getId()).size())).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    private void addFilmGenresToDb(Film film) {
        for (Genre genre : film.getGenres()) {
            filmStorage.putGenreIdAndFilmId(film.getId(), Collections.singletonList(genre.getId()));
        }
    }

    private void genreAndMpaValidation(Film film) {
        if (film.getMpa() != null) {
            try {
                FilmMpa mpa = filmStorage.getMpaById(film.getMpa().getId());
                if (mpa == null) {
                    throw new ValidateException("Mpa с id: " + film.getMpa().getId() + " не существует");
                }
                film.setMpa(mpa);
            } catch (EmptyResultDataAccessException e) {
                throw new ValidateException("Mpa с id: " + film.getMpa().getId() + " не существует");
            }
        }
        if (film.getGenres() != null) {
            try {
                Set<Genre> genres = getAllGenres().stream()
                        .peek(genre -> genre.setName(null))
                        .collect(Collectors.toSet());
                for (Genre genre : film.getGenres()) {
                    if (!genres.contains(genre)) {
                        throw new ValidateException("Жанр с id: " + genre.getId() + " не существует");
                    }
                }
            } catch (EmptyResultDataAccessException e) {
                throw new ValidateException("Жанр не существует");
            }
        }
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public FilmMpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }

    public List<FilmMpa> getAllMpas() {
        return filmStorage.getAllMpas();
    }

    public Set<Genre> getAllGenresByFilmId(int filmId) {
        return filmStorage.getAllGenresByFilmId(filmId);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId,friendId);
    }

}
