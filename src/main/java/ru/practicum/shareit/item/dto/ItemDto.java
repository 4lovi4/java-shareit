package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
}
