package ru.practicum.shareit.request.exception;

public class RequestNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Запрос id = %d не найден";

    public RequestNotFoundException() {
    }

    public RequestNotFoundException(Long itemId) {
        super(String.format(ERROR_MESSAGE, itemId));
    }

    public RequestNotFoundException(Long itemId, Throwable cause) {
        super(String.format(ERROR_MESSAGE, itemId), cause);
    }
}
