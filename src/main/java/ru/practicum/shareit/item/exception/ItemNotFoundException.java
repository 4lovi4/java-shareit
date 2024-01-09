package ru.practicum.shareit.item.exception;

public class ItemNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Предмет id = %d не найден";

    public ItemNotFoundException() {
    }

    public ItemNotFoundException(Long itemId) {
        super(String.format(ERROR_MESSAGE, itemId));
    }

    public ItemNotFoundException(Long itemId, Throwable cause) {
        super(String.format(ERROR_MESSAGE, itemId), cause);
    }
}
