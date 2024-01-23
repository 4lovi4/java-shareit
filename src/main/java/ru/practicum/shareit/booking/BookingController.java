package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.practicum.shareit.booking.service.BookingService;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody BookingDto bookingRequestBody) {
        return null;
    }

    @PatchMapping("/{booking_id}")
    public BookingDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable(name = "booking_id") Long bookingId,
                                          @RequestParam(name = "approved") Boolean approved) {
        return null;
    }

    @GetMapping("/{booking_id}")
    public BookingDto getBookingInfo(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "booking_id") Long bookingId) {
        return null;
    }


}
