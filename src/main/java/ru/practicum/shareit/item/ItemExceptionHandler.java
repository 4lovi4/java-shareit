package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.error.ErrorResponseModel;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ItemExceptionHandler {

    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json";
    private static final String ITEM_NOT_FOUND_CODE = "ITEM_NOT_FOUND";
    private static final String ITEM_WRONG_CODE = "ITEM_NOT_VALID";
    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private final ObjectMapper objectMapper;

    @Autowired
    public ItemExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<String> handleUserNotFound(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                USER_NOT_FOUND_CODE,
                exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler({ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<String> handleItemNotFound(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                ITEM_NOT_FOUND_CODE,
                exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler(ItemWrongRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleWrongRequestError(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                ITEM_WRONG_CODE,
                exception.getMessage(),
                request.getRequestURL().toString()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleValidationError(HttpServletRequest request, MethodArgumentNotValidException exception) throws JsonProcessingException {
        StringBuilder errorStringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(e ->
                errorStringBuilder.append(e.getDefaultMessage() + ";"));
        ErrorResponseModel error = new ErrorResponseModel(
                ITEM_WRONG_CODE,
                errorStringBuilder.toString(),
                request.getRequestURL().toString()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }
}
