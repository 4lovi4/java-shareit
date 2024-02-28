package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;


import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    @NotNull(message = "поле start в Booking не должно быть пустым")
    @Future(message = "Поле start в Booking должно быть в будущем")
    private LocalDateTime start;
    @NotNull(message = "поле end в Booking не должно быть пустым")
    @Future(message = "Поле end в Booking должно быть в будущем")
    private LocalDateTime end;
    private BookingStatus status;
    private User booker;
    @NonNull
    private Long itemId;
}
