package ru.yandex.practicum.filmorate.model;


import javax.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    private Integer id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта не корректна")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("EMAIL", email);
        map.put("NAME", name);
        map.put("LOGIN", login);
        map.put("BIRTHDAY", birthday);
        return map;
    }
}
