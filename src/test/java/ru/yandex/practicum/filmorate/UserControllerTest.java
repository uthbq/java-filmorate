package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private Validator validator;

    @Test
    public void testUserValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        UserController controller = new UserController();
        User user = User.builder()
                .email("george@yandex.ru")
                .name("George")
                .login("georgekate")
                .birthday(LocalDate.parse("2005-11-14"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidUserValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        UserController controller = new UserController();
        User user = User.builder()
                .email("")
                .name("George")
                .login("georgekate")
                .birthday(LocalDate.parse("2005-11-14"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidUserValidation1() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        UserController controller = new UserController();
        User user = User.builder()
                .email("george@yandex.ru")
                .name("")
                .login("georgekate")
                .birthday(LocalDate.parse("2005-11-14"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidUserValidation2() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        UserController controller = new UserController();
        User user = User.builder()
                .email("george@yandex.ru")
                .name("George")
                .login("")
                .birthday(LocalDate.parse("2005-11-14"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }
}