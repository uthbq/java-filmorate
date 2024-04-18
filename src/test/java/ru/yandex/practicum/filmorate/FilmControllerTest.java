package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FilmControllerTest {


    private Validator validator;

    @Test
    void testFilmValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = Film.builder()
                .name("Transformers")
                .description("Test film")
                .duration(120)
                .releaseDate(LocalDate.parse("1900-05-25"))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    void invalidFilmNameValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = Film.builder()
                .name("")
                .description("Test film")
                .duration(120)
                .releaseDate(LocalDate.parse("1900-05-25"))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    void invalidFilmDescriptionValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = Film.builder()
                .name("Transformers")
                .description("")
                .duration(120)
                .releaseDate(LocalDate.parse("1900-05-25"))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    void invalidFilmDurationValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = Film.builder()
                .name("Transformers")
                .description("Test film")
                .duration(0)
                .releaseDate(LocalDate.parse("1900-05-25"))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
