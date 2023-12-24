package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ItemRequestDto {
    private final Long id;
    private final String description;
    private final Long requester;
    private final LocalDateTime created;
}
