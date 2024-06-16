package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film getFilmById(int id);

    void putGenreIdAndFilmId(int filmId, int genreId);

    FilmMpa getMpaById(int id);

    List<FilmMpa> getAllMpas();

    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    Set<Genre> getAllGenresByFilmId(int filmId);

    Film addLikeToFilm(int userId, int filmId);

    Film removeLikeFromFilm(int userId, int filmId);

    Set<Integer> getAllFilmLikes(int filmId);

    void deleteFilm(int id);
    List<Film> getCommonFilms(int userId, int friendId);
}
