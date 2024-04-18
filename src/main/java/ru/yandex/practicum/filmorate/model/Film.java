package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    @Min(1)
    private int duration;
    @NotNull
    private final LocalDate releaseDate;


}
