package ru.practicum.shareit.booking.exception;

public class BookingNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Бронирование id = %d не найден";

    public BookingNotFoundException() {
    }

    public BookingNotFoundException(Long bookingId) {
        super(String.format(ERROR_MESSAGE, bookingId));
    }

    public BookingNotFoundException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
