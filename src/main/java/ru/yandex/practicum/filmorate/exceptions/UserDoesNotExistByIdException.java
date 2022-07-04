package ru.yandex.practicum.filmorate.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDoesNotExistByIdException extends Exception {


    public UserDoesNotExistByIdException(String message) {
        super(message);
    }
}