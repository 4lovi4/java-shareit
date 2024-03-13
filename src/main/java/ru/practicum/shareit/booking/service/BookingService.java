package ru.practicum.shareit.booking.service;


import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingResponse addNewBooking(Long userId, BookingDto bookingDto);

    BookingResponse changeBookingStatus(Long bookingId, Long userId, Boolean bookingApprove);

    BookingResponse getBookingById(Long bookingId, Long userId);

    Page<BookingResponse> getAllBookingsByBookerAndState(Long userId, BookingDtoState state, Integer fromIndex, Integer size);

    Page<BookingResponse> getAllBookingsByOwnerAndState(Long userId, BookingDtoState state, Integer fromIndex, Integer size);

}
