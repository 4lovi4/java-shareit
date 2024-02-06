package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Future;


import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @NotNull(message = "поле start в Booking не должно быть пустым")
    @Past(message = "Поле start в Booking должно быть в прошлом")
    private final LocalDateTime start;
    @NotNull(message = "поле end в Booking не должно быть пустым")
    @Future(message = "Поле end в Booking должно быть в будущем")
    private final LocalDateTime end;
    private BookingStatus status;
    private User booker;
    private Item item;
}
