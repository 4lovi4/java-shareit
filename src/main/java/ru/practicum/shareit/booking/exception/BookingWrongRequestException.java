package ru.practicum.shareit.booking.exception;

public class BookingWrongRequestException extends RuntimeException {

    public BookingWrongRequestException() {
    }

    public BookingWrongRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BookingWrongRequestException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
