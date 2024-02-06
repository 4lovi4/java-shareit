package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(Long userId, BookingDto bookingDto);
    BookingDto changeBookingStatus(Long bookingId, Long userId, Boolean bookingApprove);
    BookingDto getBookingById(Long bookingId, Long userId);
    List<BookingDto> getAllBookingsByBookerWithStatusAndState(Long userId, BookingStatus status, BookingDtoState state);
    List<BookingDto> getAllBookingsByOwnerAndState(Long userId, BookingDtoState state);

}
