package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class UserExceptionHandler {

    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json";
    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private static final String USER_DUPLICATION_CODE = "USER_DUPLICATED";
    private static final String USER_VALIDATION_CODE = "USER_NOT_VALID";

    private final ObjectMapper objectMapper;
    @Autowired
    public UserExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<String> handleUserNotFound(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(USER_NOT_FOUND_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler({UserDuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<String> handleUserDuplication(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(USER_DUPLICATION_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleValidationError(HttpServletRequest request, Exception exception) throws JsonProcessingException {
        ErrorResponseModel error = new ErrorResponseModel(USER_VALIDATION_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }
}
