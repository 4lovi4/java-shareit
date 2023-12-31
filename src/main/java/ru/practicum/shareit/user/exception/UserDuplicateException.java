package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.user.User;

public class UserDuplicateException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Пользователь %s уже добавлен";

    public UserDuplicateException() {
    }

    public UserDuplicateException(User user) {
        super(String.format(ERROR_MESSAGE, user.toString()));
    }

    public UserDuplicateException(User user, Throwable cause) {
        super(String.format(ERROR_MESSAGE, user), cause);
    }
}
