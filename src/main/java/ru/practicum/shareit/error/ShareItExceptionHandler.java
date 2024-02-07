package ru.practicum.shareit.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ShareItExceptionHandler {

    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json";
    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private static final String USER_DUPLICATION_CODE = "USER_DUPLICATED";
    private static final String DATA_VIOLATION_ERROR_CODE = "DATA_VIOLATED";
    private static final String VALIDATION_CODE = "PAYLOAD_NOT_VALID";
    private static final String ITEM_NOT_FOUND_CODE = "ITEM_NOT_FOUND";
    private static final String ITEM_WRONG_CODE = "ITEM_NOT_VALID";
    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_SERVER_ERROR";
    private final ObjectMapper objectMapper;

    @Autowired
    public ShareItExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    @SneakyThrows
    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<String> handleUserDataIntegrityViolation(HttpServletRequest request, DataIntegrityViolationException exception) {
        ErrorResponseModel error = new ErrorResponseModel(DATA_VIOLATION_ERROR_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleValidationError(HttpServletRequest request, MethodArgumentNotValidException exception) throws JsonProcessingException {
        StringBuilder errorStringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(e ->
                errorStringBuilder.append(e.getDefaultMessage() + ";"));
        ErrorResponseModel error = new ErrorResponseModel(
                VALIDATION_CODE,
                errorStringBuilder.toString(),
                request.getRequestURL().toString()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> handleUnexpectedException(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                UNEXPECTED_ERROR_CODE,
                exception.getMessage(),
                request.getRequestURL().toString()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
