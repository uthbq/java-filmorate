package ru.yandex.practicum.filmorate.controller;

import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(final ValidateException e) {
        log.error("Получен статус 400 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCreationException(final CreationException e) {
        log.error("Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUpdateException(final UpdateException e) {
        log.error("Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmptyResultDataAccessException(final EmptyResultDataAccessException e) {
        log.error("Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleElementIsNullException(final ElementIsNullException e) {
        log.error("Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserIsNullException(final UserNotFoundException e) {
        log.error("Получен статус 404 {}", e.getMessage());
        return new ErrorResponse("Ошибка : " + e.getMessage());
    }

}
