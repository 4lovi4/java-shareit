package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    private final String name;
    @NotBlank(message = "Поле description не должно быть пустым")
    private final String description;
    @NotNull(message = "Поле available не должно быть пустым")
    private final Boolean available;
}
