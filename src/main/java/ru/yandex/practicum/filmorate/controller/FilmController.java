package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private final LocalDate START_DATE = LocalDate.of(1895, 12, 27);

    @GetMapping
    public List<Film> findAll() {
        log.info("Фильмов в коллекции: {}", films.size());
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        isValidFilm(film);
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        isValidFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Невозможно обновить фильм");
            throw new ValidationException("Невозможно обновить фильм");
        }
        films.put(film.getId(), film);
        log.info("Фильм c id {} обновлён", film.getId());
        return film;
    }

    private void isValidFilm(Film film) {
        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.warn("Валидация не пройдена для фильма: {}", film);
            throw new ValidationException("Фильм не прошел валидацию");
        }
    }

    private int generatorId() {
        return filmId++;
    }
}
