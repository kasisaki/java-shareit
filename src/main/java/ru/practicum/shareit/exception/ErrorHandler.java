package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.errorResponse.ErrorResponse;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
// наследуемся чтобы не писать множество стандартных обработчиков
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchBadRequestException(final BadRequestException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST.value(), e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchNotFoundException(final ElementNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND.value(), e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchConflictException(final ConflictException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(CONFLICT.value(), e.getMessage()), CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchPSQLException(final PSQLException e) {
        log.error(e.getMessage(), e);
        HttpStatus httpStatus = CONFLICT;
        if (e.getMessage().contains("null value in column \"email\"")) {
            httpStatus = BAD_REQUEST;
        }
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), e.getMessage()), httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST.value(), e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchIllegalStatusException(final IllegalStatusException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }


}