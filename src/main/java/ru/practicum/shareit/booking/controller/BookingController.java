package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController("bookingRestController")
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody BookingDto bookingRequestBody) {
        return bookingService.addNewBooking(userId, bookingRequestBody);
    }

    @PatchMapping("/{booking_id}")
    public BookingResponse changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable(name = "booking_id") Long bookingId,
                                          @RequestParam(name = "approved", defaultValue = "true") Boolean approved) {
        return bookingService.changeBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{booking_id}")
    public BookingResponse getBookingInfo(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "booking_id") Long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponse> getAllBookingsToCurrentUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                 BookingDtoState state) {

        return bookingService.getAllBookingsByBookerAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getAllBookingsByOwnerItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                      BookingDtoState state) {
        return bookingService.getAllBookingsByOwnerAndState(userId, state);
    }
}
