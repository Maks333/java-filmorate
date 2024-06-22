package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler
    public Map<String, String> handleNotFoundError(NotFoundException e) {
    }

    @ExceptionHandler
    public Map<String, String> handleValidationError(ValidationException e) {

    }

    @ExceptionHandler
    public Map<String, String> handleError(RuntimeException e) {

    }
}
