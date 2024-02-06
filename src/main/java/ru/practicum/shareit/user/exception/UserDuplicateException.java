package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.user.model.User;

public class UserDuplicateException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Пользователь c email %s уже добавлен";

    public UserDuplicateException() {
    }

    public UserDuplicateException(User user) {
        super(String.format(ERROR_MESSAGE, user.getEmail()));
    }

    public UserDuplicateException(User user, Throwable cause) {
        super(String.format(ERROR_MESSAGE, user.getEmail()), cause);
    }
}
