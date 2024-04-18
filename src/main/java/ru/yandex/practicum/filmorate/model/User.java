package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
public class User {
    private Integer id;

    private final Set<Integer> friends = new HashSet<>();
    private String name;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    @NotEmpty
    private String login;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    private String email;
}
