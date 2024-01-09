package ru.practicum.shareit.user.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Пользователь id = %d не найден";

    public UserNotFoundException() {
    }

    public UserNotFoundException(Long userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }

    public UserNotFoundException(Long userId, Throwable cause) {
        super(String.format(ERROR_MESSAGE, userId), cause);
    }
}
