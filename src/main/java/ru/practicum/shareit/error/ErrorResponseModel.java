package ru.practicum.shareit.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponseModel {
    private final String code;
    private final String message;
    private final String path;
}
