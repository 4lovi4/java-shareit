package ru.practicum.shareit.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponseModel {
    private final String code;
    @JsonProperty("error")
    private final String message;
    private final String path;
}
