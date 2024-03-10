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
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingWrongRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemUnavailableException;
import ru.practicum.shareit.item.exception.ItemWrongRequestException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

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
    private static final String ITEM_UNAVAILABLE_CODE = "ITEM_UNAVAILABLE";
    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_SERVER_ERROR";
    private static final String BOOKING_NOT_FOUND_CODE = "BOOKING_NOT_FOUND";
    private final ObjectMapper objectMapper;

    @Autowired
    public ShareItExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @SneakyThrows
    @ExceptionHandler({ItemNotFoundException.class, RequestNotFoundException.class})
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
    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleUnavailableError(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                ITEM_UNAVAILABLE_CODE,
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
    @ExceptionHandler({MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleHeaderNotProvided(HttpServletRequest request, MissingRequestHeaderException exception) {
        ErrorResponseModel error = new ErrorResponseModel(DATA_VIOLATION_ERROR_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleNotValidParams(HttpServletRequest request, ConstraintViolationException exception) {
        ErrorResponseModel error = new ErrorResponseModel(DATA_VIOLATION_ERROR_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
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
    @ExceptionHandler({BookingWrongRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleBookingValidation(HttpServletRequest request, RuntimeException exception) {
        ErrorResponseModel error = new ErrorResponseModel(DATA_VIOLATION_ERROR_CODE, exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleBookingStateValidation(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        String[] exceptionMessageWords = exception.getMessage().split("\\.");
        String valueName = exceptionMessageWords[exceptionMessageWords.length - 1];
        ErrorResponseModel error = new ErrorResponseModel(DATA_VIOLATION_ERROR_CODE,
                "Unknown state: " + valueName,
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler({BookingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<String> handleBookingNotFound(HttpServletRequest request, Exception exception) {
        ErrorResponseModel error = new ErrorResponseModel(
                BOOKING_NOT_FOUND_CODE,
                exception.getMessage(),
                request.getRequestURL().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.NOT_FOUND);
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
        log.debug("Exception Unknown Class: {}", exception.getClass());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE);
        return new ResponseEntity<>(objectMapper.writeValueAsString(error), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
