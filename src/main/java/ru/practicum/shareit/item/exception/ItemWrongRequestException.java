package ru.practicum.shareit.item.exception;

public class ItemWrongRequestException extends RuntimeException {
    ;

    public ItemWrongRequestException() {
    }

    public ItemWrongRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ItemWrongRequestException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
