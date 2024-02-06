package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Comparator;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        itemDto.setId(item.getId());
        return itemDto;
    }

    public static ItemWithBookingDto toItemWithBookingDto(Item item) {
        ItemWithBookingDto itemBookingDto = new ItemWithBookingDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        itemBookingDto.setId(item.getId());
        itemBookingDto.setLastBooking(BookingMapper
                .toNearBooking(item
                        .getBookings()
                        .stream()
                        .min(Comparator.comparing(Booking::getStart)).get()));
        itemBookingDto.setNextBooking(BookingMapper
                .toNearBooking(item
                        .getBookings()
                        .stream()
                        .max(Comparator.comparing(Booking::getStart)).get()));
        return itemBookingDto;
    }

    public static Item toItem(ItemDto item) {
        return new Item(item.getName(),
                item.getDescription(),
                item.getAvailable());
    }
}
