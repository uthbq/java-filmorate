package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.HandleNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    public Film create(Film film) {

        return filmStorage.create(film);
    }

    public Film update(Film film) {

        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    public void deleteById(Integer id) {
        filmStorage.deleteById(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new HandleNotFoundException("Нет пользователя с таким id.");
        }
        Film film = filmStorage.getById(filmId);
        film.getLike().add(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new HandleNotFoundException("Нет пользователя с таким id.");
        }
        Film film = filmStorage.getById(filmId);
        film.getLike().remove(userId);
        return film;
    }

    public List<Film> fetchTopRatedFilms(int filmsCount) {
        int popularCount = (filmsCount > 0) ? filmsCount : 10;
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLike().size()).reversed())
                .limit(popularCount)
                .collect(Collectors.toList());
    }
}