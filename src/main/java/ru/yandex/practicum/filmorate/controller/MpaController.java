package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public FilmMpa getMpaById(@PathVariable("id") Integer mpaId) {
        log.info("Запрос на получение рейтинга с id: " + mpaId);
        return filmService.getMpaById(mpaId);
    }

    @GetMapping()
    public List<FilmMpa> getAllMpas() {
        log.info("Запрос на получение всех рейтингов");
        return filmService.getAllMpas();
    }

}
