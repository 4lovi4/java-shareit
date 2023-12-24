package ru.practicum.shareit.booking.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingDto {
    private final Long id;
    private final Long item;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long booker;
}
