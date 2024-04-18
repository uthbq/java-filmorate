package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Set<Film> films = new HashSet<>();
    private int id = 1;

    private int incrementId() {
        return id++;
    }

    @Override
    public Film create(Film film) {
        if (films.contains(film)) {
            throw new ValidationException("Такой фильм уже существует.");
        }
        film.setId(incrementId());
        films.add(film);
        log.info("Фильм {} добавлен.", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.contains(film)) {
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.add(film);
        log.info("Фильм {} обновлен.", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> allFilms = new ArrayList<>(films);
        return allFilms;
    }

    @Override
    public Film getById(Integer id) {
        return films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Нет фильма с таким id."));
    }

    @Override
    public void deleteById(Integer id) {
        Film filmToRemove = getById(id);
        if (filmToRemove != null) {
            films.remove(filmToRemove);
            log.info("Фильм {} удален.", filmToRemove.getName());
        } else {
            throw new NotFoundException("Такого фильма не существует.");
        }
    }
}