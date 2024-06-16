package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") Integer genreId) {
        log.info("Запрос на получение жанра с id: " + genreId);
        return filmService.getGenreById(genreId);
    }

    @GetMapping()
    public List<Genre> getAllGenres() {
        log.info("Запрос на получение всех жанров");
        return filmService.getAllGenres();
    }

}
