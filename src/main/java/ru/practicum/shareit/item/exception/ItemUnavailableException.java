package ru.practicum.shareit.item.exception;

public class ItemUnavailableException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Предмет id = %d не доступен для бронирования";

    public ItemUnavailableException() {
    }

    public ItemUnavailableException(Long itemId) {
        super(String.format(ERROR_MESSAGE, itemId));
    }

    public ItemUnavailableException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
