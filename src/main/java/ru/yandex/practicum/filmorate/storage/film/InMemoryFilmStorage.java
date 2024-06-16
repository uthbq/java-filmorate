package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private int incrementId() {
        return id++;
    }

    @Override
    public Film create(Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("Такой фильм уже существует.");
        }
        film.setId(incrementId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен.", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsValue(film)) {
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен.", film.getName());
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> allFilms = new ArrayList<>(films.values());
        return allFilms;
    }

    @Override
    public Film getById(int filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Нет фильма с таким id.");
        }
        return film;
    }

    @Override
    public void deleteById(Integer id) {
        Film filmToRemove = getById(id);
        if (filmToRemove == null) {
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.remove(id);
        log.info("Фильм {} удален.", filmToRemove.getName());
    }
}