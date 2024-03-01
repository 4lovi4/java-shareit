package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NonNull
    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;
    @NonNull
    @NotBlank(message = "Поле description не должно быть пустым")
    private String description;
    @NonNull
    @NotNull(message = "Поле available не должно быть пустым")
    private Boolean available;
}
