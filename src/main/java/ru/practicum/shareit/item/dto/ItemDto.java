package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ItemDto {
    private Long id;
    @NonNull
    @NotBlank(message = "Поле name не должно быть пустым")
    private final String name;
    @NonNull
    @NotBlank(message = "Поле description не должно быть пустым")
    private final String description;
    @NonNull
    @NotNull(message = "Поле available не должно быть пустым")
    private final Boolean available;
}
