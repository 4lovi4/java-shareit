package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd());
    }

    public static NearBookingDto toNearBooking(Booking booking) {
        return new NearBookingDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static BookingResponse toBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemInBookingResponse(booking.getItem().getId(),
                        booking.getItem().getName()))
                .booker(new BookerInBookingResponse(booking.getBooker().getId()))
                .build();
    }
}
