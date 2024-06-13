package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {


    private final FilmService filmService;


    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Запрос на создание фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Запрос на обновление фильма c id: " + film.getId());
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Запрос на получение всех фильмов ");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Запрос на получение фильма с id: " + id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Запрос на добавление лайка фильму с id: " + id + " от пользователя с id: " + userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Запрос на удаление лайка фильму с id: " + id + " от пользователя с id: " + userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikeFilms(@RequestParam(required = false) Integer count) {
        log.info("Запрос на получение " + count + " популярных фильмов");
        return filmService.mostLikeFilms(count);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable("id") int id) {
        log.info("Запрос на удаление фильма с id {}", id);
        filmService.deleteFilm(id);
    }
    @GetMapping("/common")
    public List<Film> getCommonsFilms(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "friendId") int friendId
    ) {
        return filmService.getCommonFilms(userId, friendId);
    }


}